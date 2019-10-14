package com.boost.core.java.thread.chapter5_advance.reentrantlock.example;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Case10 {

    public static void main(String[] args) {
        Service10 service = new Service10();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                service.producer();
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                service.consumer();
            }
        }).start();
    }
}

class Service10 {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private boolean hasValue = false;

    public void producer() {
        try {
            lock.lock();
            while (hasValue) {
                System.out.println("有可能★★连续");
                condition.await();
            }
            System.out.println("打印★");
            hasValue = true;
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void consumer() {
        try {
            lock.lock();
            while (!hasValue) {
                System.out.println("有可能☆☆连续");
                condition.await();
            }
            System.out.println("打印☆");
            hasValue = false;
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
