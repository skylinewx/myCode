package com.example.spring.listenters;

import com.example.spring.events.MyTestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MyTestEventListener implements ApplicationListener<MyTestEvent> {
    private static final Logger logger = LoggerFactory.getLogger(MyTestEventListener.class);
    @Override
    public void onApplicationEvent(MyTestEvent event) {
        logger.info("接收到事件[{}]了",event);
    }
}
