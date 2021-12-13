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
    public String getMaterialList() {
        return materialAble == null ? "牛奶" : materialAble.getMaterialList() + "," + "牛奶";
    }

    @Override
    public double calcCost() {
        return materialAble == null ? 1.5 : materialAble.calcCost() + 1.5;
    }
}
