package com.example.designpatterns.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * 母亲做饭
 * @author skyline
 */
public class MotherMealBuilder extends BaseMealBuilder{

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
        return "米饭";
    }

    @Override
    protected String getChef() {
        return "母亲";
    }

    @Override
    protected String getSoup() {
        return "鸡蛋汤";
    }

    @Override
    protected List<String> getDishs() {
        List<String> dishs = new ArrayList<>();
        dishs.add("肉末豆角");
        dishs.add("酸辣土豆丝");
        return dishs;
    }
}
