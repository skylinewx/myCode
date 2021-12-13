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
    public String getMaterialList() {
        return materialAble == null ? "咖啡" : materialAble.getMaterialList() + "," + "咖啡";
    }

    @Override
    public double calcCost() {
        return materialAble == null ? 1.1 : materialAble.calcCost() + 1.1;
    }
}
