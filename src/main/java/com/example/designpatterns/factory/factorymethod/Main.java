package com.example.designpatterns.factory.factorymethod;

public class Main {
    public static void main(String[] args) {
        String[] users = {"张三", "李四", "王五", "赵柳", "二麻子"};
        ChopsticksFactory chopsticksFactory = new WoodChopsticksFactory();
        for (String user : users) {
            Chopsticks chopsticks = chopsticksFactory.create(user);
            chopsticks.use();
            chopsticks.recycle();
        }
    }
}
