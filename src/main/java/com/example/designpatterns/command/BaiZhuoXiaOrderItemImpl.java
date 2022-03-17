package com.example.designpatterns.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("baizhuoxia")
public class BaiZhuoXiaOrderItemImpl extends AbstractOrderItem {

    @Autowired
    private CantoneseChef cantoneseChef;

    @Override
    public void execute() {
        cantoneseChef.cookBaiZhuoXia();
    }
}
