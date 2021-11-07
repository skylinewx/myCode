package com.example.designpatterns.singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

/**
 * 单例模式4，枚举型
 */
public enum Singleton4 {

    /**
     * i'm Singleton
     */
    INTANCE;

    private static final Logger logger = LoggerFactory.getLogger(Singleton4.class);

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
                Singleton4 instance = Singleton4.INTANCE;
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
