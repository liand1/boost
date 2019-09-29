package com.boost.core.java.thread.chapter1_threadsafety.synchronize;

public class SynchronizedForThis {

    private int count = 10;

    public void m() {
        // 任何线程要执行下面的代码，必须先拿到this的锁
        synchronized (this) {
            count--;
            System.out.println(Thread.currentThread().getName() + " count=" + count);
        }
    }
}
