package com.example.juc.lock1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wangxing
 * @date 2022/4/27
 **/
public class LockMain {

    private static final ConcurrentHashMap<String, Object> lockMap = new ConcurrentHashMap<>();
    private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static final Logger logger = LoggerFactory.getLogger(LockMain.class);

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";

        new Thread(() -> {
            logger.info("开始");
            readWriteLock.writeLock().lock();
            try {
                logger.info("持有lockMap，10秒后释放");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                logger.info("lockMap释放");
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }, "lockMap-holder").start();

        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        createThread(key1, "t1-1").start();
        createThread(key1, "t1-2").start();
        createThread(key1, "t1-3").start();
        createThread(key2, "t2").start();
        createThread(key3, "t3").start();
        createThread(key4, "t4").start();
        countDownLatch.countDown();
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Thread createThread(String key, String threadName) {
        return new Thread(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            syncTest(key);
        }, threadName);
    }

    private static void syncTest(String key) {
        logger.info("开始");
        readWriteLock.readLock().lock();
        try {
            Object o = lockMap.computeIfAbsent(key, k -> new Object());
            synchronized (o) {
                logger.info("抢到了{}锁，休眠5秒", key);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                logger.info("释放{}锁", key);
            }
        } finally {
            readWriteLock.readLock().unlock();
        }

    }
}
