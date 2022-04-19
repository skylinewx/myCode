package com.example.cache2.service;

import com.example.cache2.domain.MyCache;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class MyCacheService<TFace, KeyHolder> {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Autowired
    private MyCache<KeyHolder, TFace> myCache;

    public final TFace get(KeyHolder keyHolder) {
        TFace obj = myCache.get(keyHolder);
        if (obj != null) {
            return obj;
        }
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        //大家竞争写锁
        writeLock.lock();
        try {
            //先来个经典的double check
            obj = myCache.get(keyHolder);
            if (obj != null) {
                return obj;
            }
            obj = getFromDb(keyHolder);
            myCache.put(keyHolder, obj);
            return obj;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 从数据库中读取数据
     *
     * @param keyHolder
     * @return
     */
    protected abstract TFace getFromDb(KeyHolder keyHolder);

    /**
     * 执行保存逻辑
     *
     * @param obj
     */
    protected abstract void doSave(TFace obj);

    /**
     * 执行删除逻辑
     *
     * @param keyHolder
     */
    protected abstract void doDelete(KeyHolder keyHolder);

    /**
     * 根据对象返回对应的key
     *
     * @param obj
     * @return
     */
    protected abstract KeyHolder getKeyHolderByObj(TFace obj);

    public final void get(KeyHolder keyHolder, IAfterGet<TFace> afterGet) {
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        //读缓存，先上读锁
        readLock.lock();
        try {
            TFace obj = myCache.get(keyHolder);
            if (obj == null) {
                //缓存未命中，释放读锁
                readLock.unlock();
                ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
                //开始写缓存，上写锁
                writeLock.lock();
                try {
                    //二次校验
                    obj = myCache.get(keyHolder);
                    if (obj == null) {
                        //如果缓存中确实没有数据，那就从数据库里读一下
                        obj = getFromDb(keyHolder);
                    }
                    //到这里，写操作完毕，为了保证此时的myObj对象线程安全，
                    //即缓存里当前id对应的对象与当前的myObj对象是同一个对象
                    //这里需要上读锁
                    readLock.lock();
                } finally {
                    writeLock.unlock();
                }
            }
            //缓存使用期线程安全，别的写操作需要等待当前的读操作完毕后，再执行
            afterGet.afterGet(obj);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 缓存资源修改
     */
    public void save(TFace obj) {
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        //修改缓存需要上写锁，避免并发读写
        writeLock.lock();
        try {
            //先写数据库
            doSave(obj);
            //缓存失效
            myCache.remove(getKeyHolderByObj(obj));
        } finally {
            writeLock.unlock();
        }
    }

    public void delete(KeyHolder keyHolder) {
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            doDelete(keyHolder);
            myCache.remove(keyHolder);
        } finally {
            writeLock.unlock();
        }
    }
}
