package com.example.designpatterns.composite;

/**
 * 茶叶
 */
public class Tea implements CalculateAble {
    @Override
    public double calculate() {
        return 99;
    }

    @Override
    public String name() {
        return "tea";
    }
}
