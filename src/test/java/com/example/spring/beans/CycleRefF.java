package com.example.spring.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 循环依赖bean
 */
@Component("cycleRefF")
public class CycleRefF {

    @Autowired
    private CycleRefE cycleRefE;

    public void hello() {
        System.out.println("F:hello:" + cycleRefE);
    }
}
