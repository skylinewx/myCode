package com.example.designpatterns.flyweight;

import java.util.ArrayList;
import java.util.List;

/**
 * 享元模式，共享元数据信息，节省内存消耗才是重点。同时享元模式也有池化的思想，对象池。<br/>
 * 同时，享元模式因为需要封装new方法，所以一般会和工厂模式一起用
 * -Xmx20M -Xms20M
 *
 * @author skyline
 */
public class FlyWeightMain {
    public static void main(String[] args) {
        BulletFactory factory = new BulletFactory();
        Class<? extends BulletSpecification>[] types = new Class[]{BBCap22.class, CBCap22.class, Long22.class, LR22.class, Short22.class, Win22.class, Stinger22.class};
        List<Bullet> bullets = new ArrayList<>();
        for (Class<? extends BulletSpecification> type : types) {
            for (int i = 0; i < 100000; i++) {
                Bullet bullet = factory.createBullet(type);
                bullets.add(bullet);
                System.out.println(bullet.toString());
            }
        }
    }
}
