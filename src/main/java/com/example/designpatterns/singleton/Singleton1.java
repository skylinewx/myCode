package com.example.designpatterns.singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

/**
 * 单例模式1，超简单，饿汉
 */
public class Singleton1 {
    private static final Logger logger = LoggerFactory.getLogger(Singleton1.class);
    private static final Singleton1 INTANCE = new Singleton1();

    private Singleton1() {
    }

    public static Singleton1 getInstance() {
        return INTANCE;
    }

    public static void main(String[] args) {
        LocalDateTime begin = LocalDateTime.now();
        int count = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CountDownLatch lock = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            Thread thread = new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Singleton1 instance = Singleton1.getInstance();
                logger.info("{}", instance.hashCode());
                lock.countDown();
            }, "" + i);
            thread.start();
        }
        countDownLatch.countDown();
        try {
            lock.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("全部单例获取完毕，耗时:[{}]", Duration.between(begin, LocalDateTime.now()));
    }
}
