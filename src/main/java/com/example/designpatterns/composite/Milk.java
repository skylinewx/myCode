package com.example.designpatterns.composite;

public class Milk implements CalculateAble {
    @Override
    public double calculate() {
        return 25;
    }

    @Override
    public String name() {
        return "Milk";
    }
}
