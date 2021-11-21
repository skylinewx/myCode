package com.example.simple;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SimpleTest {
    @Test
    public void test1() {
        Map<String, String> map = new HashMap<>();
        map.put("001", "001");
        String value = map.get("001");
        map.put("001", "002");
        System.out.println(value.equals(map.get("001")));
    }

    @Test
    public void test2() {
        Random random = new Random();
        System.out.println(String.format("%03d", random.nextInt(10)));
    }
}
