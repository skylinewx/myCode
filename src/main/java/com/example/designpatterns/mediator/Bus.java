package com.example.designpatterns.mediator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author skyline
 */
public class Bus extends Car {

    Logger logger = LoggerFactory.getLogger(Bus.class);

    public Bus(Direction direction, SignalLamp signalLamp) {
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
        return "Bus{" +
                "direction=" + direction +
                ", signalLamp=" + signalLamp +
                "} ";
    }
}
