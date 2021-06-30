package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class DemoApplicationTests4 {

	/**
     * GuardedObject
	 * 线程t1处理一个事物，主线程等待获取事物的处理结果
	 * @throws InterruptedException
	 */
	@Test
	void Test1() throws InterruptedException {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		GuardedObject guardedObject = new GuardedObject();
		Thread t1 = new Thread(()->{
			logger.info("开始处理业务逻辑");
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info("业务逻辑处理完毕");
			guardedObject.setResult(new Object());
		});
		t1.start();
		logger.info("main线程开始获取返回值");
		Object result = guardedObject.getResult(TimeUnit.SECONDS.toMillis(0));
		logger.info("main线程获取到了返回值{}",result);
	}

	/**
	 * 守护对象
	 */
	static class GuardedObject{
		private Object result;
		private final Object lock = new Object();

		public Object getResult(long timeOut) {
			long currentTimeMillis = System.currentTimeMillis();
			long remainingTime = timeOut;
			synchronized (lock){
				while (result==null){
					try {
						lock.wait(remainingTime);
						if (timeOut!=0) {
							remainingTime = timeOut-(System.currentTimeMillis()-currentTimeMillis);
							if (remainingTime<=0) {
								break;
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			return result;
		}

		public void setResult(Object result) {
			synchronized (lock){
				this.result = result;
				lock.notifyAll();
			}
		}
	}

}
