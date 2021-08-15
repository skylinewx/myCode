package com.example.spring;

import com.example.spring.beans.Bean2;
import com.example.spring.beans.Bean3;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * bean的populateBean和initializeBean
 */
public class SpringTests4 {

    /**
     * populateBean
     */
    @Test
    public void test1(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Bean2 bean = applicationContext.getBean(Bean2.class);
        bean.hello();
    }


    /**
     * initializeBean
     */
    @Test
    public void test2(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Bean3 bean = applicationContext.getBean(Bean3.class);
        bean.hello();
    }
}
