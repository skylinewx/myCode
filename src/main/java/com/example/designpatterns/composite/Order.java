package com.example.designpatterns.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 *
 * @author skyline
 */
public class Order implements CalculateAble {

    private final List<CalculateAble> calculateAbles;
    private final String name;

    public Order(String name) {
        this.calculateAbles = new ArrayList<>();
        this.name = name;
    }

    @Override
    public double calculate() {
        double sum = 0;
        for (CalculateAble calculateAble : calculateAbles) {
            sum += calculateAble.calculate();
        }
        return sum;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void add(CalculateAble calculateAble) {
        calculateAbles.add(calculateAble);
    }

    @Override
    public List<CalculateAble> getCalculateAbleList() {
        return calculateAbles;
    }
}
