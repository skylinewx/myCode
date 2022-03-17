package com.example.designpatterns.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("baodu")
public class BaoDuOrderItemImpl extends AbstractOrderItem{

    @Autowired
    private BeijingCuisineChef beijingCuisineChef;

    @Override
    public void execute() {
        beijingCuisineChef.cookBaoDu();
    }
}
