package com.boost.core.java.thread.chapter2_container.c2_copyonwritelist;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 写时复制容器 copy on write
 * 比如当我们使用CopyOnWriteArrayList容器的时候，当我们往里面添加一个元素的时候，他会把这个容器
 * 直接复制一份，然后在这个复制的容器上面添加这个新元素，最后把引用指向这个新复制出来的容器，它de
 * 有点是读的时候不用加锁，但是写的效率是很低的
 *
 * 多线程环境下，写时效率低，读时效率高
 * 适合写少读多的环境
 *
 */
public class Case1 {

    public static void main(String[] args) {
        List<String> lists =
                //new ArrayList<>(); //这个会出并发问题！
                //new Vector();
                new CopyOnWriteArrayList<>();// 运行时间最慢
        Random r = new Random();
        Thread[] ths = new Thread[100];

        for(int i=0; i<ths.length; i++) {
            Runnable task = new Runnable() {

                @Override
                public void run() {
                    for(int i=0; i<1000; i++) lists.add("a" + r.nextInt(10000));
                }

            };
            ths[i] = new Thread(task);
        }


        runAndComputeTime(ths);

        System.out.println(lists.size());
    }

    static void runAndComputeTime(Thread[] ths) {
        long s1 = System.currentTimeMillis();
        Arrays.asList(ths).forEach(t->t.start());
        Arrays.asList(ths).forEach(t->{
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long s2 = System.currentTimeMillis();
        System.out.println(s2 - s1);

    }
}
