package com.example.designpatterns.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("zhaguanchang")
public class ZhaGuanChangOrderItemImpl extends AbstractOrderItem{

    @Autowired
    private BeijingCuisineChef beijingCuisineChef;

    @Override
    public void execute() {
        beijingCuisineChef.cookZhaGuanChang();
    }
}
