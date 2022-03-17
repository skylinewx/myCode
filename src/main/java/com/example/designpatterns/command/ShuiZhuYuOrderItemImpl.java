package com.example.designpatterns.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("shuizhuyu")
public class ShuiZhuYuOrderItemImpl extends AbstractOrderItem {

    @Autowired
    private SichuanCuisineChef sichuanCuisineChef;

    @Override
    public void execute() {
        sichuanCuisineChef.cookShuiZhuYu();
    }
}
