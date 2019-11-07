package com.boost.core.java.thread.chapter5_advance.aqs;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 代码（1）创建了一个独占锁ReentrantLock对象，ReentrantLock是基于AQS实现的锁。
 * 代码（2）使用创建的Lock对象的newCondition（）方法创建了一个ConditionObject变量，这个变量就是Lock锁对应的一个条件变
 *   量。需要注意的是，一个Lock对象可以创建多个条件变量。
 * 代码（3）首先获取了独占锁，代码（4）则调用了条件变量的await（）方法阻塞挂起了当前线程。当其他线程调用条件变量的signal
 * 方法时，被阻塞的线程才会从await处返回。需要注意的是，和调用Object的wait方法一样，如果在没有获取到锁前调用了条件变量
 * 的await方法则会抛出java.lang.IllegalMonitorStateException异常。
 * 代码（5）则释放了获取的锁。
 * 其实这里的Lock对象等价于synchronized加上共享变量，调用lock.lock（）方法就相当于进入了synchronized块（获取了共享变量
 * 的内置锁），调用lock.unLock（）方法就相当于退出synchronized块。调用条件变量的await（）方法就相当于调用共享变量的
 * wait（）方法，调用条件变量的signal方法就相当于调用共享变量的notify（）方法。调用条件变量的signalAll（）方法就相当于
 * 调用共享变量的notifyAll（）方法。
 */
public class Case1 {


    ReentrantLock lock = new ReentrantLock();// (1)
    Condition condition = lock.newCondition();// (2)

    private void await() {
        lock.lock(); //(3)
        try {
            System.out.println("begin wait");
            condition.await(); //(4)
            System.out.println("end wait");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); //(5)
        }
    }

    private void signal() {
        lock.lock(); //(6)
        try {
            System.out.println("begin signal");
            condition.signal(); //(7)
            System.out.println("end signal");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); //(8)
        }

    }

    public static void main(String[] args) {
        Case1 c = new Case1();
        c.await();
        c.signal();
    }
}
