package com.example.cache2.config;

import com.example.cache2.domain.GroupDO;
import com.example.cache2.domain.MyCache;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379/0").setPassword("redis").setDatabase(0);
        return Redisson.create(config);
    }

    @Bean
    public MyCache<String, GroupDO> myObjCache(){
        return new MyCache<>();
    }
}
