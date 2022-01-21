package com.example.designpatterns.builder;

import java.util.List;

/**
 * 一顿饭
 */
public class Meal {
    private String chef;
    private int userCount;
    private String mealType;
    private String stapleFood;
    private String soup;
    private List<String> dishs;

    public String getChef() {
        return chef;
    }

    public void setChef(String chef) {
        this.chef = chef;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getStapleFood() {
        return stapleFood;
    }

    public void setStapleFood(String stapleFood) {
        this.stapleFood = stapleFood;
    }

    public String getSoup() {
        return soup;
    }

    public void setSoup(String soup) {
        this.soup = soup;
    }

    public List<String> getDishs() {
        return dishs;
    }

    public void setDishs(List<String> dishs) {
        this.dishs = dishs;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "厨师='" + chef + '\'' +
                ", 用餐人数=" + userCount +
                ", 类型='" + mealType + '\'' +
                ", 主食='" + stapleFood + '\'' +
                ", 汤='" + soup + '\'' +
                ", 菜品=" + dishs +
                '}';
    }
}
