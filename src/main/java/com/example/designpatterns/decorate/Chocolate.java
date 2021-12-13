package com.example.designpatterns.decorate;

/**
 * 巧克力
 *
 * @author wangxing
 * @date 2021/12/13
 **/
public class Chocolate extends MaterialAble {

    public Chocolate(MaterialAble materialAble) {
        super(materialAble);
    }

    @Override
    public String getMaterialList() {
        return materialAble == null ? "巧克力" : materialAble.getMaterialList() + "," + "巧克力";
    }

    @Override
    public double calcCost() {
        return materialAble == null ? 1.3 : materialAble.calcCost() + 1.3;
    }
}
