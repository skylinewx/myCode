package com.example.calculator;

/**
 * 变量节点
 */
public class StaticNode extends Expression{

    @Override
    public String getText() {
        return super.getText();
    }

    @Override
    public Object getValue() {
        if (env == null) {
            return null;
        }
        return env.get(getText());
    }
}
