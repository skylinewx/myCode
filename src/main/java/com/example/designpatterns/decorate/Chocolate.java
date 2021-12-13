package com.example.designpatterns.decorate;

/**
 * 巧克力
 *
 * @author wangxing
 * @date 2021/12/13
 **/
public class Chocolate extends MaterialAble {

    public Chocolate(MaterialAble oriMaterial) {
        super(oriMaterial);
    }

    @Override
    protected String name() {
        return "巧克力";
    }

    @Override
    protected double cost() {
        return 1.3;
    }
}
