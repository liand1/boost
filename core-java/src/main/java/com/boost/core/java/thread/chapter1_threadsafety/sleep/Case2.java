package com.boost.core.java.thread.chapter1_threadsafety.sleep;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 首先创建了一个独占锁，然后创建了两个线程，每个线程内部先获取锁，然后睡眠，睡眠结束后会释放锁。
 * 首先无论你执行多少遍上面的代码都是先输出线程 A 的打印或者先输出线程 B 的打印，不会存在线程 A 和线程 B 交叉打印的情况。
 * 从执行结果看线程A先获取了锁，那么线程A会先打印一行，然后调用 sleep 让自己沉睡 10s，在线程A沉睡的这 10s 内那个
 * 独占锁 lock 还是线程A自己持有的，线程 B 会一直阻塞直到线程 A 醒过来后执行 unlock 释放锁。
 */
public class Case2 {

    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        createThreadA().start();
        createThreadB().start();

    }

    private static Thread createThreadA() {
        return new Thread(()->{
            try {
                try {
                    lock.lock();
                    System.out.println("child threadA is in sleep");
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println("child threadA is in awaked");
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static Thread createThreadB() {
        return new Thread(()->{
            try {
                try {
                    lock.lock();
                    System.out.println("child threadB is in sleep");
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println("child threadB is in awaked");
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
