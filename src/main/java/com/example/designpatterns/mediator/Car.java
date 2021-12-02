package com.example.designpatterns.mediator;

/**
 * 汽车
 *
 * @author skyline
 */
public abstract class Car {
    Direction direction;
    SignalLamp signalLamp;

    Car(Direction direction, SignalLamp signalLamp) {
        this.direction = direction;
        this.signalLamp = signalLamp;
    }

    public abstract void go();
}
