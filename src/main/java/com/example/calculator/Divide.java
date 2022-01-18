package com.example.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 除法操作符
 */
public class Divide extends Operator {

    @Override
    public char operator() {
        return '/';
    }

    @Override
    public Object getValue() {
        Object leftResult = getLeftResult();
        Object rightResult = getRightResult();
        if (leftResult instanceof Integer) {
            int left = (int) leftResult;
            if (rightResult instanceof Integer) {
                return left / (Integer) rightResult;
            }
            if (rightResult instanceof Double) {
                return left / (Double) rightResult;
            }
            if (rightResult instanceof Float) {
                return left / (Float) rightResult;
            }
            if (rightResult instanceof BigInteger) {
                return (new BigInteger(String.valueOf(leftResult))).divide(((BigInteger) rightResult));
            }
            if (rightResult instanceof BigDecimal) {
                return (new BigDecimal(String.valueOf(leftResult))).divide(((BigDecimal) rightResult));
            }
        }
        if (leftResult instanceof Double) {
            double left = (Double) leftResult;
            if (rightResult instanceof Integer) {
                return left / (Integer) rightResult;
            }
            if (rightResult instanceof Double) {
                return left / (Double) rightResult;
            }
            if (rightResult instanceof Float) {
                return left / (Float) rightResult;
            }
            if (rightResult instanceof BigInteger) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigDecimal) {
                return new BigDecimal(String.valueOf(leftResult)).divide(((BigDecimal) rightResult));
            }
        }
        if (leftResult instanceof Float) {
            float left = (float) leftResult;
            if (rightResult instanceof Integer) {
                return left / (Integer) rightResult;
            }
            if (rightResult instanceof Double) {
                return left / (Double) rightResult;
            }
            if (rightResult instanceof Float) {
                return left / (Float) rightResult;
            }
            if (rightResult instanceof BigInteger) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigDecimal) {
                return (new BigDecimal(String.valueOf(leftResult))).divide((BigDecimal) rightResult);
            }
        }
        if (leftResult instanceof BigInteger) {
            if (rightResult instanceof Integer || rightResult instanceof Double || rightResult instanceof Float || rightResult instanceof BigDecimal) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigInteger) {
                return ((BigInteger) leftResult).divide((BigInteger) rightResult);
            }
        }
        if (leftResult instanceof BigDecimal) {
            BigDecimal left = (BigDecimal) leftResult;
            if (rightResult instanceof Integer || rightResult instanceof Double || rightResult instanceof Float) {
                return left.divide(new BigDecimal(String.valueOf(rightResult)));
            }
            if (rightResult instanceof BigInteger) {
                throw new RuntimeException("not support!");
            }
            if (rightResult instanceof BigDecimal) {
                return left.divide(((BigDecimal) rightResult));
            }
        }
        throw new RuntimeException("not support!");
    }

    @Override
    public int priority() {
        return 1;
    }
}
