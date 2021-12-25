package com.example.designpatterns.composite;

/**
 * 苹果
 */
public class Apple implements CalculateAble {

    @Override
    public double calculate() {
        return 20;
    }

    @Override
    public String name() {
        return "apple";
    }
}
