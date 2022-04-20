package com.example.cache2.service;

import com.example.cache2.domain.CacheGroup;
import com.example.cache2.domain.CacheSyncMessage;
import com.example.cache2.sender.IMessageSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class MyCacheService<TFace, KeyHolder> implements CacheGroup, BeanNameAware, ApplicationContextAware {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final ConcurrentHashMap<KeyHolder, TFace> cacheMap = new ConcurrentHashMap<>();

    @Autowired
    private IMessageSender messageSender;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionCommitHandler transactionCommitHandler;
    private ApplicationContext applicationContext;
    private String cacheGroup;

    /**
     * 从数据库中读取数据
     *
     * @param keyHolder
     * @return
     */
    protected abstract TFace doGet(KeyHolder keyHolder);

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

    public final TFace get(KeyHolder keyHolder) {
        TFace obj = cacheMap.get(keyHolder);
        if (obj != null) {
            return obj;
        }
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        //大家竞争写锁
        writeLock.lock();
        try {
            //先来个经典的double check
            obj = cacheMap.get(keyHolder);
            if (obj != null) {
                return obj;
            }
            obj = doGet(keyHolder);
            cacheMap.put(keyHolder, obj);
            return obj;
        } finally {
            writeLock.unlock();
        }
    }

    public final void get(KeyHolder keyHolder, IAfterGet<TFace> afterGet) {
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        //读缓存，先上读锁
        readLock.lock();
        try {
            TFace obj = cacheMap.get(keyHolder);
            if (obj == null) {
                //缓存未命中，释放读锁
                readLock.unlock();
                ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
                //开始写缓存，上写锁
                writeLock.lock();
                try {
                    //二次校验
                    obj = cacheMap.get(keyHolder);
                    if (obj == null) {
                        //如果缓存中确实没有数据，那就从数据库里读一下
                        obj = doGet(keyHolder);
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
    @Transactional(rollbackFor = Exception.class)
    public void save(TFace obj) {
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        //修改缓存需要上写锁，避免并发读写
        writeLock.lock();
        try {
            //先写数据库
            doSave(obj);
            KeyHolder keyHolder = getKeyHolderByObj(obj);
            //缓存失效
            cacheMap.remove(keyHolder);
            transactionCommitHandler.afterCommit(() -> {
                sendSyncMessage(keyHolder);
            });
        } finally {
            writeLock.unlock();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(KeyHolder keyHolder) {
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            doDelete(keyHolder);
            cacheMap.remove(keyHolder);
            transactionCommitHandler.afterCommit(() -> {
                sendSyncMessage(keyHolder);
            });
        } finally {
            writeLock.unlock();
        }
    }

    protected ReentrantReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    public ConcurrentHashMap<KeyHolder, TFace> getCache() {
        return cacheMap;
    }

    private CacheSyncMessage getCacheSyncMessage(KeyHolder key) {
        CacheSyncMessage cacheSyncMessage = applicationContext.getBean(CacheSyncMessage.class);
        cacheSyncMessage.setCacheGroup(getCacheGroup());
        try {
            cacheSyncMessage.setKeyStr(objectMapper.writeValueAsString(key));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        cacheSyncMessage.setKeyClass(key.getClass());
        return cacheSyncMessage;
    }

    private void sendSyncMessage(KeyHolder key) {
        CacheSyncMessage cacheSyncMessage = getCacheSyncMessage(key);
        messageSender.sendMessage(cacheSyncMessage);
    }

    @Override
    public String getCacheGroup() {
        return this.cacheGroup;
    }

    @Override
    public void setBeanName(String name) {
        this.cacheGroup = name;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
