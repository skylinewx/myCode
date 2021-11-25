package com.example.designpatterns.factory.factorymethod;

/**
 * 竹筷子工厂
 * @author wangxing
 */
public class BambooChopsticksFactory implements ChopsticksFactory{
    @Override
    public Chopsticks create(String user) {
        System.out.println(user+"来了，给他一双一次性筷子");
        return new BambooChopsticks();
    }
}
