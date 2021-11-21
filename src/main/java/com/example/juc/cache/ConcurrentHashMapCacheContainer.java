package com.example.juc.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 缓存容器
 *
 * @param <T>
 * @author wangxing
 */
public class ConcurrentHashMapCacheContainer<T> implements ICacheContainer<T> {

    private final ConcurrentHashMap<String, T> cacheMap = new ConcurrentHashMap<>();

    @Override
    public T get(String key, Function<String, T> mappingFunction) {
        return cacheMap.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public T remove(String key) {
        return cacheMap.remove(key);
    }
}
