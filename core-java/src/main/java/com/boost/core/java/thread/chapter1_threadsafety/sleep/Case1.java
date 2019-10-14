package com.boost.core.java.thread.chapter1_threadsafety.sleep;

import java.util.concurrent.TimeUnit;

/**
 * 静态方法，阻塞当前线程，腾出cpu让给其它线程
 * 当一个执行中的线程调用了Thread的sleep方法后，调用线程会暂时让出指定时间的执行权，
 * 也就是在这期间不参与CPU的调度，但是该线程所拥有的监视器资源，比如锁还是持有不让出的。
 * 指定的睡眠时间到了后该函数会正常返回，线程就处于就绪状态，然后参与CPU的调度,获取到CPU的资源后就可以继续运行了。
 * 如果在睡眠期间其他线程调用了该线程的interrupt方法中断了该线程则该线程会在调用sleep方法的地方抛出
 * InterruptedException异常而返回
 */
public class Case1 {

    public static void main(String[] args) throws Exception{
        Processor processor = new Processor();
        processor.setName("t");
        processor.start();

        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "----->" + i);
            TimeUnit.MILLISECONDS.sleep(500);
        }
    }
}

class Processor extends Thread {

    // Thread中的run方法不抛出异常，所以重写的run方法也不能使用throws,执行进行try/catch
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "----->" + i);
            try {
                TimeUnit.SECONDS.sleep(1);// 阻塞一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
