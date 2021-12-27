package com.example.jmh;

import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;

@Component("arrayQueue")
public class ArrayQueue implements IQueue {
    private static final ArrayBlockingQueue<Object> QUEUE = new ArrayBlockingQueue<>(100000);

    @Override
    public void put(Object o) throws InterruptedException {
        QUEUE.put(o);
    }

    @Override
    public Object take() throws InterruptedException {
        return QUEUE.take();
    }
}
