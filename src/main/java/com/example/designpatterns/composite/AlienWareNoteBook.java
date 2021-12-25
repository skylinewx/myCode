package com.example.designpatterns.composite;

/**
 * 外星人笔记本
 */
public class AlienWareNoteBook implements CalculateAble {
    @Override
    public double calculate() {
        return 25000;
    }

    @Override
    public String name() {
        return "alienWareNoteBook";
    }
}
