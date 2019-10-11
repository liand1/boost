package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example.Case14;

import java.util.concurrent.TimeUnit;

/**
 * MyService里面的逻辑是如果size<1才会add数据，所以正常情况应该打印1
 * 原因是这个thread1和thread2的执行是异步的
 *
 * MyService注释的部分可以解决这个问题，
 */
public class Case14 {

    public static void main(String[] args) throws Exception{
        MyOneList list = new MyOneList();

        MyThread1 thread1 = new MyThread1(list);
        thread1.setName("A");
        thread1.start();

        MyThread2 thread2 = new MyThread2(list);
        thread2.setName("B");
        thread2.start();

        TimeUnit.SECONDS.sleep(6);
        System.out.println("listSize=" + list.getSize());//listSize=2 脏读
    }
}
