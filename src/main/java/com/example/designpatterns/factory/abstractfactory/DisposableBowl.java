package com.example.designpatterns.factory.abstractfactory;

/**
 * 一次性碗
 * @author wangxing
 */
public class DisposableBowl implements Bowl{
    @Override
    public void use() {
        System.out.println("这是一次性碗");
    }

    @Override
    public void recycle() {
        System.out.println("用完就扔掉吧");
    }
}
