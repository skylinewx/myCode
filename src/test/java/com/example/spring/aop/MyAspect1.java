package com.example.spring.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class MyAspect1 {

    /**
     * 增强CycleRefC.hello，主要是为了让aop帮忙生成CycleRefC的代理
     */
    @Before("execution(* com.example.spring.beans.CycleRefC.hello(..))")
    public void beforeCHello() {
        System.out.println("CycleRefC要开始sayHello了");
    }

    /**
     * 增强CycleRefD.hello，主要是为了让aop帮忙生成CycleRefD的代理
     */
    @Before("execution(* com.example.spring.beans.CycleRefD.hello(..))")
    public void beforeDHello() {
        System.out.println("CycleRefD要开始sayHello了");
    }
}
