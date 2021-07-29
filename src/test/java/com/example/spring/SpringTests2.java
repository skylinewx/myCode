package com.example.spring;

import com.example.spring.events.MyTestEvent;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 学习事件处理机制
 */
public class SpringTests2 {

    /**
     * 学习事件处理机制
     */
    @Test
    public void test1(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        applicationContext.publishEvent(new MyTestEvent("MyTestEvent"));
    }
}
