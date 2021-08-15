package com.example.spring.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Bean1 {

    public void hello(){
        System.out.println("hello i'm bean1");
    }
}
