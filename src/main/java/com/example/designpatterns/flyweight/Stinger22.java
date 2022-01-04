package com.example.designpatterns.flyweight;

/**
 * .22 Stinger
 *
 * @author skyline
 */
public class Stinger22 extends BulletSpecification {
    @Override
    public double getCaliber() {
        return 5.59;
    }

    @Override
    public double getLength() {
        return 18;
    }

    @Override
    public String getShape() {
        return "Rim,S";
    }
}
