package com.boost.core.java.thread.chapter5_advance.locksupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Case3 {


    public static void main(String[] args) throws Exception{
        case2();

    }

    /**
     * 码首先创建了一个子线程 threa ，子线程启动后调用 park 方法，由于在默认情
     * 况下子线程没有持有许可证，因而它会把自己挂起。
     * 主线程休眠 是为了让主线程调用 unpark 方法前让子线程输出 child thread begin
     * park 并阻塞。
     * 主线程然后执行 unpark 方法，参数为子线程，这样做的目的是让子线程持有许可证，
     * 然后子线程调用 park 方法就返回了。
     *
     * ch ld thread begin park'
     * main thread begin unpark 1
     * child thread unpark1
     *
     *
     */
    public static void case1() throws InterruptedException{
        Thread thread = new Thread(() -> {
            System.out.println("child thread begin park!");
            //调用 park方法，挂起自己
            LockSupport.park();
            System.out.println("child thread unpark!");
        });

        thread.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("main thread begin unpark!");
        // 调用 unpark方法让thread线程持有许可证，然后park方法返回
        LockSupport.unpark(thread);
    }

    /**
     * park 方法返回时不会告诉你因何种原因返回，所以调用者需要根据之前调用 park方法的
     * 原因，再次检查条件是否满足，如果不满足 还需要再次调用 park 方法
     * @throws InterruptedException
     */
    public static void case2() throws InterruptedException{
        Thread thread = new Thread(() -> {
            System.out.println("child thread begin park!");
            //调用 park方法，挂起自己，只有被中断才会退出循环
            while(!Thread.currentThread().isInterrupted()) {
                LockSupport.park();
            }
            System.out.println("child thread unpark!");
        });

        thread.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("main thread begin unpark!");
        // 中断子线程
        thread.interrupt();
    }
}
