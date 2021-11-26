package com.example.designpatterns.factory.abstractfactory;

/**
 * 可反复使用的碗
 *
 * @author wangxing
 */
public class RepeatableBowl implements Bowl {
    @Override
    public void use() {
        System.out.println("这个碗可以反复使用");
    }

    @Override
    public void recycle() {
        System.out.println("用完洗一洗");
    }
}
