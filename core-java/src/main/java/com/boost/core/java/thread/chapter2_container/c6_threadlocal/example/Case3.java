package com.boost.core.java.thread.chapter2_container.c6_threadlocal.example;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 验证线程变量的隔离性，发现main线程和t1线程各自拥有各自的值
 */
public class Case3 {

    public static void main(String[] args) {
        try {
            getT1().start();

            TimeUnit.SECONDS.sleep(1);

            for (int i = 0; i < 10; i++) {
                System.out.println("---------------------main.get = " + Tools.tl.get());
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static Thread getT1() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        System.out.println("t1.get = " + Tools.tl.get());
                        TimeUnit.SECONDS.sleep(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

class Tools {
    public static ThreadLocalExt tl = new ThreadLocalExt();
}

class ThreadLocalExt extends ThreadLocal<Long> {

    @Override
    protected Long initialValue() {
        return new Date().getTime();
    }
}