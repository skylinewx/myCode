package com.example.calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CalculationMain {
    private static final Logger logger = LoggerFactory.getLogger(CalculationMain.class);

    public static void main(String[] args) {
        String exp = "(2-1)*3+2+(3*(9-(5+2)*1))";
        logger.info("exp is {}", exp);
        Object exec = Calculation.exec(exp);
        logger.info("my answer is {}", exec);

        String exp2 = "Max(8,Max(5,4))+plus100(max(3,9))";
        logger.info("exp is {}", exp2);
        Object exec2 = Calculation.exec(exp2);
        logger.info("my answer is {}", exec2);

        String exp3 = "Max(8,Max(5,cc))+plus100(max(aa*2,bb))";
        Map<String, Object> env = new HashMap<>();
        env.put("cc", 4);
        env.put("aa", 3);
        env.put("bb", 9);
        logger.info("exp is {}", exp3);
        Object exec3 = Calculation.exec(exp3);
        logger.info("my answer is {}", exec3);

    }
}
