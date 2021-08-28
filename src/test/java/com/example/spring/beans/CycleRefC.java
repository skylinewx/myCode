package com.example.spring.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 循环依赖bean
 */
@Component
public class CycleRefC {

    @Autowired
    private CycleRefD cycleRefD;

    public void hello() {
        System.out.println("C:hello:" + cycleRefD);
    }
}
