package com.boost.core.java.thread.chapter1_threadsafety.volatil;

import java.util.concurrent.TimeUnit;

/**
 * volatile关键字，使一个变量在多线程间可见
 * A.B线程都用到一个变量，java默认是A线程中保留一份copy，这样如果B线程修改了该变量，则A线程未必知道
 * 使用Volatile关键字，会让所有线程都会读到变量的修改值
 * volatile关键字并不能保证多个线程共同修改running变量时所带来的不一致问题，也就是说volatile不能替代synchronized
 */
public class VolatileConcept {
    // 对比一下有无Volatile的情况的结果
    /*volatile*/ boolean running = true;

    void m() {
        System.out.println("m start");
        while(running) {
            // 在线程空闲的时候，是有可能去读主内存的数据的
            //try {
            //    TimeUnit.MILLISECONDS.sleep(10);
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }
        }
        System.out.println("m end!");
    }

    // 在JMM（java内存模型中），在多线程运行的情况下，在主内存中的变量，会读到cpu的缓冲区中，
    // 这样变量就不会经常去内存中读了，下面的代码逻辑如下，cpu在运行第一个线程的时候变量被
    // copy到一个cpu的缓存中去了， 这时候running = true,这时候cpu在运行主线程的时候修改了
    // 变量，另外一个cpu修改了变量running = false，并且写回到内存中,但是现在第一个cpu的缓冲区
    // 还是true，因为它没有去读内存里面这个变量。
    // 加了volatile后，当修改了变量之后，就会通知其它线程的缓冲区，告诉他们这个变量已经过期了，
    // 需要重新去主内存里面去读
    public static void main(String[] args) {
        VolatileConcept vc = new VolatileConcept();
        new Thread(vc::m, "v").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        vc.running = false;
    }
}
