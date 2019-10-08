package com.boost.core.java.thread.chapter1_threadsafety.daemon;

import java.util.concurrent.TimeUnit;

/**
 * 在Java中有两类线程：User Thread(用户线程)、Daemon Thread(守护线程)
 * 任何一个守护线程都是整个JVM中所有非守护线程的保姆,只要当前JVM实例中尚存在任何一个非守护线程没有结束，守护线程就全
 * 部工作；只有当最后一个非守护线程结束时，守护线程随着JVM一同结束工作。
 * Daemon的作用是为其他线程的运行提供便利服务，守护线程最典型的应用就是 GC (垃圾回收器)，它就是一个很称职的守护者。
 * User和Daemon两者几乎没有区别，唯一的不同之处就在于虚拟机的离开：如果 User Thread已经全部退出运行了，只剩下
 * Daemon Thread存在了，虚拟机也就退出了。 因为没有了被守护者，Daemon也就没有工作可做了，也就没有继续运行程序的必
 * 要了。
 * 守护线程里面的finally也不会执行。
 */
public class Case1 extends Thread {

    private int i = 0;

    @Override
    public void run() {
        try {
            while (true) {
                i++;
                System.out.println("i=" + i);
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("照样不会被执行");
        }
    }

    public static void main(String[] args) {
        try {
            Case1 thread = new Case1();
            thread.setDaemon(true);
            thread.start();
            TimeUnit.SECONDS.sleep(5);
            System.out.println("main线程离开，thread也不再打印了.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
