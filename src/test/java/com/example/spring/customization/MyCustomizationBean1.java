package com.example.spring.customization;

import com.example.spring.beans.Student;

public class MyCustomizationBean1 {

    private final Student student;

    public MyCustomizationBean1(Student student) {
        this.student = student;
    }

    public Integer getStudentAge(){
        return student.getAge();
    }
}
