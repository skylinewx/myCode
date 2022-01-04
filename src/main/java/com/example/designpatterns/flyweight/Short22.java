package com.example.designpatterns.flyweight;

/**
 * .22 Short
 *
 * @author skyline
 */
public class Short22 extends BulletSpecification {
    @Override
    public double getCaliber() {
        return 5.59;
    }

    @Override
    public double getLength() {
        return 11;
    }

    @Override
    public String getShape() {
        return "Rim,S";
    }
}
