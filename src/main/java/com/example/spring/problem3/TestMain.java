package com.example.spring.problem3;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.example.spring.problem3");
        MyComponent myComponent = applicationContext.getBean(MyComponent.class);
        System.out.println("myComponent.getName() = " + myComponent.getName());
    }
}
