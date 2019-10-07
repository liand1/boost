package com.boost.core.java.thread.chapter1_threadsafety.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 调用interrupt方法不是真正的停止线程
 */
public class Case1 extends Thread{

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 500000; i++) {
            System.out.println("i=" + (i+1));
        }
    }

    public static void main(String[] args) {
        try {
            Case1 c = new Case1();
            c.start();
            TimeUnit.SECONDS.sleep(2);
            // 并没有马上停止循化，并且循环执行完毕，调用interrupt方法仅仅是在当前线程中打了一个停止的标记，并
            // 不是真正的停止线程
            c.interrupt();
        } catch (InterruptedException e) {
            System.out.println("main catch");
            e.printStackTrace();
        }
    }
}
