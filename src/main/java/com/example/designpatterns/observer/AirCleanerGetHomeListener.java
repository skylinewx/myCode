package com.example.designpatterns.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 空气净化器对回家监听
 *
 * @author skyline
 */
public class AirCleanerGetHomeListener implements IEventListener<GetHomeEvent> {
    private static final Logger logger = LoggerFactory.getLogger(AirCleanerGetHomeListener.class);

    @Override
    public void listener(GetHomeEvent event) {
        logger.info("[{}]回来了，空气净化器已打开", event.getData());
    }
}
