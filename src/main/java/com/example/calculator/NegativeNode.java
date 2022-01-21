package com.example.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author wangxing
 * @date 2022/1/21
 **/
public class NegativeNode implements Node {

    private Node delegat;

    public NegativeNode(Node delegat) {
        this.delegat = delegat;
    }

    @Override
    public String getText() {
        return "-";
    }

    @Override
    public Object getValue() {
        Object value = delegat.getValue();
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return -1 * (Integer) value;
        }
        if (value instanceof Double) {
            return -1 * (Double) value;
        }
        if (value instanceof Float) {
            return -1 * (Float) value;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).multiply(new BigDecimal("-1"));
        }
        if (value instanceof BigInteger) {
            return ((BigInteger) value).multiply(new BigInteger("-1"));
        }
        throw new RuntimeException("not support!");
    }

    @Override
    public Node parse() {
        delegat = delegat.parse();
        return this;
    }

    public Node getDelegat() {
        return delegat;
    }

    @Override
    public void accept(NodeVisitor nodeVisitor) {
        nodeVisitor.visitNegativeNode(this);
    }
}
