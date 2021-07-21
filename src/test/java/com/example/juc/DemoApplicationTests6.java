package com.example.juc;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SpringBootTest
class DemoApplicationTests6 {

	/**
     * ReentrantReadWriteLock 读读并发，读写互斥
	 * 在test中，当主线程走完之后，会停止线程池，所以子线程会停止，用join可以防止
	 */
	@Test
	void Test1() {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
		ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
		ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
		writeLock.lock();
		logger.info("{}获取到了writeLock",Thread.currentThread().getName());

		Thread t1 = new Thread(() -> {
			logger.info("{}开始尝试获取readLock",Thread.currentThread().getName());
			readLock.lock();
			logger.info("{}获取到了readLock",Thread.currentThread().getName());
			try {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}finally {
				readLock.unlock();
				logger.info("{}释放了readLock",Thread.currentThread().getName());
			}
		}, "t1");
		t1.start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Thread t2 = new Thread(() -> {
			logger.info("{}开始尝试获取readLock",Thread.currentThread().getName());
			readLock.lock();
			logger.info("{}获取到了readLock",Thread.currentThread().getName());
			try {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}finally {
				readLock.unlock();
				logger.info("{}释放了readLock",Thread.currentThread().getName());
			}
		}, "t2");
		t2.start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Thread t3 = new Thread(() -> {
			logger.info("{}开始尝试获取writeLock",Thread.currentThread().getName());
			writeLock.lock();
			logger.info("{}获取到了writeLock",Thread.currentThread().getName());
			try {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}finally {
				writeLock.unlock();
				logger.info("{}释放了writeLock",Thread.currentThread().getName());
			}
		}, "t3");
		t3.start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Thread t4 = new Thread(() -> {
			logger.info("{}开始尝试获取readLock",Thread.currentThread().getName());
			readLock.lock();
			logger.info("{}获取到了readLock",Thread.currentThread().getName());
			try {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}finally {
				readLock.unlock();
				logger.info("{}释放了readLock",Thread.currentThread().getName());
			}
		}, "t4");
		t4.start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Thread t5 = new Thread(() -> {
			logger.info("{}开始尝试获取readLock",Thread.currentThread().getName());
			readLock.lock();
			logger.info("{}获取到了readLock",Thread.currentThread().getName());
			try {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}finally {
				readLock.unlock();
				logger.info("{}释放了readLock",Thread.currentThread().getName());
			}
		}, "t5");
		t5.start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//		readLock.unlock();
		writeLock.unlock();
		logger.info("{}释放了write",Thread.currentThread().getName());
		try {
			t1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			t3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			t4.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			t5.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}

}
