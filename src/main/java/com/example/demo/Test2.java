package com.example.demo;

import java.util.concurrent.TimeUnit;

/**
 * @author wangxing
 * @date 2021/7/2
 **/
public class Test2 {
    private static boolean FLAG = false;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            while (!FLAG){
//                System.out.println(FLAG);
            }
            System.out.println(FLAG);
        },"t1");
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        FLAG = true;
    }
}
