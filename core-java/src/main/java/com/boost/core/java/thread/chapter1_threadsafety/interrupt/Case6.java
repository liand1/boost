package com.boost.core.java.thread.chapter1_threadsafety.interrupt;

/**
 * 先中断线程，然后进入到sleep
 */
public class Case6 extends Thread {

    @Override
    public void run() {
        super.run();
        try {
            for (int i = 0; i < 1000000; i++) {
                System.out.println("i=" + (i + 1));
            }

            System.out.println("Start execution");
            Thread.sleep(20000);// throw InterruptedException
            System.out.println("completed execution");
        } catch (InterruptedException e) {
            System.out.println("this.isInterrupted(): " + this.isInterrupted());// false
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Case6 c = new Case6();
        c.start();
        c.interrupt();
        System.out.println("end");
    }
}
