package com.example.spring.listenters;

import com.example.spring.events.MyTestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * 最高优先级2号
 */
@Component
public class MyTestEventListenerPriorityOrdered2 implements ApplicationListener<MyTestEvent>, PriorityOrdered {
    private static final Logger logger = LoggerFactory.getLogger(MyTestEventListenerPriorityOrdered2.class);
    @Override
    public void onApplicationEvent(MyTestEvent event) {
        logger.info("[{}]接收到事件[{}]了",this.getClass().getSimpleName(),event);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
