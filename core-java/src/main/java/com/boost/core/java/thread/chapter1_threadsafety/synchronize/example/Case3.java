package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;

import java.util.concurrent.TimeUnit;

/**
 * 对写加锁，对读不加锁
 */
public class Case3 {

    String name;
    double balance;

    public synchronized void set(String name, double balance){
        this.name = name;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.balance = balance;
    }

    public /*synchronized*/ double getBalance(String name) {
        return balance;
    }

    /**
     * print result:
     * 0.0
     * 100.0
     *
     * 和case2场景差不多，锁定和非锁定代码块的同时执行
     * 在执行set的时候，分别给name和balance赋值，在给name
     * 赋值完之后，休眠了2秒钟，这时候balance还没有赋值
     * 这时候再去调用getBalance返回的就是一个还没有被赋值
     * 的balance，也就是脏读
     */
    public static void main(String[] args) {
        Case3 c = new Case3();
        new Thread(() -> c.set("Depp", 100)).start();

        try {
            // 休眠1秒
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(c.getBalance("Depp"));

        try {
            // 休眠2秒
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(c.getBalance("Depp"));

    }

}
