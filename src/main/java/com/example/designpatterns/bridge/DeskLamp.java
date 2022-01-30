package com.example.designpatterns.bridge;

/**
 * 台灯
 * @author skyline
 */
public class DeskLamp extends AbstractLight{
    protected DeskLamp(ILightTube lightTube) {
        super(lightTube);
    }
}
