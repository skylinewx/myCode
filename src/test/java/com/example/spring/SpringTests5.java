package com.example.spring;

import com.example.spring.beans.*;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * bean的循环依赖
 */
public class SpringTests5 {

    /**
     * "@Autowrited" 循环依赖
     */
    @Test
    public void test1() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        CycleRefA cycleRefA = applicationContext.getBean(CycleRefA.class);
        cycleRefA.hello();
        CycleRefB cycleRefB = applicationContext.getBean(CycleRefB.class);
        cycleRefB.hello();
    }

    @Test
    public void test2() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        CycleRefC cycleRefA = applicationContext.getBean(CycleRefC.class);
        cycleRefA.hello();
        CycleRefD cycleRefD = applicationContext.getBean(CycleRefD.class);
        cycleRefD.hello();
    }

    @Test
    public void test3() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        CycleRefE cycleRefE = applicationContext.getBean(CycleRefE.class);
        cycleRefE.hello();
        CycleRefF cycleRefF = applicationContext.getBean(CycleRefF.class);
        cycleRefF.hello();
    }

}
