package com.boost.core.java.thread.chapter5_advance.aqs.case2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MLock implements Lock {

    private volatile int i=0;

    @Override
    public void lock() {
        synchronized (this){// 已经有线程占用
            while (i!=0){
                try {
                    this.wait();// 阻塞自旋 CAS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i=1;
        }

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        synchronized (this){
            i=0;
            this.notifyAll();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
