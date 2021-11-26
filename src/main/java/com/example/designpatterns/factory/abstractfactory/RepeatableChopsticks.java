package com.example.designpatterns.factory.abstractfactory;

/**
 * 可反复使用的筷子
 *
 * @author wangxing
 */
public class RepeatableChopsticks implements Chopsticks {
    @Override
    public void use() {
        System.out.println("这是可反复使用的筷子");
    }

    @Override
    public void recycle() {
        System.out.println("用完洗一洗");
    }
}
