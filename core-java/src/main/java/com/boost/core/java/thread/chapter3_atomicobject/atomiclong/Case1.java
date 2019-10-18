package com.boost.core.java.thread.chapter3_atomicobject.atomiclong;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 功能：统计0的个数
 *
 * 我们在看源码的时候，
 * 1.发现Unsafe.getUnsafe()是可以获取到Unsafe实例的，因为这个类在rt.jar下面，所以类就是通过BootStarp
 * 类加载器进行加载的。
 * 2.private volatile long value;声明为volatile的，为了保证在多线程下的可见性，具体存放计数的存量。
 * 3.
 */
public class Case1 {
    private static AtomicLong atomicLong = new AtomicLong();

    private static Integer[] arrayOne = new Integer[]{0, 1, 2, 3, 0, 5, 6, 0, 56, 0};
    private static Integer[] arrayTwo = new Integer[]{10, 1, 2, 3, 0, 5, 6, 0, 56, 0};

    /**
     * 在没有原子类的情况下，实现计数器需要使用一定的同步措施，比如使用synchronized关键字等，但是这些都是阻塞算法，
     * 对性能有一定损耗，而本章介绍的这些原子操作类都使用CAS非阻塞算法，性能更好。但是在高并发情况下AtomicLong还会存在
     * 性能问题。JDK 8提供了一个在高并发下性能更好的LongAdder类
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Thread threadOne = createThreadOne();
        Thread threadTwo = createThreadTwo();

        threadOne.start();
        threadTwo.start();

        threadOne.join();
        threadTwo.join();

        System.out.println("count 0:" + atomicLong.get());

    }

    private static Thread createThreadOne() {
        return new Thread(() -> {
            int size = arrayOne.length;
            for (int i = 0; i < size; i++) {
                if (arrayOne[i].intValue() == 0) {
                    atomicLong.incrementAndGet();
                }
            }
        });
    }

    private static Thread createThreadTwo() {
        return new Thread(() -> {
            int size = arrayTwo.length;
            for (int i = 0; i < size; i++) {
                if (arrayTwo[i].intValue() == 0) {
                    atomicLong.incrementAndGet();
                }
            }
        });
    }
}
