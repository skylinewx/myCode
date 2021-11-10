package com.example.designpatterns.strategy;

public class SortUtil {

    /**
     * 给整型数组排序
     *
     * @param objs
     */
    public static <T> void sort(T[] objs, ISortor<T> sortAble) {
        for (int i = 0; i < objs.length; i++) {
            for (int j = i + 1; j < objs.length; j++) {
                T o1 = objs[i];
                T o2 = objs[j];
                int compareResult = sortAble.compareTo(o1, o2);
                if (compareResult > 0) {
                    objs[j] = o1;
                    objs[i] = o2;
                }
            }
        }
    }
}
