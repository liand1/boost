package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;

import java.util.concurrent.TimeUnit;

/**
 * 运行jps，找到运行的Case16线程
 * 输入jstack -l 5280
 * Java stack information for the threads listed above:
 * ===================================================
 * "Thread-1":
 *         at com.ultra.boost.multithread.synchronize.Case16.run(Case16.java:40)
 *         - waiting to lock <0x000000076d4bfbe8> (a java.lang.Object)
 *         - locked <0x000000076d4bfbf8> (a java.lang.Object)
 *         at java.lang.Thread.run(Thread.java:748)
 * "Thread-0":
 *         at com.ultra.boost.multithread.synchronize.Case16.run(Case16.java:26)
 *         - waiting to lock <0x000000076d4bfbf8> (a java.lang.Object)
 *         - locked <0x000000076d4bfbe8> (a java.lang.Object)
 *         at java.lang.Thread.run(Thread.java:748)
 *
 * Found 1 deadlock.
 * 相互等待对方导致的死锁
 */
public class Case16 implements Runnable {

    public String username;
    public Object lock1 = new Object();
    public Object lock2 = new Object();

    public void setFlag(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        if("a".equals(username)) {
            synchronized (lock1) {
                try {
                    System.out.println("username=" + username);
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println("lock1 -> lock2");
                }
            }
        }

        if("b".equals(username)) {
            synchronized (lock2) {
                try {
                    System.out.println("username=" + username);
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                    System.out.println("lock2 -> lock1");
                }
            }
        }

    }

    public static void main(String[] args) {
        try {
            Case16 t1 = new Case16();
            t1.setFlag("a");
            Thread thread1 = new Thread(t1);
            thread1.start();
            TimeUnit.MILLISECONDS.sleep(100);

            t1.setFlag("b");
            Thread thread2 = new Thread(t1);
            thread2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
