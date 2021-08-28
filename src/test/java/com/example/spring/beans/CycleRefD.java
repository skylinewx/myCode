package com.example.spring.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 循环依赖bean
 */
@Component
public class CycleRefD {

    @Autowired
    private CycleRefC cycleRefC;

    public void hello() {
        System.out.println("D:hello:" + cycleRefC);
    }
}
