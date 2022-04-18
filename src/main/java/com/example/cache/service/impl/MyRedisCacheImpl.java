package com.example.cache.service.impl;

import com.example.cache.dao.MyDao;
import com.example.cache.domain.MyObj;
import com.example.cache.service.IAfterGet;
import com.example.cache.service.MyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 从redis中获取缓存数据的Service实现
 */
@Component
public class MyRedisCacheImpl implements MyService {

    @Autowired
    private MyDao myDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 空缓存内容
     */
    private static final String EMPTY_CACHE_STR = "{}";
    /**
     * 有效期5天
     */
    private static final int MYOBJ_CACHE_TIMEOUT = 60 * 60 * 24 * 5;
    private static final String MYOBJ_KEY_PREFIX = "myobj:id:";
    /**
     * 缓存重建锁前缀
     */
    private static final String LOCK_CACHE_REBUILD_MYOBJ_PREFIX = "lock:cache_rebuild:myobj:";
    /**
     * 数据库操作锁前缀
     */
    private static final String LOCK_DB_OPERATE_MYOBJ_PREFIX = "lock:db_operate:myobj:";

    @Override
    public MyObj getById(String id) {
        //这里不需要上读锁是因为取出来直接返回了
        //而且redis本身也是并发安全的
        MyObj myObj = getFromRedis(id);
        if (myObj != null) {
            return myObj;
        }
        //cacheRebuildLock的作用是保证在多节点情况下，只有一个节点可以执行缓存重建的操作
        RLock cacheRebuildLock = redissonClient.getLock(LOCK_CACHE_REBUILD_MYOBJ_PREFIX + id);
        cacheRebuildLock.lock();
        try {
            //二次校验
            myObj = getFromRedis(id);
            if (myObj != null) {
                return myObj;
            }
            //dbOperateLock才是保证读写一致性的
            RReadWriteLock dbOperateLock = redissonClient.getReadWriteLock(LOCK_DB_OPERATE_MYOBJ_PREFIX + id);
            dbOperateLock.writeLock().lock();
            try {
                myObj = myDao.getById(id);
                writeData2Redis(id, myObj);
            } finally {
                dbOperateLock.writeLock().unlock();
            }
        } finally {
            cacheRebuildLock.unlock();
        }
        return myObj;
    }

    private void writeData2Redis(String id, MyObj myObj) {
        if (myObj == null) {
            stringRedisTemplate.opsForValue().set(MYOBJ_KEY_PREFIX + id, EMPTY_CACHE_STR, getNullDataCacheTimeOut(), TimeUnit.SECONDS);
        } else {
            try {
                String myObjStr = objectMapper.writeValueAsString(myObj);
                stringRedisTemplate.opsForValue().set(MYOBJ_KEY_PREFIX + id, myObjStr, getMyObjCacheTimeout(), TimeUnit.SECONDS);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 无效数据24小时内失效
     *
     * @return
     */
    public int getNullDataCacheTimeOut() {
        return ThreadLocalRandom.current().nextInt(24) * 60 * 60;
    }

    /**
     * 获得缓存的失效时间
     *
     * @return 标准失效时间+随机5以内天数
     */
    public int getMyObjCacheTimeout() {
        return MYOBJ_CACHE_TIMEOUT + ThreadLocalRandom.current().nextInt(5) * 60 * 60 * 24;
    }

    /**
     * 从redis中读取数据同时为缓存更新续命时间
     *
     * @param id
     * @return
     */
    private MyObj getFromRedis(String id) {
        String redisKey = MYOBJ_KEY_PREFIX + id;
        String myObjStr = stringRedisTemplate.opsForValue().get(redisKey);
        if (!StringUtils.hasText(myObjStr)) {
            return null;
        }
        if (EMPTY_CACHE_STR.equals(myObjStr)) {
            //无效数据续命
            stringRedisTemplate.expire(redisKey, getNullDataCacheTimeOut(), TimeUnit.SECONDS);
            MyObj myObj = new MyObj();
            myObj.setId(id);
            return myObj;
        }
        try {
            //有效数据续命
            stringRedisTemplate.expire(redisKey, getMyObjCacheTimeout(), TimeUnit.SECONDS);
            return objectMapper.readValue(myObjStr, MyObj.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getById(String id, IAfterGet afterGet) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(LOCK_DB_OPERATE_MYOBJ_PREFIX + id);
        //先上读锁，因为要在afterGet中使用数据，所以需要进行保护
        RLock readLock = readWriteLock.readLock();
        readLock.lock();
        try {
            MyObj myObj = getFromRedis(id);
            if (myObj == null) {
                readLock.unlock();
                //缓存中没有说明需要重建
                RLock cacheRebuildLock = redissonClient.getLock(LOCK_CACHE_REBUILD_MYOBJ_PREFIX + id);
                cacheRebuildLock.lock();
                try {
                    //缓存重建中的二次校验
                    readLock.lock();
                    myObj = getFromRedis(id);
                    if (myObj == null) {
                        readLock.unlock();
                        //dbOperateLock才是保证读写一致性问题的
                        RReadWriteLock dbOperateLock = redissonClient.getReadWriteLock(LOCK_DB_OPERATE_MYOBJ_PREFIX + id);
                        dbOperateLock.writeLock().lock();
                        try {
                            //此时加锁成功应该说明没有其他的服务或者线程可以操作数据库了
                            myObj = myDao.getById(id);
                            writeData2Redis(id, myObj);
                            readLock.lock();
                        } finally {
                            dbOperateLock.writeLock().unlock();
                        }
                    }
                }finally {
                    cacheRebuildLock.unlock();
                }
            }
            //在ReadLock的包裹下执行业务逻辑
            afterGet.afterGet(myObj);
        }finally {
            readLock.unlock();
        }
    }

    @Override
    public void save(MyObj myObj) {
        RReadWriteLock dbOperateLock = redissonClient.getReadWriteLock(LOCK_DB_OPERATE_MYOBJ_PREFIX + myObj.getId());
        dbOperateLock.writeLock().lock();
        try {
            myDao.save(myObj);
            stringRedisTemplate.delete(MYOBJ_KEY_PREFIX + myObj.getId());
        } finally {
            dbOperateLock.writeLock().unlock();
        }
    }

    @Override
    public void delete(String id) {
        RReadWriteLock dbOperateLock = redissonClient.getReadWriteLock(LOCK_DB_OPERATE_MYOBJ_PREFIX + id);
        dbOperateLock.writeLock().lock();
        try {
            myDao.delete(id);
            stringRedisTemplate.delete(MYOBJ_KEY_PREFIX + id);
        } finally {
            dbOperateLock.writeLock().unlock();
        }
    }
}
