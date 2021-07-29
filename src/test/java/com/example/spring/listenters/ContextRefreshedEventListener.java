package com.example.spring.listenters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component("MyContextRefreshedEventListener")
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ContextRefreshedEventListener.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("**********ContextRefreshedEvent************");
    }
}
