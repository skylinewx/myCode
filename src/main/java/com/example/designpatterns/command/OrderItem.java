package com.example.designpatterns.command;

/**
 * 菜单
 * @author skyline
 */
public interface OrderItem {
    /**
     * 执行
     */
    void execute();

    String common();
}
