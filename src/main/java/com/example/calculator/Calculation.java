package com.example.calculator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Calculation {

    static Map<String, IFunction> functionMap;
    {
        functionMap = new HashMap<>();
        Max max = new Max();
        functionMap.put(max.getName().toUpperCase(Locale.ROOT), max);
        Plus100 plus100 = new Plus100();
        functionMap.put(plus100.getName().toUpperCase(Locale.ROOT), plus100);
    }

    public Object exec(String exp) {
        Expression root = new Expression();
        root.setText(exp);
        root.setFunctionMap(functionMap);
        Node parse = root.parse();
        return parse.getValue();
    }

}
