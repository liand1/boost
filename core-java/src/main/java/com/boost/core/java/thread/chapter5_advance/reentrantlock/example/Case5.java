package com.boost.core.java.thread.chapter5_advance.reentrantlock.example;


import java.util.concurrent.locks.ReentrantLock;

/**
 * reentrantlock用于替代synchronized
 * 由于m1锁定this,只有m1执行完毕的时候,m2才能执行
 * 这里是复习synchronized最原始的语义
 *
 * 使用reentrantlock可以完成同样的功能
 * 需要注意的是，必须要必须要必须要手动释放锁（重要的事情说三遍）
 * 使用syn锁定的话如果遇到异常，jvm会自动释放锁，但是lock必须手动释放锁，因此经常在finally中进行锁的释放
 *
 * 使用reentrantlock可以进行“尝试锁定”tryLock，这样无法锁定，或者在指定时间内无法锁定，线程可以决定是否继续等待
 *
 * 使用ReentrantLock还可以调用lockInterruptibly方法，可以对线程interrupt方法做出响应，
 * 在一个线程等待锁的过程中，可以被打断
 *
 * ReentrantLock还可以指定为公平锁(谁等的时间长，让谁得到这把锁), 先到先得（顾名思义，非公平锁的意思就是先来的不一定先
 * 得到锁，这个方式可能造成某些线程一直拿不到锁,结果也就是不公平的了）
 * synchronized不是非公平锁，但是它的效率比较高，因为它不会去计算谁的等待时间长，都是竞争式的，谁得到这把锁完全
 * 取决于线程调度器
 *
 */
public class Case5 extends Thread{

    private static ReentrantLock lock=new ReentrantLock(true); //参数为true表示为公平锁，请对比输出结果
    public void run() {
        for(int i=0; i<100; i++) {
            lock.lock();
            try{
                System.out.println(Thread.currentThread().getName()+"获得锁");
            }finally{
                lock.unlock();
            }
        }
    }
    public static void main(String[] args) {
        Case5 rl=new Case5();
        Thread th1=new Thread(rl);
        Thread th2=new Thread(rl);
        th1.start();
        th2.start();
    }
}
