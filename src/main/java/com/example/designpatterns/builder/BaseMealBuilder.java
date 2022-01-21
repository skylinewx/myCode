package com.example.designpatterns.builder;

import java.util.List;

/**
 * 基础的builder
 * @author skyline
 */
public abstract class BaseMealBuilder {
    private int userCount;
    private String mealType;
    private String stapleFood;
    private String chef;
    private String soup;
    private List<String> dishs;

    /**
     * 创建厨师
     * @return
     */
    public BaseMealBuilder buildChef(){
        this.chef = getChef();
        return this;
    }

    /**
     * 创建用餐人数
     * @return
     */
    public BaseMealBuilder buildUserCount(){
        this.userCount = getUserCount();
        return this;
    }

    /**
     * 创建饭类型
     * @return
     */
    public BaseMealBuilder buildMealType(){
        this.mealType = getMealType();
        return this;
    }

    /**
     * 创建主食
     * @return
     */
    public BaseMealBuilder buildStapleFood(){
        this.stapleFood = getStapleFood();
        return this;
    }

    /**
     * 创建汤
     * @return
     */
    public BaseMealBuilder buildSoup(){
        this.soup = getSoup();
        return this;
    }

    /**
     * 创建菜品
     * @return
     */
    public BaseMealBuilder buildDishs(){
        this.dishs = getDishs();
        return this;
    }

    /**
     * 获取用餐人数
     * @return
     */
    protected abstract int getUserCount();

    /**
     * 获取饭类型
     * @return
     */
    protected abstract String getMealType();

    /**
     * 获取主食
     * @return
     */
    protected abstract String getStapleFood();

    /**
     * 获取厨师信息
     * @return
     */
    protected abstract String getChef();

    /**
     * 获取汤
     * @return
     */
    protected abstract String getSoup();

    /**
     * 获取菜品
     * @return
     */
    protected abstract List<String> getDishs();

    public final Meal build(){
        if(chef==null){
            throw new RuntimeException("厨师不能为空");
        }
        if(userCount==0){
            throw new RuntimeException("用餐人数不能是0");
        }
        if(mealType==null){
            throw new RuntimeException("饭类型不能为空");
        }
        if(stapleFood==null){
            throw new RuntimeException("主食不能为空");
        }
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
