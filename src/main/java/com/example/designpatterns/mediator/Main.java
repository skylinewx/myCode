package com.example.designpatterns.mediator;

public class Main {
    public static void main(String[] args) {
        TrafficLights trafficLights = new TrafficLights();
        trafficLights.setStatus(0);
        Bus bus = new Bus(Direction.EW, trafficLights);
        Sedan sedan = new Sedan(Direction.SN, trafficLights);
        Truck truck = new Truck(Direction.EW, trafficLights);

        bus.go();
        sedan.go();
        truck.go();

    }
}
