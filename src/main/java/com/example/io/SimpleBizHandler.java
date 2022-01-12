package com.example.io;

import com.googlecode.aviator.AviatorEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleBizHandler {
    private static final Logger logger = LoggerFactory.getLogger(SimpleBizHandler.class);

    public String handleBiz(String param){
        String result;
        try {
            result = (String) AviatorEvaluator.execute(param);
            logger.info("AviatorEvaluator的计算结果是[{}]", result);
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }
}
