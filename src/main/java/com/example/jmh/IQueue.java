package com.example.jmh;

public interface IQueue {
    void put(Object o) throws InterruptedException;

    Object take() throws InterruptedException;
}
