package com.example.designpatterns.bridge;

/**
 * 路灯
 * @author skyline
 */
public class StreetLight extends AbstractLight{
    protected StreetLight(ILightTube lightTube) {
        super(lightTube);
    }
}
