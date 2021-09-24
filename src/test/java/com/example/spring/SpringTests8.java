package com.example.spring;

import com.example.spring.app.AnnotationApp;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 参数解析
 */
public class SpringTests8 {

    /**
     * AnnotationConfigApplicationContext
     * setRequiredProperties
     */
    @Test
    public void test1() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotationApp.class);
        applicationContext.getEnvironment().setRequiredProperties("myTest");
        applicationContext.refresh();
    }


}
