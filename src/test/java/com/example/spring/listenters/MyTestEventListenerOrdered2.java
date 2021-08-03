package com.example.spring.listenters;

import com.example.spring.events.MyTestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 普通高优先级2号
 */
@Component
public class MyTestEventListenerOrdered2 implements ApplicationListener<MyTestEvent>, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(MyTestEventListenerOrdered2.class);
    @Override
    public void onApplicationEvent(MyTestEvent event) {
        logger.info("[{}]接收到事件[{}]了",this.getClass().getSimpleName(),event);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
