package com.example.spring.customization;

import com.example.spring.beans.Student;
import org.springframework.beans.factory.annotation.Autowired;

public class MyCustomizationBean1 {

    /**
     * 将配置的student注入进来
     */
    @Autowired
    private Student student;

    public Integer getStudentAge(){
        return student.getAge();
    }
}
