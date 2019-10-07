package com.boost.core.java.thread.chapter1_threadsafety.yield;

/**
 * 让出cpu一小会儿，给其它线程执行的机会
 */
public class Case1 extends Thread {

    public Case1(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(getName() + ": " + i);
            if (i % 10 == 0) {
                yield();
            }
        }
    }

    // 只要到10的倍数，一定让出cpu
    public static void main(String[] args) {
        new Case1("t1").start();
        new Case1("t2").start();
    }
}
