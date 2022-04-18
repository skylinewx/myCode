package com.example.cache.sender;

import com.example.cache.domain.CacheSyncMessage;

/**
 * 消息发送接口
 */
public interface IMessageSender {

    /**
     * 发送消息
     * @param message
     */
    void sendMessage(CacheSyncMessage cacheSyncMessage);
}
