package com.example.designpatterns.builder;

import java.util.ArrayList;
import java.util.List;

public class SimpleBuilder {
    private int userCount;
    private String mealType;
    private String stapleFood;
    private String chef;
    private String soup;
    private List<String> dishs;

    public SimpleBuilder(String chef,int userCount,String mealType,String stapleFood) {
        this.chef = chef;
        this.userCount = userCount;
        this.mealType = mealType;
        this.stapleFood = stapleFood;
    }

    public SimpleBuilder buildSoup(String soup) {
        this.soup = soup;
        return this;
    }

    public SimpleBuilder buildDish(String dish) {
        if (this.dishs == null) {
            this.dishs = new ArrayList<>();
        }
        this.dishs.add(dish);
        return this;
    }

    public Meal build(){
        Meal meal = new Meal();
        meal.setChef(chef);
        meal.setUserCount(userCount);
        meal.setSoup(soup);
        meal.setMealType(mealType);
        meal.setStapleFood(stapleFood);
        meal.setDishs(dishs);
        return meal;
    }
}
