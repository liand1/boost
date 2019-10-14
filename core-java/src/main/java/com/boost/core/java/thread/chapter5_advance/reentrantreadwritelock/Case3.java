package com.boost.core.java.thread.chapter5_advance.reentrantreadwritelock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写互斥&写读互斥
 */
public class Case3 {

    public static void main(String[] args) {
        Service3 service = new Service3();
        // 执行顺序改变，都不影响互斥的结果
        new Thread(service::read).start();
        new Thread(service::write).start();
    }

}

class Service3 {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void read() {
        try {
            try {
                lock.readLock().lock();
                System.out.println("获得读锁 " + Thread.currentThread().getName() + " " + System.currentTimeMillis());
                TimeUnit.SECONDS.sleep(5);
            } finally {
                lock.readLock().unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void write() {
        try {
            try {
                lock.writeLock().lock();
                System.out.println(
                        "获得写锁 " + Thread.currentThread().getName() + " " + System.currentTimeMillis());
                TimeUnit.SECONDS.sleep(5);
            } finally {
                lock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

