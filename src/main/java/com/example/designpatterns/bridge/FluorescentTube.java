package com.example.designpatterns.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 荧光灯管
 * @author skyline
 */
public class FluorescentTube implements ILightTube{
    private static final Logger logger = LoggerFactory.getLogger(FluorescentTube.class);
    @Override
    public void shine() {
        logger.info("荧光灯");
    }
}
