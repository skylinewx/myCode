package com.example.calculator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 计算器
 *
 * @author skyline
 */
public class Calculation {

    private static final Map<String, IFunction> FUNCTION_MAP = new HashMap<>();

    static {
        ServiceLoader<IFunction> load = ServiceLoader.load(IFunction.class);
        for (IFunction iFunction : load) {
            FUNCTION_MAP.put(iFunction.getName().toUpperCase(Locale.ROOT), iFunction);
        }
    }

    public static Object exec(String exp) {
        Expression root = new Expression();
        root.setText(exp);
        root.setFunctionMap(FUNCTION_MAP);
        Node parse = root.parse();
        return parse.getValue();
    }

}
