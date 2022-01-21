package com.example.designpatterns.visitor;

import com.example.calculator.*;

/**
 * 逆波兰式visitor
 *
 * @author skyline
 */
public class ReversePolishVisitor implements NodeVisitor {
    @Override
    public void visitOperator(Operator operator) {
        operator.getLeftNode().accept(this);
        operator.getRightNode().accept(this);
        System.out.print(operator.getText());
    }

    @Override
    public void visitDataNode(DataNode dataNode) {
        System.out.print(dataNode.getText());
    }

    @Override
    public void visitStaticNode(StaticNode staticNode) {
        System.out.print(staticNode.getText());
    }

    @Override
    public void visitFunctionNode(FunctionNode functionNode) {
        System.out.print(functionNode.getText());
    }

    @Override
    public void visitNegativeNode(NegativeNode negativeNode) {
        System.out.print("0");
        negativeNode.getDelegat().accept(this);
        System.out.print(negativeNode.getText());
    }
}
