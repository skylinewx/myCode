package com.example.spring.beans;

import com.example.spring.beans2.TestObj1;
import com.example.spring.beans2.TestObj2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConstructorInitTest3 {

    @Autowired(required = false)
    public ConstructorInitTest3(TestObj1 testObj1) {
        System.out.println("ConstructorInitTest3");
        System.out.println(testObj1);
    }

    @Autowired(required = false)
    public ConstructorInitTest3(TestObj1 testObj1, TestObj2 testObj2) {
        System.out.println("ConstructorInitTest3");
        System.out.println(testObj1);
        System.out.println(testObj2);
    }
}
