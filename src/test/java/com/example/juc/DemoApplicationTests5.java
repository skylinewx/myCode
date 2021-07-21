package com.example.juc;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootTest
class DemoApplicationTests5 {

	/**
     * ReentrantLock 加锁解锁学习
	 * 在test中，当主线程走完之后，会停止线程池，所以子线程会停止，用join可以防止
	 */
	@Test
	void Test1() {
		ReentrantLock lock = new ReentrantLock();
		Logger logger = LoggerFactory.getLogger(this.getClass());
		Thread t1 = new Thread(() -> {
			logger.info("线程[{}]在[{}]开始尝试获取锁", Thread.currentThread().getName(), LocalDateTime.now());
			lock.lock();
			try {
				logger.info("线程[{}]在[{}]获取到锁了", Thread.currentThread().getName(), LocalDateTime.now());
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} finally {
				lock.unlock();
				logger.info("线程[{}]在[{}]释放锁了", Thread.currentThread().getName(), LocalDateTime.now());
			}
		}, "t1");
		t1.start();
		lock.lock();
		logger.info("线程[{}]在[{}]获取到锁了",Thread.currentThread().getName(), LocalDateTime.now());
		try {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}finally {
			lock.unlock();
			logger.info("线程[{}]在[{}]释放锁了",Thread.currentThread().getName(), LocalDateTime.now());
		}
		try {
			t1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试一下LockSupport.park可以被打断吗
	 */
	@Test
	void Test2() {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		Thread t1 = new Thread(() -> {
			LockSupport.park(this);
			logger.info("[{}]被唤醒了，打断状态是[{}]",Thread.currentThread().getName(),Thread.currentThread().isInterrupted());
		}, "t1");
		t1.start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		t1.interrupt();
		LockSupport.unpark(t1);
		try {
			t1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
