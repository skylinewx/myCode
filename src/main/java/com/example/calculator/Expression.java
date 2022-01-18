package com.example.calculator;

import java.util.Map;

/**
 * 表达式节点
 */
public class Expression implements Node {
    Map<String, Object> env;
    Map<String, IFunction> functionMap;
    private String text;

    public Expression() {
    }

    @Override
    public Node parse() {
        Node operator = getNode('+', '-');
        if (operator != null) {
            return operator;
        }
        operator = getNode('*', '/');
        if (operator != null) {
            return operator;
        }
        if (text.indexOf('(') == -1 || isStartAndEndWidthBrackets(text)) {
            StaticNode staticNode = new StaticNode();
            staticNode.setText(text);
            staticNode.setEnv(env);
            return staticNode;
        }
        FunctionNode functionNode = new FunctionNode();
        functionNode.setFunctionMap(functionMap);
        functionNode.setEnv(env);
        functionNode.setText(text);
        return functionNode;
    }

    private Node getNode(char opt1, char opt2) {
        int length = text.length();
        int brackets = 0;
        boolean isDataNode = true;
        for (int i = 0; i < length; i++) {
            char charAt = text.charAt(i);
            if (isDataNode && (charAt > '9' || charAt < '0')) {
                isDataNode = false;
            }
            if (charAt == '(') {
                brackets++;
            }
            if (charAt == ')') {
                brackets--;
            }
            if (charAt == opt1 || charAt == opt2) {
                if (brackets == 0) {
                    Expression left = new Expression();
                    left.setText(text.substring(0, i).trim());
                    left.setFunctionMap(functionMap);
                    left.setEnv(env);
                    Operator operator = Operator.valueOf(String.valueOf(charAt));
                    operator.setLeft(left);
                    Expression right = new Expression();
                    right.setText(text.substring(i + 1).trim());
                    right.setFunctionMap(functionMap);
                    right.setEnv(env);
                    operator.setRight(right);
                    operator.parse();
                    return operator;
                }
            }
        }
        if (length > 0 && isDataNode) {
            DataNode expression = new DataNode();
            expression.setText(text);
            return expression;
        }
        return null;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        String trim = text.trim();
        if (isStartAndEndWidthBrackets(trim)) {
            trim = trim.substring(1, trim.length() - 1);
        }
        this.text = trim;
    }

    private boolean isStartAndEndWidthBrackets(String text) {
        if (text.charAt(0) == '(') {
            int length = text.length();
            int brackets = 0;
            for (int i = 0; i < length; i++) {
                char charAt = text.charAt(i);
                if (charAt == '(') {
                    brackets++;
                }
                if (charAt == ')') {
                    brackets--;
                    if (brackets == 0) {
                        return i == length - 1;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Object getValue() {
        throw new RuntimeException("not support!");
    }

    public void setEnv(Map<String, Object> env) {
        this.env = env;
    }

    public void setFunctionMap(Map<String, IFunction> functionMap) {
        this.functionMap = functionMap;
    }
}
