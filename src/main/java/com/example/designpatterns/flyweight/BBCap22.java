package com.example.designpatterns.flyweight;

/**
 * .22 BB Cap
 *
 * @author skyline
 */
public class BBCap22 extends BulletSpecification {
    @Override
    public double getCaliber() {
        return 5.59;
    }

    @Override
    public double getLength() {
        return 7;
    }

    @Override
    public String getShape() {
        return "Rim,S";
    }
}
