package com.example.calculator;

/**
 * 计算节点
 */
public interface Node {

    /**
     * 节点表达式
     * @return
     */
    String getText();

    /**
     * 节点值
     * @return
     */
    Object getValue();

    /**
     * 解析之后的节点
     * @return
     */
    Node parse();
}
