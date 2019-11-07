package com.boost.core.java.thread.chapter5_advance.aqs.case2;

import java.util.concurrent.locks.Lock;

/**
 * 输出10000
 */
public class Case2 {

    static int m = 0;

    public static Lock lock = new MLock();


    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                try {
                    lock.lock();
                    for (int j = 0; j < 100; j++) {
                        m++;
                    }
                } finally {
                    lock.unlock();
                }
            });
        }

        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();   //等待所有线程运行结束
        }

        System.out.println(m);
    }
}
