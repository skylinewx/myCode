package com.example.designpatterns.iterator;

/**
 * 可迭代的
 * @param <E>
 */
public interface Iterable<E> {

    /**
     * 获取一个迭代器
     * @return
     */
    Iterator<E> getIterator();
}
