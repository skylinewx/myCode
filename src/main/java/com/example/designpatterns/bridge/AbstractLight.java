package com.example.designpatterns.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象的灯
 * @author skyline
 */
public abstract class AbstractLight {

    private final ILightTube lightTube;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected AbstractLight(ILightTube lightTube) {
        this.lightTube = lightTube;
    }

    /**
     * 发光
     */
    public void shine(){
        lightTube.shine();
        logger.info("{}用{}发光了",this.getClass().getSimpleName(),lightTube.getClass().getSimpleName());
    }
}
