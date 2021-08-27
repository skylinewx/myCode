package com.example.spring.beans2;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;

public class Bean3 implements BeanNameAware,InitializingBean {

    private String beanName;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(beanName+":afterPropertiesSet");
    }

    public void initMethod(){
        System.out.println(beanName+":initMethod");
    }

    @PostConstruct
    public void postConstruct(){
        System.out.println(beanName+":postConstruct");
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
