package com.example.designpatterns.visitor;

import com.example.calculator.DataNode;
import com.example.calculator.FunctionNode;
import com.example.calculator.Operator;
import com.example.calculator.StaticNode;

public interface NodeVisitor {

    void visitOperator(Operator operator);

    void visitDataNode(DataNode dataNode);

    void visitStaticNode(StaticNode staticNode);

    void visitFunctionNode(FunctionNode functionNode);
}
