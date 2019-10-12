package com.boost.core.java.thread.chapter5_advance.reentrantlock.example;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用多个Condition通知部分线程(正确用法，只唤醒和B相关的线程)
 */
public class Case8 {

    public static void main(String[] args) throws Exception {
        Service8 service = new Service8();

        new Thread(() -> service.awaitA1()).start();
        new Thread(() -> service.awaitA2()).start();
        new Thread(() -> service.awaitB1()).start();
        new Thread(() -> service.awaitB2()).start();

        TimeUnit.SECONDS.sleep(3);
        service.signalB();
    }
}

class Service8 {

    private Lock lock = new ReentrantLock();
    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();

    public void awaitA1() {
        try {
            lock.lock();
            System.out.println("begin awaitA1, ThreadName=" + Thread.currentThread().getName() + ", time= " + System.currentTimeMillis());
            conditionA.await();
            System.out.println("end awaitA1, ThreadName=" + Thread.currentThread().getName() + ", time= " + System.currentTimeMillis());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void awaitA2() {
        try {
            lock.lock();
            System.out.println("begin awaitA2, ThreadName=" + Thread.currentThread().getName() + ", time= " + System.currentTimeMillis());
            conditionA.await();
            System.out.println("end awaitA2, ThreadName=" + Thread.currentThread().getName() + ", time= " + System.currentTimeMillis());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void awaitB1() {
        try {
            lock.lock();
            System.out.println("begin awaitB1, ThreadName=" + Thread.currentThread().getName() + ", time= " + System.currentTimeMillis());
            conditionB.await();
            System.out.println("end awaitB1, ThreadName=" + Thread.currentThread().getName() + ", time= " + System.currentTimeMillis());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void awaitB2() {
        try {
            lock.lock();
            System.out.println("begin awaitB2, ThreadName=" + Thread.currentThread().getName() + ", time= " + System.currentTimeMillis());
            conditionB.await();
            System.out.println("end awaitB2, ThreadName=" + Thread.currentThread().getName() + ", time= " + System.currentTimeMillis());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signalA() {
        try {
            lock.lock();
            System.out.println("begin signalA, ThreadName=" + Thread.currentThread().getName() + ", time= " + System.currentTimeMillis());
            conditionA.signalAll();
        } finally {
            lock.unlock();
        }

    }

    public void signalB() {
        try {
            lock.lock();
            System.out.println("begin signalB, ThreadName=" + Thread.currentThread().getName() + ", time= " + System.currentTimeMillis());
            conditionB.signalAll();
        } finally {
            lock.unlock();
        }

    }
}