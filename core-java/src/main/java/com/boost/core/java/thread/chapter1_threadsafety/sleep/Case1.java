package com.boost.core.java.thread.chapter1_threadsafety.sleep;

import java.util.concurrent.TimeUnit;

/**
 * 静态方法，阻塞当前线程，腾出cpu让给其它线程
 */
public class Case1 {

    public static void main(String[] args) throws Exception{
        Processor processor = new Processor();
        processor.setName("t");
        processor.start();

        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "----->" + i);
            TimeUnit.MILLISECONDS.sleep(500);
        }
    }
}

class Processor extends Thread {

    // Thread中的run方法不抛出异常，所以重写的run方法也不能使用throws,执行进行try/catch
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "----->" + i);
            try {
                TimeUnit.SECONDS.sleep(1);// 阻塞一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
