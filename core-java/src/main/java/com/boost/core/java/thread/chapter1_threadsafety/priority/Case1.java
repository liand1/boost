package com.boost.core.java.thread.chapter1_threadsafety.priority;

/**
 * 优先级高的获取cpu时间的时间就越长
 */
public class Case1 {

    public static void main(String[] args) {

        Thread t1 = new Thread(new T1());
        Thread t2 = new Thread(new T2());

        t2.setPriority(Thread.NORM_PRIORITY + 3);
        t1.start();
        t2.start();
    }

}

class T1 implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            System.out.println("T1: " + i);
        }
    }
}

class T2 implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            System.out.println("-----------T2: " + i);
        }
    }
}
