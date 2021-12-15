package com.example.designpatterns.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        IEventCenter eventCenter = EventCenter.INSTANCE;
        eventCenter.registerEvent(new AirCleanerGetHomeListener());
        eventCenter.registerEvent(new WifiGetHomeListener());
        //不指定具体的event类型，就是对所有事件都感兴趣
        eventCenter.registerEvent(new IEventListener<BaseEvent>() {
            @Override
            public void listener(BaseEvent event) {
                logger.info("eventType[{}] getMessage [{}]", event.getClass().getSimpleName(), event.getData());
            }
        });
        eventCenter.publishEvent(new GetHomeEvent("张三"));
        eventCenter.publishEvent(new BaseEvent("李四"));
    }
}
