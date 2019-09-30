package com.boost.core.java.thread.chapter2_container.threadlocal.example;

import java.util.concurrent.TimeUnit;

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
