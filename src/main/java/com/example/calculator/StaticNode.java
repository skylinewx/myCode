package com.example.calculator;

import com.example.designpatterns.visitor.NodeVisitor;

/**
 * 变量节点
 */
public class StaticNode extends Expression{

    @Override
    public String getText() {
        return super.getText();
    }

    @Override
    public void accept(NodeVisitor nodeVisitor) {
        nodeVisitor.visitStaticNode(this);
    }

    @Override
    public Object getValue() {
        if (env == null) {
            return null;
        }
        return env.get(getText());
    }
}
