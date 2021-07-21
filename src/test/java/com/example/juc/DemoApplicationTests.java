package com.example.juc;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void Test1() throws InterruptedException {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		Object lock = new Object();
		CountDownLatch countDownLatch = new CountDownLatch(1);
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread(() -> {
				try {
					countDownLatch.await();
					synchronized (lock){
						logger.info("{} 获取到了锁",Thread.currentThread().getName());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			thread.start();
			// 让出CPU时间
			Thread.sleep(200);
		}
		logger.info("预备....");
		countDownLatch.countDown();
		logger.info("开始....");
	}

	@Test
	void Test2() throws InterruptedException {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		Object lock = new Object();
		List<Thread> threads = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread(() -> {
				synchronized (lock){
					logger.info("{} 获取到了锁",Thread.currentThread().getName());
				}
			});
			threads.add(thread);
		}
		synchronized (lock){
			logger.info("预备....");
			logger.info("开始....");
			for (Thread thread : threads) {
				thread.start();
				// 让出CPU
				Thread.sleep(200);
			}
		}
	}

}
