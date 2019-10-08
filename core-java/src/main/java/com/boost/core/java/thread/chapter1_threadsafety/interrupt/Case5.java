package com.boost.core.java.thread.chapter1_threadsafety.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 睡眠中中断线程
 * 抛出异常，会被清除掉中断状态
 */
public class Case5 extends Thread {

    @Override
    public void run() {
        super.run();
        try {
            System.out.println("Start execution");
            Thread.sleep(20000);
            System.out.println("completed execution");
        } catch (InterruptedException e) {
            System.out.println("this.isInterrupted(): " + this.isInterrupted());// false
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Case5 c = new Case5();
            c.start();
            TimeUnit.SECONDS.sleep(1);
            c.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end");
    }

}
