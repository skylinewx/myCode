package com.example.spring.components;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("bean3")) {
            System.out.println("now bean3 come in postProcessBeforeInitialization======");
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("bean3")) {
            System.out.println("now bean3 come in postProcessAfterInitialization======");
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
