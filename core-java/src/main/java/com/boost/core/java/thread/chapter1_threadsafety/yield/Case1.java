package com.boost.core.java.thread.chapter1_threadsafety.yield;

/**
 * Thread类中有一个静态的yield方法，当一个线程调用yield方法时，实际就是暗示线程调度器当前请求让出自己的cpu使用，但是线
 * 程调度器可以无条件忽略这个暗示。
 * 操作系统为每一个线程分配一个时间片来占有cpu，正常情况下当一个线程把分配给自己的时间片用完之后，线程调度器才会进行下
 * 一轮的线程调度，而当一个线程调用了Thread类的静态方法yield时，是在告诉线程调度器自己占用的时间片还没用完，但是不想用
 * 了，让调度器现在就可以进行下一轮的线程调度
 * 当一个线程调用yield方法时，当前线程会让出cpu使用权，然后处于就绪状态，线程调度器会从线程就绪队列里面获取一个优先级
 * 最高的线程，但是也有可能又会调度到刚刚让出cpu的哪个线程。
 * 注意点：调用yield之后，线程是进入了就绪态，线程调度器下一次调度时还有可能调度到这个线程。 和调用sleep不同，sleep则
 * 是让线程阻塞挂起一段时间，期间线程不会被调度。
 *
 * 让出cpu一小会儿，给其它线程执行的机会,让出的时间不确定，有可能刚刚让出来，马上又获得CPU时间片
 *
 */
public class Case1 extends Thread {

    public Case1(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(getName() + ": " + i);
            if (i % 10 == 0) {
                yield();
            }
        }
    }

    // 只要到10的倍数，一定让出cpu
    public static void main(String[] args) {
        new Case1("t1").start();
        new Case1("t2").start();
    }
}
