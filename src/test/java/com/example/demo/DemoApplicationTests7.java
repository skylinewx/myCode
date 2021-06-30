package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SpringBootTest
class DemoApplicationTests7 {

	/**
	 */
	@Test
	void Test1() {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		Task task = createTask();
		List<String> valueList = task.getValueList();
		Map<String, String> map = task.getMap();
		TestParam testParam = new TestParam(map);
		for (String s : valueList) {
			map.put("key",s);
			printer(logger, testParam);
		}
	}

	private Task createTask(){
		Task task = new Task();
		Map<String,String> map = new HashMap<>();
		map.put("key","key");
		task.setMap(map);
		List<String> valueList = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			valueList.add("key"+i);
		}
		task.setValueList(valueList);
		return  task;
	}

	@Test
	void Test2() {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		int count=10000;
		for (int i = 0; i < 10; i++) {
			new Thread(()->{
				Map<String,String> map = new HashMap<>();
				map.put("key","key");
				TestParam testParam = new TestParam(map);
//				testParam.setMap(map);
				for (int j = 0; j < count; j++) {
					map.put("key","key"+j);
					printer(logger, testParam);
				}
			},"t"+i).start();
		}
	}

	private static class Task{
		Map<String,String> map;
		List<String> valueList;

		public List<String> getValueList() {
			return valueList;
		}

		public void setValueList(List<String> valueList) {
			this.valueList = valueList;
		}

		public Map<String, String> getMap() {
			return map;
		}

		public void setMap(Map<String, String> map) {
			this.map = map;
		}
	}

	private void printer(Logger logger,TestParam testParam){
		logger.info("线程:{},key的值是：{}",Thread.currentThread().getName(),testParam.getMap().get("key"));
	}

	private static class TestParam {
		private Map<String,String> map;

		public TestParam(Map<String, String> map) {
			this.map = map;
		}

		public Map<String, String> getMap() {
			return map;
		}

		public void setMap(Map<String, String> map) {
			this.map = map;
		}
	}

}
