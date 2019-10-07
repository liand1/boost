package com.boost.core.java.thread.chapter1_threadsafety.currentthread;

/**
 * currentThread()返回代码段正在被哪个线程调用的信息
 */
public class Case1 extends Thread {
    public Case1() {
        System.out.println("constructor print:" + Thread.currentThread().getName());// print: constructor print:main
    }

    @Override
    public void run() {
        System.out.println("run method print: " + Thread.currentThread().getName());// print:  run method print: Thread-0
    }

    public static void main(String[] args) {
        new Case1().start();
    }
}
