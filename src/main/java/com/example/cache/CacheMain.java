package com.example.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class CacheMain {
    public static void main(String[] args) {
        Cache<Object, Object> build = Caffeine.newBuilder().build();
        //invalidate时，先通过同步IO创建锁，然后本地事物提交，发送消息给各个服务，各个服务失效本地缓存，然后解锁
        //不可以直接通过同步IO使其他服务缓存失效，因为此时本地事物可能还没提交。
        MyCache<Object,Object> myCache = new MyCache<>(build);
//        myCache.begineModify();
    }
}
