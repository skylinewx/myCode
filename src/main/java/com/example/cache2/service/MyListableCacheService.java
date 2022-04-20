package com.example.cache2.service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class MyListableCacheService<TFace, KeyHolder> extends MyCacheService<TFace, KeyHolder> {

    private List<TFace> allDataList;

    public final List<TFace> list(Predicate<TFace> predicate) {
        ReentrantReadWriteLock.ReadLock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            return allDataList.stream().filter(predicate).collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    protected abstract List<TFace> listAll();

    @PostConstruct
    public final void initCache() {
        ReentrantReadWriteLock.WriteLock writeLock = getReadWriteLock().writeLock();
        writeLock.lock();
        try {
            allDataList = listAll();
            getCache().clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    protected TFace doGet(KeyHolder keyHolder) {
        List<TFace> list = list(item -> getKeyHolderByObj(item).equals(keyHolder));
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            throw new RuntimeException("expect 1 but find " + list.size());
        }
        return list.get(0);
    }
}
