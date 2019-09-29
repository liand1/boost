package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;

import java.util.concurrent.TimeUnit;

/**
 * 一个同步方法调用另外一个同步方法
 * 一个线程已经拥有某个对象的锁，在此申请的时候仍然会得到该对象的锁，也就是说synchronied获得的锁是可重入的
 */
public class Case4 {

    synchronized void m1() {
        System.out.println("m1 start");

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m2();
    }

    synchronized void m2() {

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("m2");
    }

    /**
     * m1和m2加了同步,在执行m1的时候已经申请了一把锁,在m1里面调用了m2方法
     * 这时候返回的这把锁还是之前所持有的
     * @param args
     */
    public static void main(String[] args) {

    }
}
