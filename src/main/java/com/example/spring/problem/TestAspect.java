package com.example.spring.problem;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TestAspect {

    @Before("pointCut()")
    public void beforeTest(){
        System.out.println("before ...");
    }

    @Pointcut("execution(public * com..TestController.*(..))")
    public void pointCut() {
        //定义切点
    }
}
