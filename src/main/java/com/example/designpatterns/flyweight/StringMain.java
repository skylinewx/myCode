package com.example.designpatterns.flyweight;

/**
 * 字符串常量池
 */
public class StringMain {
    public static void main(String[] args) {
        String str1 = new String("a");
        String str2 = "a";
        String str3 = str1.intern();
        String str4 = new String("a").intern();
        System.out.println(str1 == str2);
        System.out.println(str2 == str3);
        System.out.println(str3 == str4);
        System.out.println(str4 == str1);
    }
}
