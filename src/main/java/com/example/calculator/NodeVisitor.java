package com.example.calculator;

/**
 * 节点访问器
 */
public interface NodeVisitor {

    /**
     * 访问操作符
     *
     * @param operator
     */
    void visitOperator(Operator operator);

    /**
     * 访问数据节点
     *
     * @param dataNode
     */
    void visitDataNode(DataNode dataNode);

    /**
     * 访问常量节点
     *
     * @param staticNode
     */
    void visitStaticNode(StaticNode staticNode);

    /**
     * 访问函数节点
     *
     * @param functionNode
     */
    void visitFunctionNode(FunctionNode functionNode);

    /**
     * 访问负数节点
     *
     * @param negativeNode
     */
    void visitNegativeNode(NegativeNode negativeNode);
}
