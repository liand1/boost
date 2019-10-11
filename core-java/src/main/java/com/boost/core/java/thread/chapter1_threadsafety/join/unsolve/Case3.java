package com.boost.core.java.thread.chapter1_threadsafety.join.unsolve;

import java.util.concurrent.TimeUnit;

/**
 * 打印结果有多种
 * 第一种
 * begin 1 threadName = Thread-1 1570783417575
 * end 1 threadName = Thread-1 1570783422576
 * main end 1570783422576
 * begin 2 threadName = Thread-0 1570783422577
 * end 2 threadName = Thread-0 1570783427578
 * 执行步骤：
 * 1.t2.join(2000)先抢到t2锁,让后将其释放
 * 2.t1抢到t2锁,执行里面的run方法,打印前面两行,释放锁
 * 3.
 */
public class Case3 {

    public static void main(String[] args) throws Exception {
        Thread2 t2 = new Thread2();
        Thread1 t1 = new Thread1(t2);

        t1.start();
        t2.start();
        t2.join(2000);

        System.out.println("main end " + System.currentTimeMillis());
    }
}

class Thread1 extends Thread {

    private Thread2 thread2;

    public Thread1(Thread2 thread2) {
        this.thread2 = thread2;
    }

    @Override
    public void run() {
        try {
            synchronized (thread2) {
                System.out.println("begin 1 threadName = " + Thread.currentThread().getName() + " " + System
                        .currentTimeMillis());
                TimeUnit.SECONDS.sleep(5);
                System.out.println("end 1 threadName = " + Thread.currentThread().getName() + " " + System
                        .currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Thread2 extends Thread {

    @Override
    public synchronized void run() {
        try {
            System.out.println("begin 2 threadName = " + Thread.currentThread().getName() + " " + System
                    .currentTimeMillis());
            TimeUnit.SECONDS.sleep(5);
            System.out.println("end 2 threadName = " + Thread.currentThread().getName() + " " + System
                    .currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
