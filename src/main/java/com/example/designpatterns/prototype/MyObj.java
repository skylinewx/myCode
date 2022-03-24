package com.example.designpatterns.prototype;

public class MyObj implements Cloneable {
    public String code;
    public String title;

    @Override
    public MyObj clone() throws CloneNotSupportedException {
        return (MyObj) super.clone();
    }
}
