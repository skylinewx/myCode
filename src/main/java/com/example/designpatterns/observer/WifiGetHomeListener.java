package com.example.designpatterns.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * wifi对回家监听
 *
 * @author skyline
 */
public class WifiGetHomeListener implements IEventListener<GetHomeEvent> {
    private static final Logger logger = LoggerFactory.getLogger(WifiGetHomeListener.class);

    @Override
    public void listener(GetHomeEvent event) {
        logger.info("WiFi已自动连接到[{}]的手机", event.getData());
    }
}
