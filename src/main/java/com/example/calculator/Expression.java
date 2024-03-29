package com.example.calculator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 表达式节点
 */
public class Expression implements Node {
    Map<String, Object> env;
    Map<String, IFunction> functionMap;
    private String text;

    @Override
    public Node parse() {
        Collection<List<Character>> sortList = Operator.getSortList();
        for (List<Character> characters : sortList) {
            Node operator = getNode(characters);
            if (operator != null) {
                return operator;
            }
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

    private Node getNode(List<Character> opts) {
        int length = text.length();
        int brackets = 0;
        boolean isDataNode = true;
        int i = length - 1;
        try {
            for (; i >= 0; i--) {
                char charAt = text.charAt(i);
                if (isDataNode && (charAt > '9' || charAt < '0')) {
                    isDataNode = false;
                }
                if (charAt == ')') {
                    brackets++;
                }
                if (charAt == '(') {
                    brackets--;
                }
                if (opts.contains(charAt)) {
                    if (brackets == 0) {
                        String leftExp = text.substring(0, i).trim();
                        Expression right = new Expression();
                        right.setText(text.substring(i + 1).trim());
                        right.setFunctionMap(functionMap);
                        right.setEnv(env);
                        if ("".equals(leftExp) && charAt == '-') {
                            NegativeNode negativeNode = new NegativeNode(right);
                            return negativeNode.parse();
                        } else {
                            Expression left = new Expression();
                            left.setText(leftExp);
                            left.setFunctionMap(functionMap);
                            left.setEnv(env);
                            Operator operator = Operator.valueOf(charAt);
                            operator.setLeft(left);
                            operator.setRight(right);
                            return operator.parse();
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("在index=[" + (i + 1) + "]处发现语法异常！");
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
    public void accept(NodeVisitor nodeVisitor) {
        throw new RuntimeException("not support!");
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
