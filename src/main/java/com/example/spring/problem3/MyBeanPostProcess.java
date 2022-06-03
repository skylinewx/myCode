package com.example.spring.problem3;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class MyBeanPostProcess implements BeanPostProcessor, Ordered {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MyComponent) {
            ((MyComponent) bean).setName("zhangsan");
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
