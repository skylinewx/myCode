package com.example.spring.problem2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangxing
 * @date 2022/4/1
 **/
@Component
public class BeanB {

    @Autowired
    private BeanA beanA;

    public BeanA getBeanA() {
        return beanA;
    }
}
