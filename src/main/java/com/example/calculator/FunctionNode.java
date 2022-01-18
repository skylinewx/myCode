package com.example.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 函数节点
 */
public class FunctionNode extends Expression {

    private String funcName;
    private List<Node> params;

    @Override
    public String getText() {
        return super.getText();
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        String txt = getText();
        int index = txt.indexOf('(');
        funcName = txt.substring(0, index).toUpperCase(Locale.ROOT);
        params = new ArrayList<>();
        String substring = txt.substring(index + 1, txt.length() - 1);
        int length = substring.length();
        int brackets = 0;
        int begin = 0;
        for (int i = 0; i < length; i++) {
            char charAt = substring.charAt(i);
            if (charAt == '(') {
                brackets++;
            }
            if (charAt == ')') {
                brackets--;
            }
            if (charAt == ',') {
                if (brackets == 0) {
                    Expression expression = new Expression();
                    expression.setFunctionMap(functionMap);
                    expression.setText(substring.substring(begin, i));
                    expression.setEnv(env);
                    Node parse = expression.parse();
                    params.add(parse);
                    begin = i + 1;
                }
            }
        }
        if (begin != length) {
            Expression expression = new Expression();
            expression.setFunctionMap(functionMap);
            expression.setText(substring.substring(begin));
            expression.setEnv(env);
            Node parse = expression.parse();
            params.add(parse);
        }
    }

    @Override
    public Object getValue() {
        IFunction iFunction = functionMap.get(funcName);
        int size = params.size();
        Object[] paramVals = new Object[size];
        for (int i = 0; i < size; i++) {
            paramVals[i] = params.get(i).getValue();
        }
        return iFunction.exec(paramVals, env);
    }
}
