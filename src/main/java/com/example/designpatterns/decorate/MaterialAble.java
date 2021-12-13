package com.example.designpatterns.decorate;

/**
 * @author wangxing
 * @date 2021/12/13
 **/
public abstract class MaterialAble {

    final MaterialAble materialAble;

    public MaterialAble(MaterialAble materialAble) {
        this.materialAble = materialAble;
    }

    /**
     * 材料列表
     *
     * @return
     */
    public abstract String getMaterialList();

    /**
     * 计算费用
     *
     * @return
     */
    public abstract double calcCost();
}
