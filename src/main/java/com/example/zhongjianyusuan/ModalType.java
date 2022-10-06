package com.example.zhongjianyusuan;

public enum ModalType {
    /**
     * 指标表，模型即表名，没有BIZKEYORDER，没有FLOATORDER
     */
    ZB,
    /**
     * 多维表模型，模型名拼上_N _Y等才是表名，有BIZKEYORDER和FLOATORDER
     */
    HYPER,
    /**
     * 浮动表模型，模型名即表名，有BIZKEYORDER和FLOATORDER
     */
    FLOAT
}
