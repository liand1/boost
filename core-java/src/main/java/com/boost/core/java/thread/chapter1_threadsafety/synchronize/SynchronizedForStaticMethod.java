package com.boost.core.java.thread.chapter1_threadsafety.synchronize;

public class SynchronizedForStaticMethod {

    private static int count = 10;

    /**
     * 如果用在一个静态方法上，锁定的是class,等同于(SynchronizedForStaticMethod.class)
     */
    public static synchronized void m() {
        count--;
        System.out.println(Thread.currentThread().getName() + " count=" + count);
    }

    public static void m2() {
        synchronized (SynchronizedForStaticMethod.class) {
            count--;
            System.out.println(Thread.currentThread().getName() + " count=" + count);
        }
    }
}
