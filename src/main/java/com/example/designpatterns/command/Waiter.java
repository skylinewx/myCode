package com.example.designpatterns.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务员
 * @author skyline
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Waiter implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger("服务员");

    private List<String> orderItems;
    private ApplicationContext applicationContext;

    public void showOrder(){
        String[] beanNamesForType = applicationContext.getBeanNamesForType(OrderItem.class);
        logger.info("菜单：{}", (Object) beanNamesForType);
    }

    public void beginOrder(){
        this.orderItems = new ArrayList<>();
    }

    public void addItem(String item){
        this.orderItems.add(item);
    }

    public void endOrder(){
        for (String orderItem : orderItems) {
            OrderItem orderItemBean = applicationContext.getBean(orderItem, OrderItem.class);
            orderItemBean.execute();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
