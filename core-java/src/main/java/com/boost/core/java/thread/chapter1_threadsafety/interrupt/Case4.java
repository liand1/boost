package com.boost.core.java.thread.chapter1_threadsafety.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * Case2中程序的run方法并没有完全退出，只是退出了for循环，如果在for循环下面还有代码块，那是会执行的。
 * 要解决完全退出run方法的情况，这里我们采用了抛出InterruptedException
 * 也可以使用return停止该线程
 */
public class Case4 extends Thread {

    @Override
    public void run() {
        try {
            for (int i = 0; i < 500000; i++) {
                if(Thread.interrupted()) {
                    System.out.println("Thread.interrupted(): " + Thread.interrupted());// print: false
                    System.out.println("已经是停止状态了，退出");
                   throw new InterruptedException();
                }
                System.out.println("i=" + (i + 1));
            }
            System.out.println("under foreach");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Case4 c = new Case4();
            c.start();
            TimeUnit.SECONDS.sleep(1);
            c.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end");
    }
}
