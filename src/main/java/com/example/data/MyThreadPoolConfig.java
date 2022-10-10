package com.example.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangxing
 * @date 2022/9/16
 **/
@Configuration
public class MyThreadPoolConfig {

    @Bean("MyThreadPool")
    public ThreadPoolExecutor myDataTransferThreadPool() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(6, 6, 120, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadFactory() {

            private final AtomicInteger atomicInteger = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "myDataTransferThreadPool-" + atomicInteger.getAndIncrement());
            }
        }, new RejectedExecutionHandler() {

            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                try {
                    executor.getQueue().put(r);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return threadPoolExecutor;
    }
}
