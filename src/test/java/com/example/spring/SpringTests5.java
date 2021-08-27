package com.example.spring;

import com.example.spring.beans.CycleRefA;
import com.example.spring.beans.CycleRefB;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * bean的循环依赖
 */
public class SpringTests5 {

    /**
     *  "@Autowrited" 循环依赖
     */
    @Test
    public void test1(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        CycleRefA cycleRefA = applicationContext.getBean(CycleRefA.class);
        cycleRefA.hello();
        CycleRefB cycleRefB = applicationContext.getBean(CycleRefB.class);
        cycleRefB.hello();
    }

}
