package com.example.gc;


import java.util.ArrayList;
import java.util.List;

/**
 * -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+UseG1GC -XX:+PrintGCDateStamps
 */
public class G1Test {
    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>(1);
        while (true){
            byte[] bytes = new byte[1024*100];
            list.add(bytes);
        }
    }
}
