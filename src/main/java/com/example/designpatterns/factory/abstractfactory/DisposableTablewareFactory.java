package com.example.designpatterns.factory.abstractfactory;

/**
 * 一次性餐具工厂
 *
 * @author wangxing
 */
public class DisposableTablewareFactory implements TablewareFactory {
    @Override
    public Chopsticks createChopsticks(String user) {
        return new DisposableChopsticks();
    }

    @Override
    public Bowl createBowl(String user) {
        return new DisposableBowl();
    }

    @Override
    public Plate createPlate(String user) {
        return new DisposablePlate();
    }
}
