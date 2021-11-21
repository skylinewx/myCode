package com.example.juc.cache;

import java.util.function.Function;

/**
 * 缓存容器
 *
 * @param <T>
 * @author wangxing
 */
public interface ICacheContainer<T> {

    /**
     * 获取对象
     *
     * @param key
     * @return
     */
    T get(String key, Function<String, T> mappingFunction);

    /**
     * 移除对象
     *
     * @param key
     * @return
     */
    T remove(String key);
}
