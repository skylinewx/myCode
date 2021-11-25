package com.example.designpatterns.factory.factorymethod;

/**
 * 竹筷子
 *
 * @author wangxing
 */
public class BambooChopsticks implements Chopsticks {
    @Override
    public void use() {
        System.out.println("从塑料袋里拿出来，掰开，夹菜");
    }

    @Override
    public void recycle() {
        System.out.println("扔掉");
    }
}
