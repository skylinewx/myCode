package com.example.spring;

import com.example.spring.beans3.MySkyComponent1;
import com.example.spring.beans3.MySkyComponent2;
import com.example.spring.beans3.MySkyComponent3;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

}
