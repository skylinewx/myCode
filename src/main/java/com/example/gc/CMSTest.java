package com.example.gc;


import java.util.ArrayList;
import java.util.List;

/**
 * -Xms10m -Xmn1m -Xmx10m -XX:+PrintGCDetails -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=40 -XX:+UseCMSInitiatingOccupancyOnly
 */
public class CMSTest {
    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>();
        while (true){
            byte[] bytes = new byte[1024*100];
            list.add(bytes);
        }
    }
}
