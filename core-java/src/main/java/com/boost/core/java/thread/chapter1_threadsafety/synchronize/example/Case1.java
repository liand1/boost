package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;

public class Case1 implements Runnable{

    private int count = 10;

    // 等同于在方法的代码执行时要synchronized (this), 也就是执行代码时锁定this对象
    public /*synchronized*/ void run() {
        count--;
        System.out.println(Thread.currentThread().getName() + " count=" + count);
    }

    /**
     * 会打印多种结果,比如THREAD0 count=8,THREAD4 count=5,THREAD2 count=7,THREAD3 count=6,THREAD1 count=8
     * 现在来分析一下场景，为什么打印这个结果
     * 线程0进来，执行了i--，count=9,但是还没有打印
     * 线程1进来，执行了i--，count=8,这时候线程0执行了打印， THREAD0 count=8
     * 线程2进来，执行了i--，count=7,但是还没有打印
     * 线程3进来，执行了i--，count=6,但是还没有打印
     * 线程4进来，执行了i--，count=5,这时候线程4执行了打印， THREAD0 count=5
     * 线程2执行打印, THREAD4 count=7
     * 线程3执行打印, THREAD4 count=6
     * 线程1执行打印, THREAD4 count=8
     */
    public static void main(String[] args) {
        Case1 c = new Case1();
        for(int i=0; i<5; i++) {
            new Thread(c, "THREAD" + i).start();
        }
    }
}
