package com.example.calculator;

import java.util.List;
import java.util.Map;

/**
 * 扩展的函数接口
 */
public interface IFunction {

    /**
     * 具体的函数执行逻辑
     * @param params
     * @param env
     * @return
     */
    Object exec(Object[] params, Map<String, Object> env);

    /**
     * 函数名
     * @return
     */
    String getName();

    /**
     * 函数参数
     * @return
     */
    List<FunctionParam> getParams();
}
