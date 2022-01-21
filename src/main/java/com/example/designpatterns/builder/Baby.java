package com.example.designpatterns.builder;

/**
 * 小宝宝
 * @author skyline
 */
public class Baby {
    private BaseMealBuilder mealBuilder;
    public void wantEat(BaseMealBuilder mealBuilder){
        mealBuilder.buildChef()
                .buildDishs()
                .buildMealType()
                .buildSoup()
                .buildStapleFood()
                .buildUserCount();
        this.mealBuilder = mealBuilder;
    }

    public Meal getMeal(){
        return this.mealBuilder.build();
    }
}
