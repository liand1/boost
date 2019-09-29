package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;

/**
 * 加锁和不加锁的方法一起执行
 */
public class Case2 {

    public synchronized void m1() {
        System.out.println(Thread.currentThread().getName() + " m1 start...");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " m1 end...");
    }

    public void m2() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " m2...");
    }

    /**
     * result :
     * t1 m1 start...
     * t2 m2...
     * t1 m1 end...
     * 执行m2不需要申请锁
     */
    public static void main(String[] args) {
        Case2 c = new Case2();

        new Thread(() -> c.m1(), "t1").start();
        new Thread(() -> c.m2(), "t2").start();

//        new Thread(c::m1, "t1").start();
//        new Thread(c::m2, "t2").start();
    }
}
