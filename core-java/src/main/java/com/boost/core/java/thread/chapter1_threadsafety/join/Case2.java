package com.boost.core.java.thread.chapter1_threadsafety.join;

import java.util.concurrent.TimeUnit;

/**
 * 如果当前线程被中断，则当前线程出现异常。
 * ThreadA还会继续执行
 */
public class Case2 {

    public static void main(String[] args) {
        try {
            ThreadB b = new ThreadB();
            b.start();
            TimeUnit.SECONDS.sleep(1);

            ThreadC c = new ThreadC(b);
            c.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class ThreadA extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Math.random());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ThreadB extends Thread {

    @Override
    public void run() {
        try {
            ThreadA a = new ThreadA();
            a.start();
            a.join();
            System.out.println("b end");
        } catch (InterruptedException e) {
            System.out.println("b err");
            e.printStackTrace();
        }
    }
}

class ThreadC extends Thread {

    private ThreadB threadB;

    public ThreadC(ThreadB threadB) {
        this.threadB = threadB;
    }

    @Override
    public void run() {
        threadB.interrupt();
    }
}
