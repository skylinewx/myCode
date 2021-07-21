package com.example.juc;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootTest
class DemoApplicationTests9 {

    private static final Logger logger = LoggerFactory.getLogger(DemoApplicationTests9.class);

    @Test
    public void test() {
        SkylinePool pool = new SkylinePool(2, 5,((taskQueue, task) -> {
            //阻塞等待
            logger.info("-----------[{}]尝试放入任务[{}]",Thread.currentThread().getName(),task.getTaskNum());
            taskQueue.push(task);
            logger.info("------------[{}]放入任务[{}]成功",Thread.currentThread().getName(),task.getTaskNum());
        }));
        for (int i = 0; i < 10; i++) {
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
        logger.info("开始停止pool");
        pool.shutDownNow();
        logger.info("pool已经被停止");
    }

    /**
     * 线程池，单线程提交任务版
     */
    private static class SkylinePool {
        //线程池的线程数
        private final int coreSize;
        //线程池中的线程的集合
        private final TaskHandler[] threads;
        //线程池中任务的等待队列
        private final TaskQueue taskQueue;
        //指针，指向threads的末尾
        private int endPoint;
        //是否已停止
        private boolean shutDown;
        //拒绝策略
        private final RejectStrategy rejectStrategy;

        /**
         * 构造函数
         * @param coreSize 线程池中的线程个数
         * @param maxQueueSize 等待队列中的元素的最大个数
         * @param rejectStrategy 当等待队列已满时需要执行的逻辑
         */
        private SkylinePool(int coreSize,int maxQueueSize,RejectStrategy rejectStrategy) {
            this.coreSize = coreSize;
            this.threads = new TaskHandler[coreSize];
            taskQueue = new TaskQueue(maxQueueSize);
            this.rejectStrategy = rejectStrategy;
        }

        /**
         * 向线程池中提交一个任务
         * @param task
         */
        public void submit(Task task) {
            //如果线程池已经停止，那就不再允许提交了
            if (shutDown) {
                return;
            }
            //如果线程数还没满，那就先创建线程
            if (endPoint < coreSize) {
                logger.info("线程池还没到最大限度，任务[{}]被直接启动执行",task.getTaskNum());
                TaskHandler taskHandler = new TaskHandler(task, taskQueue, "t"+endPoint);
                threads[endPoint] = taskHandler;
                taskHandler.start();
                endPoint++;
            } else {
                //线程数已经满了之后就只能尝试放入队列了，这个try就有aqs那味儿了
                logger.info("线程池已到达最大限度，任务[{}]尝试进入队列",task.getTaskNum());
                boolean tryPush = taskQueue.tryPush(task);
                //尝试放入队列失败，一般就是队列满了，那就执行拒绝策略
                if (!tryPush) {
                    rejectStrategy.handle(taskQueue, task);
                }
            }
        }

        /**
         * 马上停止线程池
         */
        public void shutDownNow(){
            shutDown = true;
            for (int i = 0; i < coreSize; i++) {
                TaskHandler thread = threads[i];
                if (thread == null) {
                    continue;
                }
                while (thread.isAlive() && !thread.isInterrupted()){
                    logger.info("尝试打断[{}]",thread.getName());
                    thread.interrupt();
                    try {
                        //等待1秒是为了让出CPU
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                threads[i] = null;
            }
        }

        /**
         * 优雅的停止线程池
         */
        public void shutDown(){
            shutDown = true;
            for (int i = 0; i < coreSize; i++) {
                TaskHandler thread = threads[i];
                if (thread == null) {
                    continue;
                }
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                threads[i] = null;
            }
        }
    }

    /**
     * 任务队列，尝试写了一个双端同步FIFO队列
     */
    private static class TaskQueue{
        //队列中元素的最大值
        private final int maxSize;
        private final Task[] taskDeque;
        //头索引
        private int headIndex;
        //尾索引
        private int tailIndex;
        //同步锁
        private final Lock lock = new ReentrantLock();
        //条件锁，当队列为空时会阻塞
        private final Condition emptyCondition = lock.newCondition();
        //条件锁，当队列已满时会阻塞
        private final Condition fullCondition = lock.newCondition();

        /**
         * 初始化一个任务队列
         * @param maxSize
         */
        private TaskQueue(int maxSize) {
            if (maxSize<=0) {
                throw new RuntimeException("maxSize不能小于0");
            }
            this.maxSize = maxSize;
            //初始化队列
            taskDeque = new Task[maxSize];
        }

        /**
         * 从任务队列中取出一个task，如果没有，就阻塞
         * @return
         */
        public Task take(){
            //如果当前线程已经被打断，那就直接返回null
            if (Thread.currentThread().isInterrupted()) {
                logger.info("[{}]已经被打断",Thread.currentThread().getName());
                return null;
            }
            //抢锁，准备取出一个任务
            logger.info("[{}]开始获取任务",Thread.currentThread().getName());
            lock.lock();
            try {
                //如果队列为空，那就阻塞，这里需要是while而不是if，因为下面调用的是signalAll，而且也可以防止意外唤醒
                while (checkIsEmpty()){
                    logger.info("队列为空，等待其他线程放入任务后，才能取出任务,[{}]",Thread.currentThread().getName());
                    try {
                        emptyCondition.await();
                    } catch (InterruptedException e) {
                        logger.info("[{}]已经被打断",Thread.currentThread().getName());
                        Thread.currentThread().interrupt();
                        return null;
                    }
                }
                logger.info("[{}]的打断状态是[{}]",Thread.currentThread().getName(),Thread.currentThread().isInterrupted());
                if (Thread.currentThread().isInterrupted()) {
                    logger.info("[{}]已经被打断",Thread.currentThread().getName());
                    return null;
                }
                logger.info("取出了一个任务,[{}]",Thread.currentThread().getName());
                Task task = doPop();
                //取出一个任务后，列表肯定不是full的状态了
                fullCondition.signalAll();
                return task;
            }finally {
                lock.unlock();
            }
        }

        /**
         * 从列表中取出一个任务
         * @return
         */
        public Task doPop(){
            Task pop = taskDeque[headIndex];
            taskDeque[headIndex] = null;
            headIndex++;
            headIndex = headIndex%maxSize;
            return pop;
        }

        /**
         * 判断数组是否已经满了
         * 1、head...tail
         * 2、...tail,head...
         * @return
         */
        private boolean checkIsFull(){
            int i = tailIndex - headIndex;
            return i == maxSize-1 || i == -1;
        }

        /**
         * 判断数组是否为空的
         * @return
         */
        private boolean checkIsEmpty(){
            return tailIndex==headIndex;
        }

        /**
         * 尝试放入一个任务
         * @param task
         * @return
         */
        public boolean tryPush(Task task){
            //抢锁，尝试放入
            logger.info("[{}]开始尝试放入任务[{}]",Thread.currentThread().getName(),task.getTaskNum());
            lock.lock();
            try {
                //如果已经满了，那就直接返回，aqs风格
                if (checkIsFull()){
                    return false;
                }
                //队列中有空闲位置的话，那就放入任务
                logger.info("任务[{}]成功进入队列,[{}]",task.getTaskNum(),Thread.currentThread().getName());
                doAdd(task);
                //放入之后，列表一定不为空了
                emptyCondition.signalAll();
                return true;
            }finally {
                lock.unlock();
            }
        }

        /**
         * 放入一个任务
         * @param task
         */
        public void doAdd(Task task){
            taskDeque[tailIndex]=task;
            tailIndex++;
            tailIndex=tailIndex%maxSize;
        }

        /**
         * 向队列中添加一个任务，如果队列到达最大长度，那就阻塞
         * @param task
         */
        public void push(Task task){
            logger.info("[{}]开始放入任务[{}]",Thread.currentThread().getName(),task.getTaskNum());
            lock.lock();
            try {
                while (checkIsFull()){
                    logger.info("队列已满，等待任务被取出后，才能继续,[{}]",Thread.currentThread().getName());
                    try {
                        fullCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                logger.info("任务[{}]成功进入队列,[{}]",task.getTaskNum(),Thread.currentThread().getName());
                doAdd(task);
                emptyCondition.signalAll();
            }finally {
                lock.unlock();
            }
        }
    }

    /**
     * 任务处理器，其实就是线程
     */
    private static class TaskHandler extends Thread{
        //当前要处理的任务
        private Task task;
        //待处理任务的队列
        private final TaskQueue taskQueue;

        /**
         * 任务处理器
         * @param task 初始时的任务
         * @param taskQueue 待处理任务的队列
         * @param name 任务处理器名称（线程名）
         */
        public TaskHandler(Task task, TaskQueue taskQueue, String name){
            this.task = task;
            this.taskQueue = taskQueue;
            setName(name);
        }

        @Override
        public void run() {
            //如果有当前任务或者队列中可以获取到任务
            while (task!=null || (task=taskQueue.take())!=null){
                logger.info("[{}]开始执行任务[{}]",Thread.currentThread().getName(),task.getTaskNum());
                task.run();
                if (Thread.currentThread().isInterrupted()) {
                    logger.info("[{}]已经被打断",Thread.currentThread().getName());
                    return;
                }
                //将当前任务置为null
                task = null;
            }
            logger.info("[{}]no task",Thread.currentThread().getName());
        }
    }

    /**
     * 任务，基于Runnable
     */
    private static abstract class Task implements Runnable{

        //任务编号
        private final int taskNum;

        /**
         * 任务
         * @param taskNum 任务编号
         */
        private Task(int taskNum) {
            this.taskNum = taskNum;
        }

        public int getTaskNum() {
            return taskNum;
        }
    }

    /**
     * 拒绝策略
     */
    private interface RejectStrategy{

        /**
         * 当队列已满时会触发的具体逻辑
         * @param taskQueue
         * @param task
         */
        void handle(TaskQueue taskQueue,Task task);

    }

}
