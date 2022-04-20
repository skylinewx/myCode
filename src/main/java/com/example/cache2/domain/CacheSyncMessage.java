package com.example.cache2.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 缓存同步消息
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CacheSyncMessage {
    private String cacheGroup;
    private String keyStr;
    @Autowired
    private ObjectMapper objectMapper;
    private Class<?> keyClass;

    public void setCacheGroup(String cacheGroup) {
        this.cacheGroup = cacheGroup;
    }

    public void setKeyStr(String keyStr) {
        this.keyStr = keyStr;
    }

    public String getCacheGroup() {
        return cacheGroup;
    }

    public String getKeyStr() {
        return keyStr;
    }

    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setKeyClass(Class<?> keyClass) {
        this.keyClass = keyClass;
    }

    public Class<?> getKeyClass() {
        return keyClass;
    }
}
