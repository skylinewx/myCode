package com.example.spring;

import com.example.spring.beans3.MySkyComponent1;
import com.example.spring.beans3.MySkyComponent2;
import com.example.spring.beans3.MySkyComponent3;
import com.example.spring.utils.SimpleClassScan;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Set;

/**
 * bean的循环依赖
 */
public class SpringTests6 {

    /**
     * 自定义注解扫描
     */
    @Test
    public void test1() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        MySkyComponent1 component1 = applicationContext.getBean(MySkyComponent1.class);
        MySkyComponent2 component2 = applicationContext.getBean(MySkyComponent2.class);
        MySkyComponent3 component3 = applicationContext.getBean(MySkyComponent3.class);
        System.out.println("component1.getMySkyComponent2:" + component1.getMySkyComponent2());
        System.out.println("component2.getMySkyComponent3:" + component2.getMySkyComponent3());
        System.out.println("component3.getMySkyComponent1:" + component3.getMySkyComponent1());
    }

    @Test
    public void test2() {
        SimpleClassScan simpleClassScan = new SimpleClassScan();
        Set<Class<?>> scan = simpleClassScan.scan("com.example");
        for (Class<?> clazz : scan) {
            System.out.println(clazz);
        }
    }

    @Test
    public void test3() {
        SimpleClassScan simpleClassScan = new SimpleClassScan();
        Set<Class<?>> scan = simpleClassScan.scan("org.slf4j");
        for (Class<?> clazz : scan) {
            System.out.println(clazz);
        }
    }

}
