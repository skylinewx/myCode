package com.example.spring.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Bean2 {

    @Autowired
    private Bean1 bean1;

    public void hello(){
        System.out.println("hello i'm bean2 ");
        bean1.hello();
    }
}
