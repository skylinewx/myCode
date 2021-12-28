package com.example.jmh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.example.jmh")
public class SpringBootApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootApp.class, args);
        IQueue arrayQueue = context.getBean("arrayQueue", IQueue.class);
        System.out.println(arrayQueue);
        IQueue linkedQueue = context.getBean("linkedQueue", IQueue.class);
        System.out.println(linkedQueue);
    }
}
