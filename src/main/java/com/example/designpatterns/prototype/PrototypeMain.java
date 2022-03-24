package com.example.designpatterns.prototype;

/**
 * 原型模式
 */
public class PrototypeMain {
    public static void main(String[] args) throws CloneNotSupportedException {
        PrototyObj prototyObj = new PrototyObj();
        prototyObj.intVal=88;
        prototyObj.str="88";
        MyObj myObj = new MyObj();
        myObj.code="zhangsan";
        myObj.title="张三";
        prototyObj.myObj=myObj;
        PrototyObj cloneObj = prototyObj.clone();
        System.out.println("(cloneObj==prototyObj) = " + (cloneObj == prototyObj));
        System.out.println("(cloneObj.myObj==prototyObj.myObj) = " + (cloneObj.myObj == prototyObj.myObj));
        prototyObj.myObj.code="lisi";
        prototyObj.myObj.title="李四";
        System.out.println("cloneObj.myObj.code = " + cloneObj.myObj.code);
        System.out.println("cloneObj.myObj.title = " + cloneObj.myObj.title);
    }
}
