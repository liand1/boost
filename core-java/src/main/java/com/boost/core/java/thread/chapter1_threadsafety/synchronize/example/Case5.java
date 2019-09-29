package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;

import java.util.concurrent.TimeUnit;

/**
 * 一个同步方法调用另外一个同步方法
 * 一个线程已经拥有某个对象的锁，在此申请的时候仍然会得到该对象的锁，也就是说synchronized获得的锁是可重入的
 * 这里是继承中，子类调用父类的同步方法，也是可重入的
 */
public class Case5 {

    synchronized void m() {
        System.out.println("m start");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("m end");
    }

    public static void main(String[] args) {
        new SubCase5().m();
    }
}

class SubCase5 extends Case5 {
    @Override
    synchronized void m() {
        System.out.println("child m start");
        super.m();
        System.out.println("child m end");
    }
}
