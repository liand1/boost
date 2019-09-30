package com.boost.core.java.thread.chapter1_threadsafety.wait.example;

import java.util.concurrent.TimeUnit;

public class Case3 {

    // 打印结果
    // above wait
    //above notify
    //under notify
    //under wait
    // 线程1执行开始，打印above wait，然后调用了lock.wait(),当前线程释放lock的锁，然后进行等待状态，等待被唤醒
    // 线程2执行开始，打印above notify，然后调用lock.notify(),唤醒等待的线程(切记，这时候线程1并不会开始执行，
    // 因为notify并不会释放锁，必须要等到其他线程执行完毕释放完锁，其他等待的线程才会继续执行，如果有多个线程都在等待
    // 那么这么多个线程都会争取这个对象的锁)

    public static void main(String[] args) {
        try {
            Object lock = new Object();
            Thread1 t1 = new Thread1("t1", lock);
            t1.start();
            TimeUnit.SECONDS.sleep(3);
            Thread2 t2 = new Thread2("t2", lock);
            t2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Thread1 extends Thread {

    private Object lock;

    public Thread1(String s, Object lock) {
        super(s);
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                System.out.println("above wait");
                lock.wait();
                System.out.println("under wait");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Thread2 extends Thread {

    private Object lock;

    public Thread2(String s, Object lock) {
        super(s);
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            System.out.println("above notify");
            lock.notify();
            System.out.println("under notify");
        }
    }
}
