package com.example.cache2.listener.redis;

import com.example.cache2.domain.CacheSyncMessage;
import com.example.cache2.service.MyCacheService;
import com.example.cache2.service.MyListableCacheService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class RedisMessageListener implements MessageListener, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(RedisMessageListener.class);
    @Autowired
    private ObjectMapper objectMapper;
    private ApplicationContext applicationContext;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        String messageStr = new String(body, StandardCharsets.UTF_8);
        byte[] channel = message.getChannel();
        String channelStr = new String(channel, StandardCharsets.UTF_8);
        logger.info("getMessage [{}] from channel [{}]", messageStr, channelStr);
        CacheSyncMessage cacheSyncMessage;
        try {
            cacheSyncMessage = objectMapper.readValue(messageStr, CacheSyncMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String cacheGroup = cacheSyncMessage.getCacheGroup();
        String keyStr = cacheSyncMessage.getKeyStr();
        Object keyHolder;
        try {
            keyHolder = objectMapper.readValue(keyStr, cacheSyncMessage.getKeyClass());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        MyCacheService<?, ?> myCache = applicationContext.getBean(cacheGroup, MyCacheService.class);
        if (myCache instanceof MyListableCacheService) {
            ((MyListableCacheService<?, ?>) myCache).initCache();
        } else {
            myCache.getCache().remove(keyHolder);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
