package com.example.designpatterns.observer;

/**
 * 事件中心
 *
 * @author skyline
 */
public interface IEventCenter {

    /**
     * 发布事件
     *
     * @param event
     */
    void publishEvent(BaseEvent event);

    /**
     * 注册事件
     *
     * @param eventListener
     */
    void registerEvent(IEventListener<? extends BaseEvent> eventListener);
}
