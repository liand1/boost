package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;

import java.util.concurrent.TimeUnit;

/**
 * 同步不具有继承性，重写父类方法的时候，如果父类加了synchronized但是子类没有加的话，是不会有同步效果的
 */
public class Case9 {

    public synchronized void m() {
        try {
            System.out.println("start execution; threadName=" + Thread.currentThread().getName() + ", time=" + System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(5);
            System.out.println("end execution; threadName=" + Thread.currentThread().getName() + ", time=" + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Sub subRef = new Sub();
        Thread a = new MyThreadA(subRef);
        a.setName("A");
        a.start();

        Thread b = new MyThreadB(subRef);
        b.setName("B");
        b.start();
    }
}

class Sub extends Case9 {
    @Override
    public /*synchronized*/ void m() {
        try {
            System.out.println("start sub execution; threadName=" + Thread.currentThread().getName() + ", time=" + System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(5);
            System.out.println("end sub execution; threadName=" + Thread.currentThread().getName() + ", time=" + System.currentTimeMillis());
            super.m();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThreadA extends Thread {
    private Sub sub;

    public MyThreadA(Sub sub) {
        super();
        this.sub = sub;
    }

    @Override
    public void run() {
        sub.m();
    }
}

class MyThreadB extends Thread {
    private Sub sub;

    public MyThreadB(Sub sub) {
        super();
        this.sub = sub;
    }

    @Override
    public void run() {
        sub.m();
    }
}

