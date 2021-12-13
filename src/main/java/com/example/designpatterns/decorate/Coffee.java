package com.example.designpatterns.decorate;

/**
 * 咖啡
 *
 * @author wangxing
 * @date 2021/12/13
 **/
public class Coffee extends MaterialAble {

    public Coffee(MaterialAble oriMaterial) {
        super(oriMaterial);
    }

    @Override
    protected String name() {
        return "咖啡";
    }

    @Override
    protected double cost() {
        return 1.1;
    }
}
