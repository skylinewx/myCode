package com.example.spring.beans;

import com.example.spring.beans2.TestObj1;
import com.example.spring.beans2.TestObj2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class ConstructorInitTest2Error {

    @Autowired
    public ConstructorInitTest2Error(TestObj1 testObj1){
        System.out.println(testObj1);
    }

    @Autowired
    public ConstructorInitTest2Error(TestObj2 testObj2){
        System.out.println(testObj2);
    }
}
