package com.example.designpatterns.observer;

/**
 * 事件
 *
 * @author skyline
 */
public class BaseEvent {

    private final Object data;

    public BaseEvent(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
