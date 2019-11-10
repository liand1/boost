package com.boost.core.java.thread.chapter5_advance.aqs.case5;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 基于AQS实现的不可重入的独占锁
 * 在如下代码中，NonReentrantLock定义了一个内部类Sync用来实现具体的锁的操作，Sync则继承了AQS。由于我们实现的是独占模式的
 * 锁，所以Sync重写了tryAcquire、tryRelease和isHeldExclusively 3个方法。另外，Sync提供了newCondition这个方法用来支持条
 * 件变量。
 */
public class Case5 implements Lock, java.io.Serializable {
    // 内部帮助类
    private static class Sync extends AbstractQueuedSynchronizer {
        // 是否锁已经被持有
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        //如果state为0 则尝试获取锁
        public boolean tryAcquire(int acquires) {
            assert acquires == 1;//
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        // 尝试释放锁，设置state为0
        protected boolean tryRelease(int releases) {
            assert releases == 1;//
            if (getState() == 0)
                throw new IllegalMonitorStateException();
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        // 提供条件变量接口
        Condition newCondition() {
            return new ConditionObject();
        }
    }

    //创建一个Sync来做具体的工作
    private final Sync sync = new Sync();

    public void lock() {
        sync.acquire(1);
    }

    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    public void unlock() {
        sync.release(1);
    }

    public Condition newCondition() {
        return sync.newCondition();
    }

    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    public boolean tryLock(long timeout, TimeUnit unit) throws
            InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }

}
