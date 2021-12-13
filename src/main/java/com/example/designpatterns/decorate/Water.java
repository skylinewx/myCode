package com.example.designpatterns.decorate;

/**
 * 水
 *
 * @author wangxing
 * @date 2021/12/13
 **/
public class Water extends MaterialAble {

    public Water(MaterialAble oriMaterial) {
        super(oriMaterial);
    }

    @Override
    protected String name() {
        return "水";
    }

    @Override
    protected double cost() {
        return 0.2;
    }
}
