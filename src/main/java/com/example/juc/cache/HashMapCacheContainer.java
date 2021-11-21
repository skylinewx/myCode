package com.example.juc.cache;

import java.util.HashMap;
import java.util.function.Function;

/**
 * 缓存容器
 *
 * @param <T>
 * @author wangxing
 */
public class HashMapCacheContainer<T> implements ICacheContainer<T> {

    private final HashMap<String, T> cacheMap = new HashMap<>();

    @Override
    public synchronized T get(String key, Function<String, T> mappingFunction) {
        return cacheMap.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public synchronized T remove(String key) {
        return cacheMap.remove(key);
    }
}
