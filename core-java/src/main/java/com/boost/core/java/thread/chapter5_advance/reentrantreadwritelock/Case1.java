package com.boost.core.java.thread.chapter5_advance.reentrantreadwritelock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读读共享
 * 这面这个是使用ReentrantReadWriteLock里面读相关的锁，也称为共享锁。也就是多个读锁之间不互斥。表示在没有线程进行
 * 写操作时，进行读取操作的多线线程都可以获取读锁。
 */
public class Case1 {

    /**
     * 下面打印结果可以得知，三个线程近乎同时进入lock后面的代码。说明在此使用了lock.readLock().lock()可以提高程序
     * 运行效率
     * @param args
     */
    public static void main(String[] args) {
        Service1 service = new Service1();
        new Thread(()->service.read()).start();
        new Thread(()->service.read()).start();
        new Thread(()->service.read()).start();
    }
}

class Service1 {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void read() {
        try {
            try {
                lock.readLock().lock();
                System.out.println("获得读锁" + Thread.currentThread().getName() + " " + System.currentTimeMillis());
                TimeUnit.SECONDS.sleep(3);
            } finally {
                lock.readLock().unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
