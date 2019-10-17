package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;

import java.util.concurrent.TimeUnit;

/**
 * 指令重排 在java程序中，JMM想尽了各种方法来提高其性能，在软硬结合的情况下，是的JAVA程序能够高效且安全的运行。本文
 * 叙述的指令重排序则是JAVA编译器和处理器为了优化程序性能而对指令序列进行重新排序的一种手段。 ​ 在保证最终结果不受影
 * 响的情况下进行指令的重排序来提高程序运行的效率。 ​指令重排序需要满足以下两个条件：
 * 1)在单线程环境下不能改变程序运行的结果；
 * 2)在数据依赖关系的不允许重排序；
 *
 * 举一个例子： int a=1;// line1 int b=2;// line2 int c=a+b;// line3 在如上代码中,变量c的值依赖a和b的值,
 * 所以重排序后能够保证(3)的操作在(2)(1)之后,但是(1)(2)谁先执行就不一定了,
 * 这在单线程下不会存在问题,因为并不影响最终结果 。
 */
public class Case18 extends Thread {

    /**
     * 首先这段代码里面的变量没有被声明为volatile的,也没有使用任何同步措施,所以 在多线程下存在共享变量内存可见性问题。这里先不谈内存可见性问题,因为通过把变量
     * 声明为volatile的本身就可以避免指令重排序问题。
     */
    private static int num = 0;
    private static boolean ready = false;

    public static class ReadThread extends Thread {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (ready) {// (1)
                    System.out.println(num + num);// (2)
                }
                System.out.println("read thread...");
            }
        }
    }

    public static class WriteThread extends Thread {

        @Override
        public void run() {
            num = 2;// (3)
            ready = !ready;// (4)
            System.out.println("writeThread set over");
        }
    }

    /**
     *这里先看看指令重排序会造成什么影响,如上代码在不考虑、内存可见性问题的情况下
     * 一定会输出4?答案是不一定,由于代码(1)(2)(3)(4)之间不存在依赖关系,所以
     * 写线程的代码(3)(4)可能被重排序为先执行(4)再执行(3),那么执行(4)后,读
     * 线程可能已经执行了(1)操作,并且在(3)执行前开始执行(2)操作,这时候输出结
     * 果为0而不是4.
     *
     * 重排序在多线程下会导致非预期的程序执行结果,而使用volatile修饰ready就可以避免重排序和内存可见性问题.写
     * volatile变量时,可以确保volatile写之前的操作不会被编译器重排序到volatile写之后.读volatile变量时,可以确
     * 保volatile读之后的操作不会被编译器重排序到volatile读之前.
     */
    public static void main(String[] args) throws Exception {
        ReadThread rt = new ReadThread();
        rt.start();

        WriteThread wt = new WriteThread();
        wt.start();

        TimeUnit.MILLISECONDS.sleep(10);
        rt.interrupt();
        System.out.println("main exit");
    }
}
