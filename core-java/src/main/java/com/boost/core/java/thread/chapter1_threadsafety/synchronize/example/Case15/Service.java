package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example.Case15;

public class Service {

    public synchronized void methodA() {
        System.out.println("methodA begin");
        boolean isContinue = true;
        while(isContinue) {

        }
        System.out.println("methodA end ");
    }

    public synchronized void methodB() {
        System.out.println("methodB begin");
        System.out.println("methodB end ");
    }

}

/**
 * 解决死锁，让两个方法持有不同的锁
 */
class Service1 {

    private Object lock1 = new Object();

    public  void methodA() {
        synchronized (lock1) {
            System.out.println("methodA begin");
            boolean isContinue = true;
            while(isContinue) {

            }
            System.out.println("methodA end ");
        }
    }

    private Object lock2 = new Object();
    public  void methodB() {
        synchronized (lock2) {
            System.out.println("methodB begin");
            System.out.println("methodB end ");
        }
    }

}
