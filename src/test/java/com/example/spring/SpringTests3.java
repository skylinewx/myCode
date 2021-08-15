package com.example.spring;

import com.example.spring.events.MyTestEvent;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * bean的实例化和注解收集
 */
public class SpringTests3 {

    /**
     * bean的实例化
     */
    @Test
    public void test1(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Object testObj1 = applicationContext.getBean("testObj1");
        System.out.println(testObj1);
        Object testObj2 = applicationContext.getBean("testObj2");
        System.out.println(testObj2);
    }
}
