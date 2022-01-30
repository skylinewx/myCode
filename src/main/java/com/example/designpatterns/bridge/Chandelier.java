package com.example.designpatterns.bridge;

/**
 * 吊灯
 * @author skyline
 */
public class Chandelier extends AbstractLight{
    protected Chandelier(ILightTube lightTube) {
        super(lightTube);
    }
}
