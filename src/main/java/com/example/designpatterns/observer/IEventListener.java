package com.example.designpatterns.observer;

/**
 * 事件监听者
 *
 * @author skyline
 */
public interface IEventListener<T extends BaseEvent> {

    /**
     * 监听
     *
     * @param event 事件
     */
    void listener(T event);
}
