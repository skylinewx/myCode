package com.example.designpatterns.strategy;

public class Banana {
    private double weight;
    private int count;

    public Banana(double weight, int count) {
        this.weight = weight;
        this.count = count;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Banana{" +
                "weight=" + weight +
                ", count=" + count +
                '}';
    }
}
