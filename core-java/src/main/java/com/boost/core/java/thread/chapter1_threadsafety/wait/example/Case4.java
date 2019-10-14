package com.boost.core.java.thread.chapter1_threadsafety.wait.example;

import java.util.concurrent.TimeUnit;

/**
 * 当前线程调用共享对象的wait()方法时，当前线程只会释放当前共享对象的锁，当前线程持有的其他共享对象的监视器锁并不会被释放。
 * 在上述代码中，在main函数中分别启动了A线程和B线程，为了让线程A先获取到资源，这里让线程B休眠了1s，线程A先获取到共享变量
 * resourceA和共享变量resourceB的锁，然后resourceA的wait()方法挂起了自己，并释放了resourceA的锁。
 *
 * 线程B休眠结束后，肯定先尝试获取resourceA上的锁，如果当时线程A还没有调用wait()方法释放该锁，则线程B会被阻塞，当线程A释
 * 放了resourceA上的锁后，线程B就会获取到resourceA上的锁，然后尝试获取resourceB上的锁。由于线程A调用的是resourceA上的
 * wait()方法，所以并没有将resourceB上的锁给释放掉，当线程B尝试获取resourceB上的锁时就会被阻塞。
 *
 */
public class Case4 {
    private static volatile Object resourceA = new Object();
    private static volatile Object resourceB = new Object();

    public static void main(String[] args) throws Exception {
        Thread threadA = createThreadA();
        Thread threadB = createThreadB();

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();

        System.out.println("main over");

    }

    public static Thread createThreadA() {
        return new Thread(() -> {
            try {
                // 获取resourceA共享资源的监视器锁
                synchronized (resourceA) {
                    System.out.println("threadA get resourceA lock");
                    // 获取resourceB共享资源的监视器锁
                    synchronized (resourceB) {
                        System.out.println("threadA get resourceB lock");
                        System.out.println("threadA release resourceA lock");
                        resourceA.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static Thread createThreadB() {
        return new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                // 获取resourceA共享资源的监视器锁
                synchronized (resourceA) {
                    System.out.println("threadB get resourceA lock");
                    System.out.println("threadB try get resourceB lock");
                    // 获取resourceB共享资源的监视器锁
                    synchronized (resourceB) {
                        System.out.println("threadB get resourceB lock");
                        System.out.println("threadB release resourceA lock");
                        resourceA.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

}
