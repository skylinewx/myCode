package com.example.cache2.sender;

import com.example.cache2.domain.CacheSyncMessage;

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
