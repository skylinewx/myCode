package com.example.designpatterns.iterator;

/**
 * 迭代器
 * @author skyline
 * @param <E>
 */
public interface Iterator<E> {

    /**
     * 是否有下一个
     * @return
     */
    boolean hasNext();

    /**
     * 下一个
     * @return
     */
    E next();
}
