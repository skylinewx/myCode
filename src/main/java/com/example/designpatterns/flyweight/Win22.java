package com.example.designpatterns.flyweight;

/**
 * .22 Win
 *
 * @author skyline
 */
public class Win22 extends BulletSpecification {
    @Override
    public double getCaliber() {
        return 5.59;
    }

    @Override
    public double getLength() {
        return 27;
    }

    @Override
    public String getShape() {
        return "Rim,S";
    }
}
