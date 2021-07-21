package com.example.juc;

import java.util.concurrent.CountDownLatch;

public class MultiThreadDemo {

    public static void main(String[] args) {
        Printer printer = new Printer();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread t1 = new Thread(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printer.print("a", 1, 2);
        });
        Thread t2 = new Thread(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printer.print("b", 2, 3);
        });
        Thread t3 = new Thread(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printer.print("c", 3, 1);
        });
        t1.start();
        t2.start();
        t3.start();
        countDownLatch.countDown();
    }

    static class Printer{
        private int current = 1;
        private final Object lockObj = new Object();
        public void print(String str,int signal,int nextSignal){
            for (int i = 0; i < 4; i++) {
                synchronized (lockObj){
                    while(current!=signal){
                        try {
                            lockObj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print(str);
                    current = nextSignal;
                    lockObj.notifyAll();
                }
            }
        }
    }
}
