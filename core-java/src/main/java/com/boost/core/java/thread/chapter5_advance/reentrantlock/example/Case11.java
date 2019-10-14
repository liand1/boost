package com.boost.core.java.thread.chapter5_advance.reentrantlock.example;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lock.getHoldCount()查询当前线程保持此锁定的次数，也就是调用lock()方法的次数
 * lock().getQueueLength()返回正在等待获取此锁的线程估计数
 * lock().getWaitQueueLength(Condition)返回正在等待获取此锁的线程估计数
 * hasQueuedThread(Thread)查询指定线程是否在等待获取此锁
 * hasQueuedThreads()查询是否有线程在等待获取此锁
 * hasWaiters(Condition)是否有线程正在等待此锁有关的condition条件
 * isFair()
 * isHeldByCurrentThread()当前线程是否保持锁定
 * isLock()
 * awaitUntil
 */
public class Case11 {

    static Service11 service = new Service11();

    public static void main(String[] args) throws Exception{
//        testGetHoldCount();
//        testGetQueueLength();
        testGetWaitQueueLength();
    }

    public static void testGetHoldCount() {
        service.getHoldCount1();
    }

    public static void testGetQueueLength() throws Exception {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> service.getQueueLength()).start();
        }
        TimeUnit.SECONDS.sleep(2);
        System.out.println("有线程数：" + service.getLock().getQueueLength() + "个线程在等待获取锁");
    }

    public static void testGetWaitQueueLength() throws Exception {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> service.await()).start();
            TimeUnit.SECONDS.sleep(2);
            service.signal();
        }
    }

}

class Service11 {

    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public ReentrantLock getLock() {
        return lock;
    }

    public void getHoldCount1() {
        try {
            lock.lock();
            System.out.println("getHoldCount1=" + lock.getHoldCount());
            getHoldCount2();
        } finally {
            lock.unlock();
        }
    }

    public void getHoldCount2() {
        try {
            lock.lock();
            System.out.println("getHoldCount2=" + lock.getHoldCount());
        } finally {
            lock.unlock();
        }
    }

    public void getQueueLength() {
        try {
            lock.lock();
            System.out.println("threadName=" + Thread.currentThread().getName() + " 进入方法 ");
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void await() {
        try {
            lock.lock();
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        try {
            lock.lock();
            System.out.println("有 " + lock.getWaitQueueLength(condition) + "个线程正在等待condition");
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

}
