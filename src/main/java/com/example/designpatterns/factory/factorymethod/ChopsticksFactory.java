package com.example.designpatterns.factory.factorymethod;

/**
 * 筷子工厂
 *
 * @author wangxing
 */
public interface ChopsticksFactory {

    /**
     * 生产筷子
     *
     * @param user 筷子的使用者
     * @return 筷子
     */
    Chopsticks create(String user);
}
