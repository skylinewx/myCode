package com.example.designpatterns.composite;

import java.util.List;

/**
 * 组合模式
 *
 * @author skyline
 */
public class CompositeMain {
    public static void main(String[] args) {
        CalculateAble order = new Order("order");
        CalculateAble smallOrder = new Order("smallOrder");
        smallOrder.add(new Apple());
        smallOrder.add(new Tea());
        CalculateAble bigOrder = new Order("bigOrder");
        bigOrder.add(new AlienWareNoteBook());
        order.add(smallOrder);
        order.add(bigOrder);
        order.add(new Milk());
        loop(order, 0);
        double calculate = order.calculate();
        System.out.println("总价为：" + calculate);
    }

    private static void loop(CalculateAble calculateAble, int level) {
        if (level == 0) {

        } else if (level == 1) {
            System.out.print("├─");
        } else {
            for (int i = 0; i < level - 1; i++) {
                System.out.print("│ ");
            }
            System.out.print("├─");
        }
        System.out.println(calculateAble.name());
        List<CalculateAble> calculateAbleList = calculateAble.getCalculateAbleList();
        if (calculateAbleList != null) {
            for (CalculateAble able : calculateAbleList) {
                loop(able, level + 1);
            }
        }
    }
}
