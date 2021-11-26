package com.example.designpatterns.factory.abstractfactory;

/**
 * 一次性的盘子
 *
 * @author wangxing
 */
public class DisposablePlate implements Plate {
    @Override
    public void use() {
        System.out.println("这是一次性盘子");
    }

    @Override
    public void recycle() {
        System.out.println("用完扔掉吧");
    }
}
