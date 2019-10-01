package com.boost.core.java.thread.chapter2_container.c5_blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * SynchronusQueue 容量为0
 */
public class Case5 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> strs = new SynchronousQueue<>();

        new Thread(() -> {
            try {
                System.out.println(strs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        TimeUnit.SECONDS.sleep(5);
        strs.put("aaa"); //阻塞等待消费者消费
        //strs.add("aaa");
        System.out.println(strs.size());
    }
}
