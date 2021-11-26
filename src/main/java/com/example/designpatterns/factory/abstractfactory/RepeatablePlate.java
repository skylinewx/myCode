package com.example.designpatterns.factory.abstractfactory;

/**
 * 可反复使用的盘子
 *
 * @author wangxing
 */
public class RepeatablePlate implements Plate {
    @Override
    public void use() {
        System.out.println("这是一次性盘子");
    }

    @Override
    public void recycle() {
        System.out.println("用完扔掉吧");
    }
}
