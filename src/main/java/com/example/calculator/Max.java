package com.example.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 求两个整数的较大数的函数
 */
public class Max implements IFunction{

    @Override
    public Object exec(Object[] params, Map<String, Object> env) {
        return Math.max((Integer) params[0], (Integer) params[1]);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public List<FunctionParam> getParams() {
        List<FunctionParam> params = new ArrayList<>();
        FunctionParam param1 = new FunctionParam();
        param1.setName("left");
        param1.setType(Integer.class);
        FunctionParam param2 = new FunctionParam();
        param2.setName("right");
        param2.setType(Integer.class);
        params.add(param1);
        params.add(param2);
        return params;
    }
}
