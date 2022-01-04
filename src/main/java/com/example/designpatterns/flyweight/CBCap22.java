package com.example.designpatterns.flyweight;

/**
 * .22 CB Cap
 *
 * @author skyline
 */
public class CBCap22 extends BulletSpecification {
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
