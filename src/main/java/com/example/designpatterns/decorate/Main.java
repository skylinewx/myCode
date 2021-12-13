package com.example.designpatterns.decorate;

/**
 * @author wangxing
 * @date 2021/12/13
 **/
public class Main {
    public static void main(String[] args) {
        System.out.println("来一份摩卡");
        MaterialAble mocha = createMocha();
        System.out.println("摩卡的成分列表：" + mocha.getMaterialList());
        System.out.println("摩卡的价格：" + mocha.calcCost());
        System.out.println("来一份高乐高");
        MaterialAble hotGaoLego = createHotGaoLego();
        System.out.println("高乐高的成分列表：" + hotGaoLego.getMaterialList());
        System.out.println("高乐高的价格：" + hotGaoLego.calcCost());
    }

    private static MaterialAble createHotGaoLego() {
        MaterialAble water = new Water(null);
        MaterialAble chocolate = new Chocolate(water);
        MaterialAble gaoLego = new Milk(chocolate);
        return gaoLego;
    }

    private static MaterialAble createMocha() {
        MaterialAble water = new Water(null);
        MaterialAble coffee = new Coffee(water);
        MaterialAble coffee2 = new Coffee(coffee);
        MaterialAble mocha = new Chocolate(coffee2);
        return mocha;
    }
}
