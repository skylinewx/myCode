package com.example.spring.beans;

import com.example.spring.beans2.TestObj1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConstructorInitTest1 {

    @Autowired
    public ConstructorInitTest1(TestObj1 testObj1) {
        System.out.println(testObj1);
    }
}
