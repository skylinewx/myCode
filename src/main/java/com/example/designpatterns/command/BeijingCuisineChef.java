package com.example.designpatterns.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 京菜厨师
 * @author skyline
 */
@Component
public class BeijingCuisineChef {
    private static final Logger logger = LoggerFactory.getLogger("京菜厨师");

    public void cookZhaGuanChang(){
        logger.info("炸灌肠做好了");
    }

    public void cookBaoDu(){
        logger.info("爆肚做好了");
    }
}
