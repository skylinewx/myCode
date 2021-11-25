package com.example.designpatterns.factory.factorymethod;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 木筷子工厂
 *
 * @author wangxing
 */
public class WoodChopsticksFactory implements ChopsticksFactory {
    private static final ConcurrentHashMap<String, Chopsticks> cache = new ConcurrentHashMap<>();

    @Override
    public Chopsticks create(String user) {
        System.out.println(user + "来了，把属于他的那双筷子给他了");
        return cache.computeIfAbsent(user, (k) -> new WoodChopsticks());
    }
}
