package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
class DemoApplicationTests2 {

	/**
	 * 3个线程依次打印 abc abc 打印4次
	 * @throws InterruptedException
	 */
	@Test
	void Test1() throws InterruptedException {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("ready");
		Printer printer = new Printer();
		CountDownLatch countDownLatch = new CountDownLatch(1);
		Thread t1 = new Thread(() -> {
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			printer.print("a", 1, 2);
		});
		Thread t2 = new Thread(() -> {
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			printer.print("b", 2, 3);
		});
		Thread t3 = new Thread(() -> {
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			printer.print("c", 3, 1);
		});
		t1.start();
		t2.start();
		t3.start();
		countDownLatch.countDown();
	}

	static class Printer{
		private int current = 1;
		private final Object lockObj = new Object();
		Logger logger = LoggerFactory.getLogger(this.getClass());
		public void print(String str,int signal,int nextSignal){
			for (int i = 0; i < 4; i++) {
				synchronized (lockObj){
					while (current!=signal){
						try {
							lockObj.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					logger.info(str);
					current = nextSignal;
					lockObj.notifyAll();
				}
			}
		}
	}

}
