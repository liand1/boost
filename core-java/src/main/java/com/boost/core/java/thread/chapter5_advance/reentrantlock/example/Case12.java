package com.boost.core.java.thread.chapter5_advance.reentrantlock.example;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Condition实现顺序执行
 */
public class Case12 {
    volatile private static int nextPrintWho = 1;

    private static ReentrantLock lock = new ReentrantLock();
    private final static Condition conditionA = lock.newCondition();
    private final static Condition conditionB = lock.newCondition();
    private final static Condition conditionC = lock.newCondition();

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            getThreadA().start();
            getThreadB().start();
            getThreadC().start();
        }
    }

    private static Thread getThreadA() {
        return new Thread(() -> {
            try {
                lock.lock();
                while (nextPrintWho != 1) {
                    conditionA.await();
                }
                for (int i = 0; i < 3; i++) {
                    System.out.println("ThreadA " + i);
                }
                nextPrintWho = 2;
                conditionB.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
    }

    private static Thread getThreadB() {
        return new Thread(() -> {
            try {
                lock.lock();
                while (nextPrintWho != 2) {
                    conditionB.await();
                }
                for (int i = 0; i < 3; i++) {
                    System.out.println("ThreadB " + i);
                }
                nextPrintWho = 3;
                conditionC.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
    }

    private static Thread getThreadC() {
        return new Thread(() -> {
            try {
                lock.lock();
                while (nextPrintWho != 3) {
                    conditionC.await();
                }
                for (int i = 0; i < 3; i++) {
                    System.out.println("ThreadC " + i);
                }
                nextPrintWho = 1;
                conditionA.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
    }


}

