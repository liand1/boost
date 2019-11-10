package com.boost.core.java.thread.chapter5_advance.aqs;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lock.newCondition()的作用其实是new了一个AQS内部声明的ConditionObject对象，ConditionObject是AQS的内部类。每个条件变量
 * 内部都维护了一个条件队列，用来存放调用条件变量的await()方法时被阻塞的线程。注意这个条件队列和AQS队列不是一回事。
 *
 *  代码(1)创建了一个独占锁ReentrantLock对象，ReentrantLock是基于AQS实现的锁。
 *  代码（2）使用创建的Lock对象的newCondition（）方法创建了一个ConditionObject变量，这个变量就是Lock锁对应的一个条件变量。
 *  需要注意的是，一个Lock对象可以创建多个条件变量。
 *  代码（3）首先获取了独占锁，代码（4）则调用了条件变量的await（）方法阻塞挂起了当前线程。当其他线程调用条件变量的
 *  signal方法时，被阻塞的线程才会从await处返回。需要注意的是，和调用Object的wait方法一样，如果在没有获取到锁前调用了条件
 *  变量的await方法则会抛出java.lang.IllegalMonitorStateException异常。
 *  代码（5）则释放了获取的锁。

 *
 */
public class Case4 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();//(2)

    /**
     * 打印顺序
     * begin wait
     * begin signal
     * end signal
     * end wait
     */
    public static void main(String[] args) {
        Case4 c = new Case4();
        new Thread(()->c.await()).start();
        new Thread(()->c.signal()).start();
    }

    public void await() {
        lock.lock();//(3)
        try {
            System.out.println("begin wait");
            condition.await();//(4)
            System.out.println("end wait");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//(5)
        }
    }

    public void signal() {
        lock.lock();//(6)
        try {
            System.out.println("begin signal");
            condition.signal();//(7)
            System.out.println("end signal");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//(8)
        }
    }
}
