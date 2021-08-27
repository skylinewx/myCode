package com.example.spring.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 循环依赖bean
 */
@Component
public class CycleRefA {

    @Autowired
    private CycleRefB cycleRefB;

    public void hello(){
        System.out.println("A:hello:"+cycleRefB);
    }
}
