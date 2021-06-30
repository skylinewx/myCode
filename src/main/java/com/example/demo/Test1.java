package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test1 {

    private static final Logger logger = LoggerFactory.getLogger(Test1.class);

    public static void main(String[] args) {
        SkylinePool pool = new SkylinePool(2, 2);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            pool.submit(new Task(finalI) {
                @Override
                public void run() {
                    logger.info("[{}]正在执行任务[{}]",Thread.currentThread().getName(),finalI);
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logger.info("[{}]执行任务[{}]完毕",Thread.currentThread().getName(),finalI);
                }
            });
        }
        pool.shutDown();
        logger.info("pool已经被停止");
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程池，单线程提交任务版
     */
    private static class SkylinePool {
        private final int coreSize;
        private final TaskHandler[] threads;
        private final TaskQueue taskQueue;
        private int endPoint;
        private boolean shutDown;

        private SkylinePool(int coreSize,int maxQueueSize) {
            this.coreSize = coreSize;
            this.threads = new TaskHandler[coreSize];
            taskQueue = new TaskQueue(maxQueueSize);
        }

        public void submit(Task task) {
            if (shutDown) {
                return;
            }
            if (endPoint < coreSize) {
                logger.info("线程池还没到最大限度，任务[{}]被直接启动执行",task.getTaskNum());
                TaskHandler taskHandler = new TaskHandler(task, taskQueue, "t"+endPoint);
                threads[endPoint] = taskHandler;
                taskHandler.start();
                endPoint++;
            } else {
                logger.info("线程池已到达最大限度，任务[{}]尝试进入队列",task.getTaskNum());
                taskQueue.push(task);
            }
        }

        public void shutDown(){
            shutDown = true;
            for (int i = 0; i < coreSize; i++) {
                TaskHandler thread = threads[i];
                if (thread == null) {
                    continue;
                }
                while (!thread.isEnd() && !thread.isInterrupted()){
                    logger.info("尝试打断[{}]",thread.getName());
                    thread.interrupt();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                threads[i] = null;
            }
        }
    }

    private static class TaskQueue{
        private final int maxSize;
        private final Deque<Task> taskDeque;
        private final Lock lock = new ReentrantLock();
        private final Condition emptyCondition = lock.newCondition();
        private final Condition fullCondition = lock.newCondition();

        private TaskQueue(int maxSize) {
            this.maxSize = maxSize;
            taskDeque = new ArrayDeque<>();
        }

        /**
         * 从任务队列中取出一个task，如果没有，就阻塞
         * @return
         */
        public Task take(){
            if (Thread.currentThread().isInterrupted()) {
                logger.info("[{}]已经被打断",Thread.currentThread().getName());
                return null;
            }
            logger.info("[{}]开始获取任务",Thread.currentThread().getName());
            lock.lock();
            try {
                while (taskDeque.size()==0){
                    logger.info("队列为空，等待其他线程放入任务后，才能取出任务,[{}]",Thread.currentThread().getName());
                    try {
                        emptyCondition.await();
                    } catch (InterruptedException e) {
//                        e.printStackTrace();
                        logger.info("[{}]已经被打断",Thread.currentThread().getName());
                        Thread.currentThread().interrupt();
                        return null;
                    }
                }
                logger.info("取出了一个任务,[{}]",Thread.currentThread().getName());
                Task pop = taskDeque.pop();
                fullCondition.signalAll();
                return pop;
            }finally {
                lock.unlock();
            }
        }

        /**
         * 向队列中添加一个任务，如果队列到达最大长度，那就阻塞
         * @param task
         */
        public void push(Task task){
            logger.info("[{}]开始放入任务[{}]",Thread.currentThread().getName(),task.getTaskNum());
            lock.lock();
            try {
                while (taskDeque.size()==maxSize){
                    logger.info("队列已满，等待任务被取出后，才能继续,[{}]",Thread.currentThread().getName());
                    try {
                        fullCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                logger.info("任务[{}]成功进入队列,[{}]",task.getTaskNum(),Thread.currentThread().getName());
                taskDeque.add(task);
                emptyCondition.signalAll();
            }finally {
                lock.unlock();
            }
        }
    }

    private static class TaskHandler extends Thread{
        private Task task;
        private boolean end;
        private final TaskQueue taskQueue;

        public TaskHandler(Task task,TaskQueue taskQueue,String name){
            this.task = task;
            this.taskQueue = taskQueue;
            setName(name);
        }

        @Override
        public void run() {
            while (task!=null || (task=taskQueue.take())!=null){
                logger.info("[{}]开始执行任务[{}]",Thread.currentThread().getName(),task.getTaskNum());
                task.run();
                if (Thread.currentThread().isInterrupted()) {
                    logger.info("[{}]已经被打断",Thread.currentThread().getName());
                    return;
                }
                task = null;
            }
            logger.info("[{}]no task",Thread.currentThread().getName());
            end = true;
        }

        public boolean isEnd() {
            return end;
        }
    }

    private static abstract class Task implements Runnable{

        private final int taskNum;

        private Task(int taskNum) {
            this.taskNum = taskNum;
        }

        public int getTaskNum() {
            return taskNum;
        }
    }

}
