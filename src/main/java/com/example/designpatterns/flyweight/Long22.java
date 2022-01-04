package com.example.designpatterns.flyweight;

/**
 * .22 Long
 *
 * @author skyline
 */
public class Long22 extends BulletSpecification {
    @Override
    public double getCaliber() {
        return 5.59;
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
