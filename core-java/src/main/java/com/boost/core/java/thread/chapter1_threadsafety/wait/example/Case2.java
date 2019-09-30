package com.boost.core.java.thread.chapter1_threadsafety.wait.example;

public class Case2 {

    // under wait()永远不会执行，因为当前线程在等待中，没有释放锁，并且没有被唤醒
    public static void main(String[] args) {
        try {
            String lock = "hello world";
            System.out.println("above syn ");
            synchronized (lock) {
                System.out.println("above wait()");
                lock.wait();
                System.out.println("under wait()");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}