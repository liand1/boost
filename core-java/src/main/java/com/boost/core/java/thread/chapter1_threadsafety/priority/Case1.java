package com.boost.core.java.thread.chapter1_threadsafety.priority;

/**
 * 优先级高的获取cpu时间就越长,也有不确定性和随机性
 */
public class Case1 {

    public static void main(String[] args) {

        Thread t1 = new Thread(new T1());
        Thread t2 = new Thread(new T2());

        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        t2.start();
    }

}

class T1 implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++) {
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
