package com.example.spring.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 循环依赖bean
 */
@Component("cycleRefE")
public class CycleRefE {

    @Autowired
    private CycleRefF cycleRefF;

    public void hello() {
        System.out.println("E:hello:" + cycleRefF);
    }
}
