package com.example.spring.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 循环依赖bean
 */
@Component
public class CycleRefB {

    @Autowired
    private CycleRefA cycleRefA;

    public void hello(){
        System.out.println("B:hello:"+cycleRefA);
    }
}
