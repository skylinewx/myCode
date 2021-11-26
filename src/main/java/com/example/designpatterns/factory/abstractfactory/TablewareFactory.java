package com.example.designpatterns.factory.abstractfactory;

/**
 * 餐具工厂
 *
 * @author wangxing
 */
public interface TablewareFactory {

    /**
     * 生产筷子
     *
     * @param user 筷子的使用者
     * @return 筷子
     */
    Chopsticks createChopsticks(String user);

    /**
     * 生产碗
     *
     * @param user 碗的使用者
     * @return
     */
    Bowl createBowl(String user);

    /**
     * 生产盘子
     *
     * @param user 盘子的使用者
     * @return
     */
    Plate createPlate(String user);
}
