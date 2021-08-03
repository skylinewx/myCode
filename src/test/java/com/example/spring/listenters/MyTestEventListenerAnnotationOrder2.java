package com.example.spring.listenters;

import com.example.spring.events.MyTestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 注释优先级2号
 */
@Component
public class MyTestEventListenerAnnotationOrder2 implements ApplicationListener<MyTestEvent> {
    private static final Logger logger = LoggerFactory.getLogger(MyTestEventListenerAnnotationOrder2.class);

    @Order(value = 2)
    @Override
    public void onApplicationEvent(MyTestEvent event) {
        logger.info("[{}]接收到事件[{}]了",this.getClass().getSimpleName(),event);
    }
}
