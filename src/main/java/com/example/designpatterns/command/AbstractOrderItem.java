package com.example.designpatterns.command;

import org.springframework.beans.factory.BeanNameAware;

/**
 * 抽象命令
 * @author skyline
 */
public abstract class AbstractOrderItem implements OrderItem, BeanNameAware {

    private String beanName;

    @Override
    public String common() {
        return beanName;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
