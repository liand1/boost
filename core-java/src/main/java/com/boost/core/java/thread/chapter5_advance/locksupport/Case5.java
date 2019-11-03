package com.boost.core.java.thread.chapter5_advance.locksupport;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 先进先出互斥
 *
 * 这是个先进先出的锁，也就是只有队列的首元素可以获取锁,在代码 (1 ）处，如
 * 果当前线程不是队首或者当前锁己经被其他线程获取，则调用 park 方法挂起自己
 *
 * 然后在代码(2)处判断，如果 park 方法是因为被中断而返回，则忽略中断，并且重
 * 置中断标志，做个标记，然后再 判断当前线程是不是队首元素或者当前锁是否己经被其
 * 他线程获取，如果是则继续调用 park 方法挂起自己
 *
 * 然后在代码(3)中，判断标记，如果标记为 true 则中断 线程，这个怎么理解呢？
 * 其实就是其他线程中断了该线程，虽然我对中断信号不感兴趣，忽略它，但是不代表其他
 * 线程对该标志不感兴趣，所以要恢复下
 */
public class Case5 {

    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<Thread>();

    public void lock() {
        boolean wasInterrupted = false;
        Thread current = Thread.currentThread();
        waiters.add(current);

        // 只有队首的线程可以获取锁（1)
        while(waiters.peek() != current || !locked.compareAndSet(false, true)) {
            LockSupport.park(this);
            if (Thread.interrupted()) { // (2)
                wasInterrupted = true;
            }
        }

        waiters.remove();
        if (wasInterrupted) {// (3)
            current.interrupt();
        }

    }

    public void unlock() {
        locked.set(false);
        LockSupport.unpark(waiters.peek());
    }
}
