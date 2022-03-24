package com.example.designpatterns.prototype;

public class ComplexObj implements Cloneable {
    public String a;
    public String b;
    public String c;
    public String d;
    public String e;
    public String f;
    public String g;
    public String h;
    public String i;
    public String j;
    public String k;
    public String l;
    public String m;
    public String n;
    public String o;
    public String p;
    public String q;
    private String id;


    public ComplexObj(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public ComplexObj clone() throws CloneNotSupportedException {
        return (ComplexObj) super.clone();
    }
}
