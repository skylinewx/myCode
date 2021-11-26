package com.example.designpatterns.factory.abstractfactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 可反复使用的餐具的工厂
 *
 * @author wangxing
 */
public class RepeatableTablewareFactory implements TablewareFactory {
    private final ConcurrentHashMap<String, Chopsticks> chopsticksCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Bowl> bowlCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Plate> plateCache = new ConcurrentHashMap<>();

    @Override
    public Chopsticks createChopsticks(String user) {
        return chopsticksCache.computeIfAbsent(user, k -> new RepeatableChopsticks());
    }

    @Override
    public Bowl createBowl(String user) {
        return bowlCache.computeIfAbsent(user, k -> new RepeatableBowl());
    }

    @Override
    public Plate createPlate(String user) {
        return plateCache.computeIfAbsent(user, k -> new RepeatablePlate());
    }
}
