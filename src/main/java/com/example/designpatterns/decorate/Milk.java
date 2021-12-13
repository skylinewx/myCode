package com.example.designpatterns.decorate;

/**
 * 牛奶
 *
 * @author wangxing
 * @date 2021/12/13
 **/
public class Milk extends MaterialAble {

    public Milk(MaterialAble oriMaterial) {
        super(oriMaterial);
    }

    @Override
    protected String name() {
        return "牛奶";
    }

    @Override
    protected double cost() {
        return 1.5;
    }
}
