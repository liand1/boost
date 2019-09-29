package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;

import java.util.concurrent.TimeUnit;

/**
 * 程序执行过程中，如果出现异常，默认情况锁会被释放
 * 所以，在并发处理的过程中，有异常要多加小心，不然可能会发生不一致的情况
 * 比如，多个线程共同访问同一个资源，这时如果异常处理不合适，在第一个线程中
 * 抛出异常，其他线程就会进入同步代码区，有可能会访问到异常产生时的数据
 */
public class Case6 {
    int count = 0;

    synchronized void m() {
        System.out.println(Thread.currentThread().getName() + " start");
        while(true) {
            count++;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(count == 5) {
                // 此处抛出异常，锁被释放，要想不被释放，可以在这里进行catch，让后让循环继续
                int i = 1/0;
            }
        }
    }

    // 上面的代码逻辑，同步方法里面是一个死循环，如果不报异常，c2永远不会执行，
    // 现在让线程抛出异常，c2会被执行
    public static void main(String[] args) {
        Case6 c = new Case6();

        new Thread(() -> c.m(), "c1").start();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> c.m(), "c2").start();
    }
}
