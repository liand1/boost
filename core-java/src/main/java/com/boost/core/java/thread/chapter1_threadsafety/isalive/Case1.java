package com.boost.core.java.thread.chapter1_threadsafety.isalive;

import java.util.concurrent.TimeUnit;

/**
 * 判断当前的线程是否处于活动状态, 活动状态代码已经启动并且尚未终止，
 * 线程处于正在运行或准备运行的状态，就认为是活动状态
 */
public class Case1 extends Thread{

    @Override
    public void run() {
        System.out.println("run = " + this.isAlive());// true
    }

    public static void main(String[] args) throws Exception {
        Case1 c = new Case1();
        System.out.println("begin =" + c.isAlive());// false
        c.start();
//        TimeUnit.MILLISECONDS.sleep(100);// 去掉注释，再看下面的打印结果，变成false, 是因为线程已经在100毫秒内执行完毕
        System.out.println("cnd =" + c.isAlive()); // true/false
    }
}
