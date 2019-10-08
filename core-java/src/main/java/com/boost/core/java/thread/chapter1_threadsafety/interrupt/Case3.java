package com.boost.core.java.thread.chapter1_threadsafety.interrupt;

import java.util.concurrent.TimeUnit;


/**
 * 判断线程是否是停止状态 isInterrupted
 * interrupted()测试当前线程是否已经中断，执行后具有将状态标志清除为false的功能
 * this.isInterrupted()测试线程Thread对象是否已经是中断状态，但不清除标志
 */
public class  Case3 extends Thread  {

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 500000; i++) {
            System.out.println("i=" + (i + 1));
        }
    }

    public static void main(String[] args) {
        try {
            Case3 c = new Case3();
            c.start();
            TimeUnit.SECONDS.sleep(1);
            c.interrupt();

            System.out.println("c.isInterrupted(): " + c.isInterrupted());// true
            System.out.println("c.isInterrupted(): " + c.isInterrupted());// true

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end");
    }
}
