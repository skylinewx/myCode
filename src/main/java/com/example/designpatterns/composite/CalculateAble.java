package com.example.designpatterns.composite;

import java.util.List;

/**
 * @author skyline
 */
public interface CalculateAble {
    /**
     * 计算价格
     */
    double calculate();

    /**
     * 当前商品名
     *
     * @return
     */
    String name();

    /**
     * 添加另一个可计算的节点
     *
     * @param calculateAble
     */
    default void add(CalculateAble calculateAble) {
    }

    /**
     * 获得商品列表
     *
     * @return
     */
    default List<CalculateAble> getCalculateAbleList() {
        return null;
    }
}
