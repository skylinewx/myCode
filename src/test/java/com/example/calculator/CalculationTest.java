package com.example.calculator;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author skyline
 * @date 2022/1/18
 **/
public class CalculationTest {

    private static final Logger logger = LoggerFactory.getLogger(CalculationMain.class);

    @Test
    public void test1() {
        String exp = "(2-1)*3+2+(3*(9-(5+2)*1))";
        exe(exp, null);
    }

    @Test
    public void test2() {
        String exp = "Max(8,Max(5,4))+plus100(max(3,9))";
        exe(exp, null);
    }


    @Test
    public void test3() {
        String exp = "Max(8,Max(5,cc))*2+plus100(max(aa*2,bb))";
        Map<String, Object> env = new HashMap<>();
        env.put("cc", 4);
        env.put("aa", 3);
        env.put("bb", 9);
        exe(exp, env);
    }

    private void exe(String exp, Map<String, Object> env) {
        logger.info("exp is {}", exp);
        if (env != null) {
            logger.info("env is {}", env);
        }
        Object exec = Calculation.exec(exp, env);
        logger.info("my answer is {}", exec);
    }
}
