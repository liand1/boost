package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example.Case15;

/**
 *
 * 死锁，B永远也得不到执行的机会
 */
public class Case15 {

    public static void main(String[] args) throws Exception {
        Service service = new Service();
        ThreadA threadA = new ThreadA(service);
        threadA.start();

        ThreadB threadB = new ThreadB(service);
        threadB.start();
    }
}
