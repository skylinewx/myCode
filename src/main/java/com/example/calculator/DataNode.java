package com.example.calculator;

import com.example.designpatterns.visitor.NodeVisitor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 数值节点
 */
public class DataNode extends Expression{

    @Override
    public String getText() {
        return super.getText();
    }

    @Override
    public void accept(NodeVisitor nodeVisitor) {
        nodeVisitor.visitDataNode(this);
    }

    @Override
    public Object getValue() {
        String text = getText();
        int i = text.indexOf('.');
        if (i == -1) {
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return new BigInteger(text);
            }
        }
        int precision = text.length() - i;
        if (precision <= 6) {
            try {
                return Float.parseFloat(text);
            } catch (NumberFormatException e) {
                return new BigDecimal(text);
            }
        }
        if (precision <= 15) {
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException e) {
                return new BigDecimal(text);
            }
        }
        return new BigDecimal(text);
    }
}
