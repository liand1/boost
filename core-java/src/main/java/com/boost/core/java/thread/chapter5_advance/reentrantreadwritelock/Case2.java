package com.boost.core.java.thread.chapter5_advance.reentrantreadwritelock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 写写互斥
 * lock.writeLock().lock()的效果就是同一时间只允许一个线程执行lock后面的代码
 */
public class Case2 {

    public static void main(String[] args) {
        Service2 service = new Service2();

        new Thread(service::write).start();
        new Thread(service::write).start();
    }
}

class Service2 {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

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

