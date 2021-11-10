package com.example.juc.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发事物测试，<br/>
 * 在并发环境下从A账户转账给B账户随机金额，A、B两账户初始都是500块，并发结束后，总金额应该保持1000块不变
 *
 * @author wangxing
 */
@Configuration
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = "com.example.juc.transaction")
public class MainApp {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MainApp.class, args);
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainApp.class);
        AccountChangeService accountChangeService = applicationContext.getBean(AccountChangeService.class);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 120, TimeUnit.SECONDS, new SynchronousQueue<>(),
                new ThreadFactory() {
                    private final AtomicInteger integer = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "accountChangeTest-" + integer.getAndIncrement());
                    }
                }, new RejectedExecutionHandler() {

            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                try {
                    executor.getQueue().put(r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        int max = 100;
        for (int i = 0; i < max; i++) {
            threadPoolExecutor.submit(accountChangeService::test);
        }
        logger.info("finish");
    }

}
