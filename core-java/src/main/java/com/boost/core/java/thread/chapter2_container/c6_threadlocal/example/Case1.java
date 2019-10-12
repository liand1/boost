package com.boost.core.java.thread.chapter2_container.c6_threadlocal.example;

import java.util.concurrent.TimeUnit;


/**
 * 变量值的共享可以使用public static变量的形式，所有的线程都使用同一个public static变量。
 * 如果想实现每一个线程都有自己的共享变量该如何解决？ThreadLocal主要解决的就是每个线程绑
 * 定自己的值，可以将ThreadLocal类比喻成全局存放数据的盒子，盒子中可以存放每个线程的私有数据。
 */
public class Case1 {

    volatile static Person p = new Person();

    public static void main(String[] args) {

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(p.name);
        }).start();

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            p.name = "lisi";
        }).start();
    }



}

class Person {
    String name = "zhangsan";
}
