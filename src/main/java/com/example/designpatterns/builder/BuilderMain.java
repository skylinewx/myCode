package com.example.designpatterns.builder;

/**
 * 建造者模式
 *
 * @author skyline
 */
public class BuilderMain {
    public static void main(String[] args) {
        Meal meal = new SimpleBuilder("me",2, "晚饭", "面条")
                .buildSoup("紫菜汤").buildDish("麻婆豆腐")
                .build();
        System.out.println(meal);
        Baby baby = new Baby();
        for (int i = 0; i < 7; i++) {
            System.out.print("第"+(i+1)+"天");
            if (i%2==0) {
                baby.wantEat(new FatherMealBuilder());
            }else {
                baby.wantEat(new MotherMealBuilder());
            }
            System.out.println(baby.getMeal());
        }
    }
}
