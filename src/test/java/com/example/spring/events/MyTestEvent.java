package com.example.spring.events;

import org.springframework.context.ApplicationEvent;

public class MyTestEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public MyTestEvent(Object source) {
        super(source);
    }
}
