package com.example.jmh;

/**
 * 队列
 *
 * @author wangxing
 */
public interface IQueue {
    /**
     * 放入数据
     *
     * @param o
     * @throws InterruptedException
     */
    void put(Object o) throws InterruptedException;

    /**
     * 取出数据
     *
     * @return
     * @throws InterruptedException
     */
    Object take() throws InterruptedException;
}
