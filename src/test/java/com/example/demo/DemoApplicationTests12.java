package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class DemoApplicationTests12 {

    private static final Logger logger = LoggerFactory.getLogger(DemoApplicationTests12.class);
    volatile int a = 0, b = 0;
    int x = 0, y = 0;

    /**
     * 指令重排
     */
    @Test
    public void test() throws InterruptedException {
        int count = 0;
        while (true) {
            a = 0;
            b = 0;
            x = 0;
            y = 0;
            Thread t1 = new Thread(() -> {
                a = 1;
                x = b;
            }, "t1");

            Thread t2 = new Thread(() -> {
                b = 1;
                y = a;
            }, "t2");

            t1.start();
            t2.start();

            t1.join();
            t2.join();

            logger.info("count=[{}],x=[{}],y=[{}]", count++, x, y);

            if (x == 0 && y == 0) {
                break;
            }
        }
    }

    private static Object INSTANCE;
    /**
     * 单例测试
     */
    @Test
    public Object getInstance(){
        if (INSTANCE == null) {
            synchronized (DemoApplicationTests12.class){
                if (INSTANCE == null) {
                    INSTANCE = new Object();
                }
            }
        }
        return INSTANCE;
    }

    private static volatile boolean FLAG = false;
    @Test
    public void test2() throws InterruptedException {
        Thread t1 = new Thread(()->{
            while (!FLAG){
               System.out.println(FLAG);
            }
            System.out.println(FLAG);
        },"t1");
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        FLAG = true;
        t1.join();
    }


}
