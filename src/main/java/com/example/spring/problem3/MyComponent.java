package com.example.spring.problem3;

import org.springframework.stereotype.Component;

@Component
public class MyComponent {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
