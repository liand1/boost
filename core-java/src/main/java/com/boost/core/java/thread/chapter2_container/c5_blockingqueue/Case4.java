package com.boost.core.java.thread.chapter2_container.c5_blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

/**
 * LinkedTransferQueue
 */
public class Case4 {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> strs = new LinkedTransferQueue<>();

		new Thread(() -> {
			try {
				System.out.println(strs.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();

//        strs.transfer("aaa");// 如果找不到消费者会阻塞

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        strs.put("aaa");


        /*new Thread(() -> {
            try {
                System.out.println(strs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();*/
    }
}
