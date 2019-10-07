package com.boost.core.java.thread.chapter1_threadsafety.join;

import java.util.concurrent.TimeUnit;

/**
 * join方法的意思是将调用该发放的线程合并到执行代码块的线程里面去
 * 举个例子，下面的代码中我们可以知道有两个线程，main线程和t线程
 * 当在main里面调用t.join()后，将t线程合并到main线程，也就是将
 * 两个线程合并成一个线程，也就不会出现并行运行的状态，所以会等到
 * t线程里面的代码执行完毕之后再去执行t.join()后面的代码
 */
public class Case1 {

    public static void main(String[] args) {
        MyRunnable m = new MyRunnable();
        Thread t = new Thread(m, "MyRunnable-Thread");
        t.start();

        for (int i = 0; i < 30; i++) {
            // currentThread()返回代码段正在被哪个线程调用的信息
            System.out.println(Thread.currentThread().getName() + "--" + i);
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(i == 10) {
                try {
                    // 等待t线程执行完毕,可以想象成插队，当main线程执行到第10次的时候
                    // t插队进来，并且要等到t执行完毕之后才开始执行join后面的代码
                    t.join();
                    System.out.println("不要插队，好么");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class MyRunnable implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 30; i++) {
            System.out.println(Thread.currentThread().getName() + "--" + i);
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
