package com.boost.core.java.thread.chapter1_threadsafety.volatil.example;

import java.util.ArrayList;
import java.util.List;

/**
 * volatile关键字并不能保证多个线程共同修改running变量时所带来的不一致问题，也就是说volatile不能替代synchronized
 * volatile只能保证可见性，不能保证原子性
 * synchronized能保证可见性，也能保证原子性， 但是性能比volatile低很多
 * 理解volatile和synchronized的区别
 */
public class Case1 {

    volatile int count =0;

    void m() {
        for(int i=0; i<1000; i++) count++;
    }

    /**
     * 10 个线程，每个线程+10000,正常情况下count=100000，但是结果不是这样的
     * 比如程序计算，count被累加到100的时候，其中一个线程把100读到自己的缓冲区中，
     * 还每开始累加，这时候另外一个线程也读到这个100，累加到了101写回到了主内存
     * 但是另外一个线程之前还是100这时候也只时累加到101（它不会去管写写数据的时候count已经是101了）
     */
    public static void main(String[] args) {
        Case1 c = new Case1();

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
