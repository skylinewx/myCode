package com.example.designpatterns.decorate;

/**
 * @author wangxing
 * @date 2021/12/13
 **/
public abstract class MaterialAble {

    private final MaterialAble oriMaterial;

    public MaterialAble(MaterialAble oriMaterial) {
        this.oriMaterial = oriMaterial;
    }

    /**
     * 计算价格
     *
     * @return
     */
    public final double calcCost() {
        return oriMaterial == null ? cost() : oriMaterial.calcCost() + cost();
    }

    /**
     * 获取材料列表
     *
     * @return
     */
    public final String getMaterialList() {
        return oriMaterial == null ? name() : oriMaterial.getMaterialList() + "," + name();
    }

    /**
     * 名称
     *
     * @return
     */
    protected abstract String name();

    /**
     * 费用
     *
     * @return
     */
    protected abstract double cost();
}
