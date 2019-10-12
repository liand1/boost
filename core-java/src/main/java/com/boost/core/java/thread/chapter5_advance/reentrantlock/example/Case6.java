package com.boost.core.java.thread.chapter5_advance.reentrantlock.example;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Condition
 * synchronized与wait和notify/notifyAll相结合可以实现等待，通知模式。
 * ReentrantLock也可以实现同样的功能，但需要借助于Condition对象。
 *
 * ReentrantLock中的await相当与Object中的wait
 * ReentrantLock中的await(long, TimeUnit)相当于Object中的wait(long)
 * ReentrantLock中的signal相当于Object中的notify
 * ReentrantLock中的signalAll相当于Object中的notifyAll
 */
public class Case6 {

    public static void main(String[] args) throws Exception{
        Service6 service = new Service6();
        new Thread( () -> service.await()).start();
        TimeUnit.SECONDS.sleep(3);
        service.signal();
    }
}

class Service6 {

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void await() {
        try {
            lock.lock();
            System.out.println("wait");
//            condition.await(3, TimeUnit.SECONDS);
            condition.await();
            System.out.println("under await");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println("lock is free");
        }
    }

    public void signal() {
        try {
            lock.lock();
            System.out.println("signal");
            condition.signal();
            System.out.println("under signal");
        } finally {
            lock.unlock();
            System.out.println("lock is free");
        }
    }

}
