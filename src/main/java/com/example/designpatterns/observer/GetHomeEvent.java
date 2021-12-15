package com.example.designpatterns.observer;

/**
 * 到家了事件
 *
 * @author skyline
 */
public class GetHomeEvent extends BaseEvent {
    public GetHomeEvent(String userName) {
        super(userName);
    }
}
