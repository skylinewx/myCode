package com.example.designpatterns.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * 父亲做饭
 * @author skyline
 */
public class FatherMealBuilder extends BaseMealBuilder{

    @Override
    protected int getUserCount() {
        return 3;
    }

    @Override
    protected String getMealType() {
        return "晚餐";
    }

    @Override
    protected String getStapleFood() {
        return "饼";
    }

    @Override
    protected String getChef() {
        return "父亲";
    }

    @Override
    protected String getSoup() {
        return "丸子汤";
    }

    @Override
    protected List<String> getDishs() {
        List<String> dishs = new ArrayList<>();
        dishs.add("鸡蛋西红柿");
        return dishs;
    }
}
