package com.example.cache.service.impl;

import com.example.cache.dao.MyDao;
import com.example.cache.domain.MyObj;
import com.example.cache.service.IAfterGet;
import com.example.cache.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 本地缓存
 */
@Component
public class MyLocalCacheServiceImpl implements MyService {

    private final Map<String, MyObj> cacheMap = new HashMap<>();
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    @Autowired
    private MyDao myDao;

    /**
     * 一个简单的缓存读取的样例，使用读写锁兼顾效率与安全。
     * 这个方法一般用来配合Controller层读取缓存数据使用，
     * 对于后端使用缓存来说，因为缓存对应已经被return，
     * 导致失去了锁的保护，可能会导致不可重复读的情况出现。
     * 严谨处理的话，MyObj对象上应该存个版本属性，
     * 通过乐观锁来避免无锁状态下缓存中的MyObj被修改的情况
     * @param id
     * @return
     */
    @Override
    public MyObj getById(String id){
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        MyObj myObj;
        //要读缓存，先上读锁，hashmap线程不安全
        readLock.lock();
        try {
            myObj = cacheMap.get(id);
            if (myObj != null) {
                //缓存命中，在finally中释放读锁
                return myObj;
            }
        }finally {
            readLock.unlock();
        }
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        //大家竞争写锁
        writeLock.lock();
        try {
            //先来个经典的double check
            myObj = cacheMap.get(id);
            if (myObj != null) {
                return myObj;
            }
            myObj = myDao.getById(id);
            cacheMap.put(id, myObj);
            return myObj;
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * 经典的缓存读时写入操作，
     * 与ReentrantReadWriteLock中给出的CacheObject的写法是一样的，只是use变成了一个回调接口，AfterGet。
     * 可以保证缓存在使用期间线程安全，但是会产生回调地狱。
     * @param id
     * @param afterGet
     */
    @Override
    public void getById(String id, IAfterGet afterGet){
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        //读缓存，先上读锁
        readLock.lock();
        try {
            MyObj myObj = cacheMap.get(id);
            if (myObj == null) {
                //缓存未命中，释放读锁
                readLock.unlock();
                ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
                //开始写缓存，上写锁
                writeLock.lock();
                try {
                    //二次校验
                    myObj = cacheMap.get(id);
                    if (myObj == null) {
                        //如果缓存中确实没有数据，那就从数据库里读一下
                        myObj = myDao.getById(id);
                    }
                    //到这里，写操作完毕，为了保证此时的myObj对象线程安全，
                    //即缓存里当前id对应的对象与当前的myObj对象是同一个对象
                    //这里需要上读锁
                    readLock.lock();
                }finally {
                    writeLock.unlock();
                }
            }
            //缓存使用期线程安全，别的写操作需要等待当前的读操作完毕后，再执行
            afterGet.afterGet(myObj);
        }finally {
            readLock.unlock();
        }
    }

    /**
     *  缓存资源修改
     */
    @Override
    public void save(MyObj myObj){
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        //修改缓存需要上写锁，避免并发读写
        writeLock.lock();
        try {
            //先写数据库
            myDao.save(myObj);
            //缓存失效
            cacheMap.remove(myObj.getId());
        }finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(String id){
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            myDao.delete(id);
            cacheMap.remove(id);
        }finally {
            writeLock.unlock();
        }
    }
}

