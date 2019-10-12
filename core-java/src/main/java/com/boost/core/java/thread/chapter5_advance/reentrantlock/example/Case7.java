package com.boost.core.java.thread.chapter5_advance.reentrantlock.example;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用多个Condition通知部分线程(错误用法，唤醒了所有线程)
 */
public class Case7 {

    public static void main(String[] args) throws Exception{
        Service7 service = new Service7();
        new Thread(()->service.awaitA()).start();
        new Thread(()->service.awaitB()).start();
        TimeUnit.SECONDS.sleep(3);
        service.signalAll();
    }
}

class Service7 {

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void awaitA() {
        try {
            lock.lock();
            System.out.println("begin awaitA, ThreadName="+Thread.currentThread().getName()+", time= " +System.currentTimeMillis());
            condition.await();
            System.out.println("end awaitA, ThreadName="+Thread.currentThread().getName()+", time= " +System.currentTimeMillis());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void awaitB() {
        try {
            lock.lock();
            System.out.println("begin awaitB, ThreadName="+Thread.currentThread().getName()+", time= " +System.currentTimeMillis());
            condition.await();
            System.out.println("end awaitB, ThreadName="+Thread.currentThread().getName()+", time= " +System.currentTimeMillis());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signalAll() {
        try {
            lock.lock();
            System.out.println("begin signalAll, ThreadName="+Thread.currentThread().getName()+", time= " +System.currentTimeMillis());
            condition.signalAll();
        } finally {
            lock.unlock();
        }

    }
}

