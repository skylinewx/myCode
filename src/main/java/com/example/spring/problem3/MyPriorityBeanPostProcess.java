package com.example.spring.problem3;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * @author wangxing
 */
@Component
public class MyPriorityBeanPostProcess implements BeanPostProcessor, Ordered {

    @Autowired
    private MyComponent myComponent;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
