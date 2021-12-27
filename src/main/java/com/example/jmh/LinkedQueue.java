package com.example.jmh;

import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

@Component("linkedQueue")
public class LinkedQueue implements IQueue {
    private static final LinkedBlockingQueue<Object> QUEUE = new LinkedBlockingQueue<>(100000);

    @Override
    public void put(Object o) throws InterruptedException {
        QUEUE.put(o);
    }

    @Override
    public Object take() throws InterruptedException {
        return QUEUE.take();
    }
}
