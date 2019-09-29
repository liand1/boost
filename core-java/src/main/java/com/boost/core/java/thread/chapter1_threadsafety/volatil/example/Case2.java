package com.boost.core.java.thread.chapter1_threadsafety.volatil.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 解决同样的问题更高效的方法，使用AtomXXX类
 * AtomXXX类本身方法都是原子性的，但不能保证多个方法调用是原子性的
 */
public class Case2 {

    AtomicInteger count =new AtomicInteger(0);

    void m() {
        for(int i=0; i<10000; i++) count.incrementAndGet();
    }

    public static void main(String[] args) {
        Case2 c = new Case2();

        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<10; i++) {
            threads.add(new Thread(c::m, "thread-"+i));
        }

        threads.forEach(o -> o.start());

        threads.forEach(o ->{
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(c.count);
    }
}
