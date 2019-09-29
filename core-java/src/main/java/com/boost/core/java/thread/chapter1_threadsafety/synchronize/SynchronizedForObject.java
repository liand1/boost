package com.boost.core.java.thread.chapter1_threadsafety.synchronize;

/**
 * 对某个对象加锁，互斥锁
 */
public class SynchronizedForObject {

    private int count = 10;
    private Object o = new Object();

    public void m() {
        // 任何线程要执行下面的代码，必须先拿到o的锁，如果第二个线程要执行这个代码，就要等到o对象的锁被释放，也就是
        // 执行到大括号结尾的那里，所以只要有一个线程拿到了o的锁，其它线程就拿不到了
        // synchronize锁定的是对象，不是代码块, 加了这个关键字的代码块是一个原子性操作， 是不可分的
        //当一个线程想要执行同步方法里面的代码时，首先尝试去拿这把锁，如果能拿到这把锁，那么这个线程就可以去
        // 执行synchronize里面的代码，如果不能拿到这把锁，那么这个线程就会不断地去尝试拿这把锁，知道能够拿到为止
        synchronized (o) {
            count--;
            System.out.println(Thread.currentThread().getName() + " count=" + count);
        }
    }
}
