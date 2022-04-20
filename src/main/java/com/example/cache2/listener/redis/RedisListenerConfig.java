package com.example.cache2.listener.redis;

import com.example.cache2.service.CacheGroup;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Map;

@Configuration
public class RedisListenerConfig implements ApplicationContextAware {

    public static final String REDIS_CHANNEL_PREFIX = "redis.cacheSync.";
    private ApplicationContext applicationContext;

    @Bean
    public RedisMessageListenerContainer getRedisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            RedisMessageListener redisMessageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        Map<String, CacheGroup> beansOfType = applicationContext.getBeansOfType(CacheGroup.class);
        for (Map.Entry<String, CacheGroup> entry : beansOfType.entrySet()) {
            container.addMessageListener(redisMessageListener, new ChannelTopic(REDIS_CHANNEL_PREFIX + entry.getValue().getCacheGroup()));
        }
        return container;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
