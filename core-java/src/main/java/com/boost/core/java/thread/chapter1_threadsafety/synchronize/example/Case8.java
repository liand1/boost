package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;

import java.util.concurrent.TimeUnit;

public class Case8 {

    Object o = new Object();

    void m() {
        synchronized (o) {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            }
        }
    }

    // 锁的信息是记录在堆内存中的，不是在栈中，所以我们可以知道锁的是对象
    // 当执行第一个线程的时候，锁的是第一个Object, 执行了里面的四循环
    // 在执行第二个线程的时候，o指向了一个新的Object，这时候这个新的Object还没有被锁定，第二个线程就可以
    // 执行m方法
    public static void main(String[] args) {
        Case8 t = new Case8();
        //启动第一个线程
        new Thread(t::m, "t1").start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //创建第二个线程
        Thread t2 = new Thread(t::m, "t2");
        t.o = new Object(); //锁对象发生改变，所以t2线程得以执行，如果注释掉这句话，线程2将永远得不到执行机会
        t2.start();
    }

}
