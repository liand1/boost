package com.boost.core.java.thread.chapter3_atomicobject;

import java.util.concurrent.atomic.LongAdder;

public class Case4 {
    private static LongAdder longAdder =  new LongAdder();

    private static void addOne() {
        longAdder.increment();
    }

    private static void add(long num) {
        for (long i = 0; i < num; i++) {
            addOne();
        }
    }

    public static Thread createThread() {
        return new Thread(() -> {
            add(1000000);
            System.out.println("threadName=" + Thread.currentThread().getName());
        });
    }

    /**
     * time is about 1s
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            Thread thread = createThread();
            thread.start();
        }
        long i = 0;
        while (i != (500 * 1000000)) {
            i = longAdder.longValue();
        }
        long end = System.currentTimeMillis();
        System.out.println("total count = " + longAdder.longValue() + ", total time is " + ((end - start) / 1000));
    }
}
