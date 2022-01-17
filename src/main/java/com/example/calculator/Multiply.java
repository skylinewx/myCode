package com.example.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 乘法操作符
 */
public class Multiply extends Operator{

    @Override
    public String getText() {
        return "*";
    }

    @Override
    public Object getValue() {
        Object leftResult = getLeftResult();
        Object rightResult = getRightResult();
        if (leftResult instanceof Integer) {
            int left = (int) leftResult;
            if (rightResult instanceof Integer) {
                return left * (Integer) rightResult;
            }
            if (rightResult instanceof Double) {
                return left * (Double) rightResult;
            }
            if (rightResult instanceof Float) {
                return left * (Float) rightResult;
            }
            if (rightResult instanceof BigInteger) {
                return ((BigInteger) rightResult).multiply(BigInteger.valueOf(left));
            }
            if (rightResult instanceof BigDecimal) {
                return ((BigDecimal) rightResult).multiply(BigDecimal.valueOf(left));
            }
        }
        if (leftResult instanceof Double) {
            double left = (Double) leftResult;
            if (rightResult instanceof Integer) {
                return left * (Integer) rightResult;
            }
            if (rightResult instanceof Double) {
                return left * (Double) rightResult;
            }
            if (rightResult instanceof Float) {
                return left * (Float) rightResult;
            }
            if (rightResult instanceof BigInteger) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigDecimal) {
                return ((BigDecimal) rightResult).multiply(new BigDecimal(String.valueOf(leftResult)));
            }
        }
        if (leftResult instanceof Float) {
            float left = (float) leftResult;
            if (rightResult instanceof Integer) {
                return left * (Integer) rightResult;
            }
            if (rightResult instanceof Double) {
                return left * (Double) rightResult;
            }
            if (rightResult instanceof Float) {
                return left * (Float) rightResult;
            }
            if (rightResult instanceof BigInteger) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigDecimal) {
                return ((BigDecimal) rightResult).multiply(new BigDecimal(String.valueOf(rightResult)));
            }
        }
        if (leftResult instanceof BigInteger) {
            if (rightResult instanceof Integer || rightResult instanceof Double || rightResult instanceof Float || rightResult instanceof BigDecimal) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigInteger) {
                return ((BigInteger) rightResult).multiply((BigInteger) leftResult);
            }
        }
        if (leftResult instanceof BigDecimal) {
            BigDecimal left = (BigDecimal) leftResult;
            if (rightResult instanceof Integer || rightResult instanceof Double || rightResult instanceof Float) {
                return left.multiply(new BigDecimal(String.valueOf(rightResult)));
            }
            if (rightResult instanceof BigInteger) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigDecimal) {
                return ((BigDecimal) rightResult).multiply(left);
            }
        }
        throw new RuntimeException("not support!");
    }
}
