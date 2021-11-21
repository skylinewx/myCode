package com.example.juc.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangxing
 */
@Configuration
public class CacheConfig {

    @Bean
    public ConcurrentHashMapCacheContainer<StudentDO> getConcurrentHashMapCacheContainer() {
        return new ConcurrentHashMapCacheContainer<>();
    }

    @Bean
    public HashMapCacheContainer<StudentDO> getHashMapCacheContainer() {
        return new HashMapCacheContainer<>();
    }

    @Bean
    public ReadWriteLockCacheContainer<StudentDO> getReadWriteLockCacheContainer() {
        return new ReadWriteLockCacheContainer<>();
    }

    @Bean
    public ReadWriteLockCacheContainer2<StudentDO> getReadWriteLockCacheContainer2() {
        return new ReadWriteLockCacheContainer2<>();
    }
}
