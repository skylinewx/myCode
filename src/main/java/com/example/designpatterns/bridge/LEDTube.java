package com.example.designpatterns.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LED灯管
 * @author skyline
 */
public class LEDTube implements ILightTube{
    private static final Logger logger = LoggerFactory.getLogger(LEDTube.class);
    @Override
    public void shine() {
        logger.info("LED灯");
    }
}
