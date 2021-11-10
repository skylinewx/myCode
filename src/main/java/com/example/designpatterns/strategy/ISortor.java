package com.example.designpatterns.strategy;

/**
 * @author w123x
 */
public interface ISortor<T> {
    /**
     * @param o1 参与比较的对象
     * @param o2 参与比较的对象
     * @return <0代表o1<o2;=0代表o1=o2;>0代表o1>02
     */
    int compareTo(T o1, T o2);
}
