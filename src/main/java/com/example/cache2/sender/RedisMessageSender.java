package com.example.cache2.sender;

import com.example.cache2.domain.CacheSyncMessage;
import com.example.cache2.listener.redis.RedisListenerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisMessageSender implements IMessageSender {

    private static final Logger logger = LoggerFactory.getLogger(RedisMessageSender.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void sendMessage(CacheSyncMessage cacheSyncMessage) {
        String channel = RedisListenerConfig.REDIS_CHANNEL_PREFIX + cacheSyncMessage.getCacheGroup();
        logger.info("send [{}] to channel [{}]", cacheSyncMessage,channel);
        stringRedisTemplate.convertAndSend(channel, cacheSyncMessage.toString());
    }
}
