package com.example.designpatterns.prototype;

public class PrototyObj implements Cloneable {
    public String str;
    public Integer intVal;
    public MyObj myObj;

    @Override
    public PrototyObj clone() throws CloneNotSupportedException {
        PrototyObj clone = (PrototyObj) super.clone();
        clone.myObj = myObj.clone();
        return clone;
    }
}
