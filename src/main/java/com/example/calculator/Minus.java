package com.example.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 减法操作符
 */
public class Minus extends Operator {

    @Override
    public char operator() {
        return '-';
    }

    @Override
    public Object getValue() {
        Object leftResult = getLeftResult();
        Object rightResult = getRightResult();
        if (leftResult instanceof Integer) {
            int left = (int) leftResult;
            if (rightResult instanceof Integer) {
                return left - (Integer) rightResult;
            }
            if (rightResult instanceof Double) {
                return left - (Double) rightResult;
            }
            if (rightResult instanceof Float) {
                return left - (Float) rightResult;
            }
            if (rightResult instanceof BigInteger) {
                return BigInteger.valueOf(left).subtract(((BigInteger) rightResult));
            }
            if (rightResult instanceof BigDecimal) {
                return BigDecimal.valueOf(left).subtract(((BigDecimal) rightResult));
            }
        }
        if (leftResult instanceof Double) {
            double left = (Double) leftResult;
            if (rightResult instanceof Integer) {
                return left - (Integer) rightResult;
            }
            if (rightResult instanceof Double) {
                return left - (Double) rightResult;
            }
            if (rightResult instanceof Float) {
                return left - (Float) rightResult;
            }
            if (rightResult instanceof BigInteger) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigDecimal) {
                return new BigDecimal(String.valueOf(leftResult)).subtract(((BigDecimal) rightResult));
            }
        }
        if (leftResult instanceof Float) {
            float left = (float) leftResult;
            if (rightResult instanceof Integer) {
                return left - (Integer) rightResult;
            }
            if (rightResult instanceof Double) {
                return left - (Double) rightResult;
            }
            if (rightResult instanceof Float) {
                return left - (Float) rightResult;
            }
            if (rightResult instanceof BigInteger) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigDecimal) {
                return new BigDecimal(String.valueOf(leftResult)).subtract(((BigDecimal) rightResult));
            }
        }
        if (leftResult instanceof BigInteger) {
            if (rightResult instanceof Integer || rightResult instanceof Double || rightResult instanceof Float || rightResult instanceof BigDecimal) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigInteger) {
                return ((BigInteger) leftResult).subtract(((BigInteger) rightResult));
            }
        }
        if (leftResult instanceof BigDecimal) {
            BigDecimal left = (BigDecimal) leftResult;
            if (rightResult instanceof Integer || rightResult instanceof Double || rightResult instanceof Float) {
                return left.subtract(new BigDecimal(String.valueOf(rightResult)));
            }
            if (rightResult instanceof BigInteger) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigDecimal) {
                return left.subtract(((BigDecimal) rightResult));
            }
        }
        throw new RuntimeException("not support!");
    }

    @Override
    public int priority() {
        return 0;
    }
}
