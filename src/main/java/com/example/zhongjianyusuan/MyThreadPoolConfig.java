package com.example.zhongjianyusuan;

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

    @Bean("mySplitTableThreadPool")
    public ThreadPoolExecutor mySplitTableThreadPool() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {

            private final AtomicInteger atomicInteger = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "mySplitTableThread-" + atomicInteger.getAndIncrement());
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

    @Bean("myDataTransferThreadPool")
    public ThreadPoolExecutor myDataTransferThreadPool() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4, 120, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadFactory() {

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
