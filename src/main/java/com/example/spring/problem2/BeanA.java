package com.example.spring.problem2;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author wangxing
 * @date 2022/4/1
 **/
@Component
public class BeanA {

    private final BeanB beanB;

    @Lazy
    public BeanA(BeanB beanB) {
        this.beanB = beanB;
    }

    public BeanB getBeanB() {
        return beanB;
    }
}
