package com.boost.core.java.thread.chapter2_container.c5_blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

/**
 * LinkedTransferQueue
 */
public class Case4 {

    public static void main(String[] args) throws InterruptedException {
        TransferQueue<String> strs = new LinkedTransferQueue<>();

		new Thread(() -> {
			try {
				System.out.println(strs.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();

        strs.transfer("aaa");// 如果找不到消费者会阻塞

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //strs.put("aaa");// put 不会阻塞


        /*new Thread(() -> {
            try {
                System.out.println(strs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();*/
    }
}
