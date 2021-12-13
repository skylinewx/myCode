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
        //首先，来一杯水
        MaterialAble gaoLego = new Water(null);
        //然后放点巧克力
        gaoLego = new Chocolate(gaoLego);
        //再来点牛奶
        gaoLego = new Milk(gaoLego);
        return gaoLego;
    }

    private static MaterialAble createMocha() {
        //首先，来一杯水
        MaterialAble mocha = new Water(null);
        //加入2份咖啡
        mocha = new Coffee(mocha);
        mocha = new Coffee(mocha);
        //再放入巧克力
        mocha = new Chocolate(mocha);
        return mocha;
    }
}
