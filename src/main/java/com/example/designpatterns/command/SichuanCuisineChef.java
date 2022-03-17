package com.example.designpatterns.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 川菜厨师
 * @author skyline
 */
@Component
public class SichuanCuisineChef {
    private static final Logger logger = LoggerFactory.getLogger("川菜厨师");

    public void cookShuiZhuYu(){
        logger.info("水煮鱼做好了");
    }

    public void cookMaPoDouFu(){
        logger.info("麻婆豆腐做好了");
    }
}
