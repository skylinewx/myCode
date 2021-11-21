package com.example.juc.cache;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * 缓存容器
 *
 * @param <T>
 * @author wangxing
 */
public class ReadWriteLockCacheContainer<T> implements ICacheContainer<T> {

    private final HashMap<String, T> cacheMap = new HashMap<>();
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Override
    public T get(String key, Function<String, T> mappingFunction) {
        //读之前先上读锁
        readWriteLock.readLock().lock();
        try {
            boolean has = cacheMap.containsKey(key);
            if (!has) {
                //没有数据，需要写，读锁释放
                readWriteLock.readLock().unlock();
                //加写锁，必须先释放读锁，不然这里直接GG
                readWriteLock.writeLock().lock();
                try {
                    //并发二重校验
                    has = cacheMap.containsKey(key);
                    if (!has) {
                        T data = mappingFunction.apply(key);
                        cacheMap.put(key, data);
                    }
                } finally {
                    //这里要先降级，给自己占一个坑，防止当前线程释放写锁之后，
                    //马上有其他线程获取到写锁，并对对象修改
                    readWriteLock.readLock().lock();
                    //finally里面释放写锁
                    readWriteLock.writeLock().unlock();
                }
            }
            return cacheMap.get(key);
        } finally {
            //finally里面释放写锁
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public T remove(String key) {
        readWriteLock.writeLock().lock();
        try {
            return cacheMap.remove(key);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
