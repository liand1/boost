package com.boost.core.java.thread.chapter1_threadsafety.wait.example;

/**
 * 在调用wait之前，线程必须获得该对象的对象级别锁，即只能在同步方法或同步块中调用wait
 * 方法。否则会抛出IllegalMonitorStateException异常
 */
public class Case1 {

    public static void main(String[] args) {
        String str = "hello world";
        try {
            str.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
