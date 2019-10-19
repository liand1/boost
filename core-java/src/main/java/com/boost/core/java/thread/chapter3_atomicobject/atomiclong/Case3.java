package com.boost.core.java.thread.chapter3_atomicobject.atomiclong;

import java.util.concurrent.atomic.AtomicLong;

/**
 * LongAdder
 */
public class Case3 {

    private static AtomicLong counter = new AtomicLong(0);

    private static void addOne() {
        counter.incrementAndGet();
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
     * time is about 6~7s
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
            i = counter.get();
        }
        long end = System.currentTimeMillis();
        System.out.println("total count = " + counter.get() + ", total time is " + ((end - start) / 1000));
    }
}
