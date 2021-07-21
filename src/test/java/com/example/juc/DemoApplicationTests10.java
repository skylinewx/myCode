package com.example.juc;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

@SpringBootTest
class DemoApplicationTests10 {

    private static final Logger logger = LoggerFactory.getLogger(DemoApplicationTests10.class);

    @Test
    public void test() {
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(
                        1,
                        2,
                        60,
                        TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(10),
                        new ThreadFactory() {
                            @Override
                            public Thread newThread(Runnable r) {
                                return null;
                            }
                        },new RejectedExecutionHandler(){

                            @Override
                            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

                            }
                        });

    }

}
