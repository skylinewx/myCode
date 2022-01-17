package com.example.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 加法操作符节点
 */
public class Add extends Operator{

    @Override
    public String getText() {
        return "+";
    }

    @Override
    public Object getValue() {
        Object leftResult = getLeftResult();
        Object rightResult = getRightResult();
        if (leftResult instanceof Integer) {
            int left = (int) leftResult;
            if (rightResult instanceof Integer) {
                return left + (Integer) rightResult;
            }
            if (rightResult instanceof Double) {
                return left + (Double) rightResult;
            }
            if (rightResult instanceof Float) {
                return left + (Float) rightResult;
            }
            if (rightResult instanceof BigInteger) {
                return ((BigInteger) rightResult).add(BigInteger.valueOf(left));
            }
            if (rightResult instanceof BigDecimal) {
                return ((BigDecimal) rightResult).add(BigDecimal.valueOf(left));
            }
        }
        if (leftResult instanceof Double) {
            double left = (Double) leftResult;
            if (rightResult instanceof Integer) {
                return left + (Integer) rightResult;
            }
            if (rightResult instanceof Double) {
                return left + (Double) rightResult;
            }
            if (rightResult instanceof Float) {
                return left + (Float) rightResult;
            }
            if (rightResult instanceof BigInteger) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigDecimal) {
                return ((BigDecimal) rightResult).add(new BigDecimal(String.valueOf(leftResult)));
            }
        }
        if (leftResult instanceof Float) {
            float left = (float) leftResult;
            if (rightResult instanceof Integer) {
                return left + (Integer) rightResult;
            }
            if (rightResult instanceof Double) {
                return left + (Double) rightResult;
            }
            if (rightResult instanceof Float) {
                return left + (Float) rightResult;
            }
            if (rightResult instanceof BigInteger) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigDecimal) {
                return ((BigDecimal) rightResult).add(new BigDecimal(String.valueOf(rightResult)));
            }
        }
        if (leftResult instanceof BigInteger) {
            if (rightResult instanceof Integer) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof Double) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof Float) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigInteger) {
                return ((BigInteger) rightResult).add((BigInteger) leftResult);
            }
            if (rightResult instanceof BigDecimal) {
                throw new RuntimeException("not support!");
            }
        }
        if (leftResult instanceof BigDecimal) {
            BigDecimal left = (BigDecimal) leftResult;
            if (rightResult instanceof Integer || rightResult instanceof Double || rightResult instanceof Float) {
                return left.add(new BigDecimal(String.valueOf(rightResult)));
            }
            if (rightResult instanceof BigInteger) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigDecimal) {
                return ((BigDecimal) rightResult).add(left);
            }
        }
        throw new RuntimeException("not support!");
    }
}
