package com.boost.core.java.thread.chapter5_advance.locksupport;

import java.util.concurrent.locks.LockSupport;

/**
 * void unpark(Thread thread)
 * 当一个线程调用了 unpark 时候，如果参数 thread 线程没有持有 thread 与 LockSupport 类关联的许可证，则让 thread 线程持有。
 * 如果 thread 之前调用了 park() 被挂起，则调用 unpark 后，该线程会被唤醒。
 * 如果 thread 之前没有调用 park，则调用 unPark 方法后，在调用 park() 方法，会立刻返回
 */
public class Case2 {

    public static void main(String[] args) {
        System.out.println("begin park!");
        //使当前线程获取到许可证
        LockSupport.unpark(Thread.currentThread());
        //再次调用park
        LockSupport.park();
        System.out.println("end park!");// print
    }
}
