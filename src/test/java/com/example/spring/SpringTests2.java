package com.example.spring;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 学习事件处理机制
 */
public class SpringTests2 {

    /**
     * 通过classPath读取xml配置文件获取bean
     */
    @Test
    public void test1(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");

    }
}
