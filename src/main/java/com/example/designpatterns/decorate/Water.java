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
    public String getMaterialList() {
        return materialAble == null ? "水" : materialAble.getMaterialList() + "," + "水";
    }

    @Override
    public double calcCost() {
        return materialAble == null ? 0.2 : materialAble.calcCost() + 0.2;
    }
}
