package com.example.designpatterns.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Banana[] test = new Banana[]{new Banana(1.2, 5), new Banana(2.3, 6), new Banana(3.2, 8), new Banana(2.9, 6)};
        SortUtil.sort(test, (o1, o2) -> {
            if (o1.getCount() > o2.getCount()) {
                return 1;
            }
            if (o1.getCount() < o2.getCount()) {
                return -1;
            }
            return 0;
        });
        logger.info("{}", (Object) test);
    }
}
