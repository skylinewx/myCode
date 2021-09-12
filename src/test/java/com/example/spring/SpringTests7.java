package com.example.spring;

import com.example.spring.utils.PlaceholderParser;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数解析
 */
public class SpringTests7 {

    /**
     * ${xx.sss:ccc}参数解析
     */
    @Test
    public void test1() {
        Map<String, String> valueMap = new HashMap<>();
//        valueMap.put("poolType", "cpu");
        valueMap.put("cpu", "" + Runtime.getRuntime().availableProcessors());
        valueMap.put("io", "" + Runtime.getRuntime().availableProcessors() * 10);
        PlaceholderParser parser = new PlaceholderParser(valueMap, "${", "}", ":");
        String oriStr = "demo:${${poolType:io}}";
        String parseResult = parser.doParse(oriStr);
        System.out.println(oriStr + "~~~" + parseResult);
    }


}
