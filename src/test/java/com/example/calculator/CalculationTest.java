package com.example.calculator;

import org.junit.Assert;
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
        Object exe = exe(exp, null);
        Assert.assertEquals(11, exe);
    }

    @Test
    public void test2() {
        String exp = "Max(8,Max(5,4))+plus100(max(3,9))";
        Object exe = exe(exp, null);
        Assert.assertEquals(117, exe);
    }


    @Test
    public void test3() {
        String exp = "Max(8,Max(5,cc))*2+plus100(max(aa*2,bb))";
        Map<String, Object> env = new HashMap<>();
        env.put("cc", 4);
        env.put("aa", 3);
        env.put("bb", 9);
        Object exe = exe(exp, env);
        Assert.assertEquals(125, exe);
    }

    @Test
    public void test4() {
        String exp = "2-3-4-5";
        Object exe = exe(exp, null);
        Assert.assertEquals(-10, exe);
    }

    @Test
    public void test5() {
        String exp = "(2-1)*3-2+(3*(9-(5+2)*1))";
        Object exe = exe(exp, null);
        Assert.assertEquals(7, exe);
    }

    @Test
    public void test6() {
        String exp = "-1*2";
        Object exe = exe(exp, null);
        Assert.assertEquals(-2, exe);
    }

    @Test
    public void test7() {
        String exp = "-1+2*(-5)-8";
        Object exe = exe(exp, null);
        Assert.assertEquals(-19, exe);
    }

    private Object exe(String exp, Map<String, Object> env) {
        logger.info("exp is {}", exp);
        if (env != null) {
            logger.info("env is {}", env);
        }
        Object exec = Calculation.exec(exp, env);
        logger.info("my answer is {}", exec);
        return exec;
    }
}
