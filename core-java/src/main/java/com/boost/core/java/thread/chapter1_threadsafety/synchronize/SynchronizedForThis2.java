package com.boost.core.java.thread.chapter1_threadsafety.synchronize;

public class SynchronizedForThis2 {

    private int count = 10;

    // 等同于在方法的代码执行时要synchronized (this), 也就是执行代码时锁定this对象
    public synchronized void m() {
        count--;
        System.out.println(Thread.currentThread().getName() + " count=" + count);
    }
}
