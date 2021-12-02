package com.example.designpatterns.mediator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 卡车
 *
 * @author skyline
 */
public class Truck extends Car {

    Logger logger = LoggerFactory.getLogger(Truck.class);

    public Truck(Direction direction, SignalLamp signalLamp) {
        super(direction, signalLamp);
    }

    @Override
    public void go() {
        if (signalLamp.canGo(direction)) {
            logger.info("[{}]gogogo", this);
        } else {
            logger.info("[{}]can not go", this);
        }
    }

    @Override
    public String toString() {
        return "Truck{" +
                "direction=" + direction +
                ", signalLamp=" + signalLamp +
                "} ";
    }
}
