package com.example.designpatterns.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * 子弹工厂
 *
 * @author skyline
 */
public class BulletFactory {
    private final Map<Class<? extends BulletSpecification>, BulletSpecification> specificationMap = new HashMap<>();

    public Bullet createBullet(Class<? extends BulletSpecification> specificationClazz) {
        BulletSpecification bulletSpecification = specificationMap.computeIfAbsent(specificationClazz, key -> {
            try {
                return key.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        });
        return new Bullet(bulletSpecification);
    }
}
