package com.example.designpatterns.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 粤菜厨师
 * @author skyline
 */
@Component
public class CantoneseChef {
    private static final Logger logger = LoggerFactory.getLogger("粤菜厨师");

    public void cookBaiQieJi(){
        logger.info("白切鸡做好了");
    }

    public void cookBaiZhuoXia(){
        logger.info("白灼虾做好了");
    }
}
