package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;

import java.util.concurrent.TimeUnit;

/**
 *   死锁的产生必须具备以下四个条件
 *   1）互斥条件：指进程对所分配到的资源进行排它性使用，即在一段时间内某资源只由一个进程占用。如果此时还有其它进程请求资
 *      源，则请求者只能等待，直至占有资源的进程用毕释放。
 *   2）请求和保持条件：指进程已经保持至少一个资源，但又提出了新的资源请求，而该资源已被其它进程占有，此时请求进程阻塞，
 *      但又对自己已获得的其它资源保持不放。
 *   3）不剥夺条件：指进程已获得的资源，在未使用完之前，不能被剥夺，只能在使用完时由自己释放。
 *   4）环路等待条件：指在发生死锁时，必然存在一个进程——资源的环形链，即进程集合{P0，P1，P2，···，Pn}中的P0正在等待
 *      一个P1占用的资源；P1正在等待P2占用的资源，……，Pn正在等待已被P0占用的资源。
 *
 *
 * 1线程调度器先调度了线程A，也就是把CUP资源分配给了A，A使用synchronized (resourceA)获取到了resourceA的监视器锁，然后
 *  休眠1秒，主要目的是为了保证线程B先获得resourceB的锁。
 * 2线程调用sleep方法休眠1秒后，会去执行 synchronized (resourceB)方法，企图获取resourceB的锁，而现在resourceB已经被
 *  B所持有，所以A会被阻塞而等待。而同时B休眠结束后会企图获得resourceA的锁，而现在resourceA被A所持有，这样就陷入了相互
 *  等待的状态， 也就形成了死锁， 下面谈谈本例是如何满足死锁的四个条件的
 *
 *  .首先resourceA和resourceB都是互斥资源，当A调用synchronized (resourceA)方法获取到resourceA上的监视器锁并释放前，A
 *      再调用synchronized (resourceA)尝试获取该资源会被阻塞，只有A主动释放该锁，B才能获得，这满足了互斥条件
 *  .A首先通过synchronized (resourceA)获取到了resourceA上的监视器锁资源，然后通过synchronized (resourceB)方法等待获取
 *      resourceB上的监视器锁资源，这就构成了请求并持有锁条件
 *  .A在获取resourceA上的监视器锁资源后，该资源不会被线程B掠夺走，只有线程A自己主动释放resourceA资源时，它才会放弃该资
 *      源的特有权，这构成了资源的不可剥夺条件。
 *  .A持有resourceA资源并等待获取resourceB资源，而线程B持有resourceB资源并等待获取resourceA资源，这构成了环路等待条件。
 *      所有线程A和线程B就进入了死锁状态。
 */
public class Case17 {

    private static Object resourceA = new Object();
    private static Object resourceB = new Object();

    public static void main(String[] args) {
        getThreadA().start();
        getThreadB().start();
    }

    private static Thread getThreadA() {
        return new Thread(()->{
           synchronized (resourceA) {
               System.out.println(Thread.currentThread().getName() + "get ResourceA");

               try {
                   TimeUnit.SECONDS.sleep(1);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

               System.out.println(Thread.currentThread().getName() + "waiting get ResourceB");
               synchronized (resourceB) {
                   System.out.println(Thread.currentThread().getName() + "get ResourceB");
               }
           }
        });
    }

    private static Thread getThreadB() {
        return new Thread(()->{
            synchronized (resourceB) {
                System.out.println(Thread.currentThread().getName() + "get ResourceB");

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "waiting get ResourceA");
                synchronized (resourceA) {
                    System.out.println(Thread.currentThread().getName() + "get ResourceA");
                }
            }
        });
    }
}
