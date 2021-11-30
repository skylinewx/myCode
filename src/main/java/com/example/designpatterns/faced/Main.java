package com.example.designpatterns.faced;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example.designpatterns.faced")
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main.class);
        SchoolFacade schoolFacade = applicationContext.getBean(SchoolFacade.class);
        Student student = new Student();
        student.setName("张三");
        student.setMajor("数学系");
        schoolFacade.entrance(student);
    }
}
