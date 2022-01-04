package com.example.designpatterns.flyweight;

/**
 * .22 LR
 *
 * @author skyline
 */
public class LR22 extends BulletSpecification {
    @Override
    public double getCaliber() {
        return 5.7;
    }

    @Override
    public double getLength() {
        return 15;
    }

    @Override
    public String getShape() {
        return "Rim,S";
    }
}
