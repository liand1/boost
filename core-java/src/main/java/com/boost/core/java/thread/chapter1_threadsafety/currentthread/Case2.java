package com.boost.core.java.thread.chapter1_threadsafety.currentthread;

/**
 * Thread.currentThread().getName()是获得调用这个方法的线程的名字
 * this.getName()这个方法是获取当前对象的名字，只是单纯的方法
 * 的调用。因为没有重写这个方法所以调用的是父类Thread(把这个对象
 * 当作是普通的对象)中的方法。
 */
public class Case2 extends Thread {

    Case2() {
        System.out.println("Case2--begin");
        System.out.println("Thread.currentThread().getName(): " + Thread.currentThread().getName());
        System.out.println("this.getName(): " + this.getName());// print:  Thread-0 , why not print main?
        System.out.println("Case2--end");
        System.out.println("");
    }

    @Override
    public void run() {
        System.out.println("cun--begin");
        System.out.println("Thread.currentThread().getName(): " + Thread.currentThread().getName());
        System.out.println("this.getName(): " + this.getName());
        System.out.println("run--end");
    }

    public static void main(String[] args) {
        new Case2().start();


        /*Case2 c = new Case2();
        Thread t = new Thread(c);
        t.setName("thread");
        t.start();*/
    }
}
