package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;

import java.util.concurrent.TimeUnit;

/**
 * 一个同步方法调用另外一个同步方法
 * 一个线程已经拥有某个对象的锁，再次申请的时候仍然会得到该对象的锁，也就是说synchronied获得的锁是可重入的
 * 因此线程在试图获得它自己占有的锁时，请求会成功。重进入意味着申请锁的请求是基于"per-thread"而不是"per-invocation"
 * 。重进入的实现是通过每个锁关联一个请求计数器，和一个占有它的线程。当计数为0时，认为锁是未被占有的。线程请求一个未
 * 被占有的锁时，JVM将记录锁的占有者，并且将请求计数置为1.如果同一线程再次请求这个锁，计数将递增；每次占用线程退出同步
 * 代码块，计数器就减一。直到计数器变成0，就意味着锁被释放。
 */
public class Case4 {

    synchronized void m1() {
        System.out.println("m1 start");

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m2();
    }

    synchronized void m2() {

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("m2");
    }

    /**
     * m1和m2加了同步,在执行m1的时候已经申请了一把锁,在m1里面调用了m2方法
     * 这时候返回的这把锁还是之前所持有的
     * @param args
     */
    public static void main(String[] args) {

    }
}
