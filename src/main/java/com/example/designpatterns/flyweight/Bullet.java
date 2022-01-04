package com.example.designpatterns.flyweight;

import java.util.UUID;

/**
 * 子弹
 *
 * @author skyline
 */
public class Bullet {
    private final String id;
    private final BulletSpecification specification;

    public Bullet(BulletSpecification specification) {
        this.id = UUID.randomUUID().toString();
        this.specification = specification;
    }

    public String getId() {
        return id;
    }

    public BulletSpecification getSpecification() {
        return specification;
    }

    @Override
    public String toString() {
        return "Bullet{" +
                "id='" + id + '\'' +
                ", specification=" + specification +
                '}';
    }
}
