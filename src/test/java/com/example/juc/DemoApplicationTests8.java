package com.example.juc;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.*;

@SpringBootTest
class DemoApplicationTests8 {

	/**
	 * countDownLunch学习
	 */
	@Test
	void Test1() {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		//创建一个count=1的闭锁
		CountDownLatch countDownLatch = new CountDownLatch(1);
		List<Thread> threads = new ArrayList<>();
		//创建5个线程
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(() -> {
				logger.info("[{}]在等待发令枪", Thread.currentThread().getName());
				try {
					//等待闭锁的count=0
					countDownLatch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				logger.info("枪响了,[{}]跑!", Thread.currentThread().getName());
			}, "t" + (i + 1));
			thread.start();
			threads.add(thread);
			try {
				//让出CPU
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("开枪，开跑！");
		//将count--
		countDownLatch.countDown();
		try {
			//让出CPU
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//循环等待所有线程结束
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	void test2(){
		Logger logger = LoggerFactory.getLogger(this.getClass());
		CyclicBarrier cyclicBarrier = new CyclicBarrier(2, ()->{
			logger.info("cyclicBarrier被置为0了,{}",Thread.currentThread().getName());
		});
		logger.info("cyclicBarrier初始化为2,{}",Thread.currentThread().getName());
		List<Thread> threads = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			Thread thread = new Thread(() -> {
				logger.info("cyclicBarrier -1,[{}]",Thread.currentThread().getName());
				try {
					cyclicBarrier.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
				logger.info("cyclicBarrier 0了,[{}]",Thread.currentThread().getName());
			}, "t" + (i + 1));
			thread.start();
			threads.add(thread);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//循环等待所有线程结束
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	void test3(){
		Semaphore semaphore = new Semaphore(3);
		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("初始化了一个大小为3的信号量。");
		List<Thread> threads = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(() -> {
				logger.info("[{}]尝试申请一个资源",Thread.currentThread().getName());
				try {
					semaphore.acquire();
					logger.info("[{}]申请到了资源",Thread.currentThread().getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				logger.info("[{}]释放了一个资源",Thread.currentThread().getName());
				semaphore.release();
			}, "t" + (i + 1));
			thread.start();
			threads.add(thread);
			try {
				//让出CPU
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//循环等待所有线程结束
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	void test4(){
		Logger logger = LoggerFactory.getLogger(this.getClass());
		int count = 5;
		CountDownLatch countDownLatch = new CountDownLatch(count);
		String[] list = new String[count];
		Random random = new Random();
		for (int i = 0; i < count; i++) {
			int finalI = i;
			Thread thread = new Thread(() -> {
				for (int j = 0; j <= 100; j++) {
					try {
						TimeUnit.MILLISECONDS.sleep(random.nextInt(200));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					list[finalI]= Thread.currentThread().getName()+"("+j+"%)";
					System.out.print("\r"+ Arrays.toString(list));
				}
				countDownLatch.countDown();
			}, "t" + (i + 1));
			thread.start();
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("结束了");

	}

}
