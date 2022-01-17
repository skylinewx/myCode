package com.example.calculator;

/**
 * 操作符节点
 */
public abstract class Operator implements Node{
    private Node left;
    private Node right;
    private Node leftResult;
    private Node rightResult;

    public static Operator valueOf(String text){
        switch (text) {
            case "+":
                return new Add();
            case "-":
                return new Minus();
            case "*":
                return new Multiply();
            case "/":
                return new Divide();
            default:
                throw new RuntimeException("not support!");
        }
    }

    @Override
    public Node parse() {
        leftResult = left.parse();
        rightResult = right.parse();
        return this;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Object getLeftResult() {
        return leftResult.getValue();
    }

    public Object getRightResult() {
        return rightResult.getValue();
    }

    @Override
    public Object getValue() {
        throw new RuntimeException("not support!");
    }


}
