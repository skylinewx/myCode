package com.example.designpatterns.bridge;

/**
 * 桥接模式，灯与灯管
 * @author skyline
 */
public class BridgeMain {
    public static void main(String[] args) {
        //led台灯
        DeskLamp ledDeskLamp = new DeskLamp(new LEDTube());
        ledDeskLamp.shine();
        //普通台灯
        DeskLamp ordinaryDeskLamp = new DeskLamp(new FluorescentTube());
        ordinaryDeskLamp.shine();
        //led吊灯
        Chandelier ledChandelier = new Chandelier(new LEDTube());
        ledChandelier.shine();
    }
}
