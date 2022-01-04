package com.example.designpatterns.flyweight;

/**
 * 子弹规格
 *
 * @author skyline
 */
public abstract class BulletSpecification {

    /**
     * 口径，mm
     *
     * @return
     */
    public abstract double getCaliber();

    /**
     * 长度，mm
     *
     * @return
     */
    public abstract double getLength();

    /**
     * 形状
     *
     * @return
     */
    public abstract String getShape();

    @Override
    public String toString() {
        return "Type:" + this.getClass().getSimpleName() + ",Caliber:" + getCaliber() + ",Length:" + getLength() + ",Shape:" + getShape();
    }
}
