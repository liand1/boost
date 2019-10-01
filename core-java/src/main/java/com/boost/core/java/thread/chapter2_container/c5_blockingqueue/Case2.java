package com.boost.core.java.thread.chapter2_container.c5_blockingqueue;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * ArrayBlockingQueue
 */
public class Case2 {

    static BlockingQueue<String> strs = new ArrayBlockingQueue<>(10);

    static Random r = new Random();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            strs.put("a" + i);
        }

        strs.put("aaa"); //满了就会等待，程序阻塞, 无限制的阻塞下去
        //strs.add("aaa");// 报异常
        boolean s = strs.offer("aaa");// 通过返回值判断是否加成功
        //strs.offer("aaa", 1, TimeUnit.SECONDS);

        System.out.println(strs);
    }
}
