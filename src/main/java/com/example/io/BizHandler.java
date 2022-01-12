package com.example.io;

import com.googlecode.aviator.AviatorEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务处理器
 * @author skyline
 */
public class BizHandler {
    private static final Logger logger = LoggerFactory.getLogger(BizHandler.class);

    public String handleBiz(String param){
        Object result;
        try {
            result = AviatorEvaluator.execute(param);
            logger.info("AviatorEvaluator的计算结果是[{}]", result);
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result.toString();
    }
}
