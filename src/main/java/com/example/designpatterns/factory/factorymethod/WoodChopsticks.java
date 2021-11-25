package com.example.designpatterns.factory.factorymethod;

/**
 * 木头筷子
 *
 * @author wangxing
 */
public class WoodChopsticks implements Chopsticks {
    @Override
    public void use() {
        System.out.println("从饭盒里拿出来，夹菜");
    }

    @Override
    public void recycle() {
        System.out.println("洗干净，放回到饭盒里");
    }
}
