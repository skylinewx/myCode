package com.example.designpatterns.factory.abstractfactory;

/**
 * 一次性筷子
 * @author wangxing
 */
public class DisposableChopsticks implements Chopsticks{
    @Override
    public void use() {
        System.out.println("这是一次性筷子");
    }

    @Override
    public void recycle() {
        System.out.println("用完扔掉");
    }
}
