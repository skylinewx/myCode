package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

@SpringBootTest
class DemoApplicationTests11 {

    private static final Logger logger = LoggerFactory.getLogger(DemoApplicationTests11.class);

    /**
     * scheduleAtFixedRate
     */
    @Test
    public void test() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(()->{
            logger.info("==============");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
        //为了看效果
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * scheduleWithFixedDelay
     */
    @Test
    public void test2() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(()->{
            logger.info("==============");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
        //为了看效果
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * heartBeatTest
     */
    @Test
    public void test3(){
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(()->{
            doHeartTest(scheduledExecutorService);
        }, 2, TimeUnit.SECONDS);
        //为了看效果
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doHeartTest(ScheduledExecutorService scheduledExecutorService){
        logger.info("心跳测试==========");
        scheduledExecutorService.schedule(()->{
            doHeartTest(scheduledExecutorService);
        }, 2, TimeUnit.SECONDS);
    }

}
