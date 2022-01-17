package com.example.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 给一个整数加100的函数
 */
public class Plus100 implements IFunction{

    @Override
    public Object exec(Object[] params, Map<String, Object> env) {
        Object param = params[0];
        return (Integer) param + 100;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public List<FunctionParam> getParams() {
        List<FunctionParam> params = new ArrayList<>();
        FunctionParam param1 = new FunctionParam();
        param1.setName("p1");
        param1.setType(Integer.class);
        params.add(param1);
        return params;
    }
}
