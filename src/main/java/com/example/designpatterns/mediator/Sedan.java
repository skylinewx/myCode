package com.example.designpatterns.mediator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 小汽车
 *
 * @author skyline
 */
public class Sedan extends Car {

    Logger logger = LoggerFactory.getLogger(Sedan.class);

    public Sedan(Direction direction, SignalLamp signalLamp) {
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
        return "Sedan{" +
                "direction=" + direction +
                ", signalLamp=" + signalLamp +
                "} ";
    }
}
