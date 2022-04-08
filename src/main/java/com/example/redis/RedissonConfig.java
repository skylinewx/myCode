package com.example.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author skyline
 */
@Configuration
public class RedissonConfig {

    @Bean
    RedissonClient getRedisson(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379/0").setPassword("redis").setDatabase(0);
        return Redisson.create(config);
    }
}
