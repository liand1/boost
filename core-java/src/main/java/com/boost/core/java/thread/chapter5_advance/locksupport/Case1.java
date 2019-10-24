package com.boost.core.java.thread.chapter5_advance.locksupport;

import java.util.concurrent.locks.LockSupport;

/**
 * void park() 方法
 * 如果调用 park() 的线程已经拿到了与 LockSupport 关联的许可证，则调用 LockSupport.park() 会马上返回，否者调用线程会被
 * 禁止参与线程的调度，也就是会被阻塞挂起,如下代码，直接在 main 函数里面调用 park 方法，最终结果只会输出begin park!，然
 * 后当前线程会被挂起，这是因为默认下调用线程是不持有许可证的。
 *
 * 在其它线程调用 unpark(Thread thread) 方法并且当前线程作为参数时候，调用park方法被阻塞的线程会返回。
 */
public class Case1 {

    public static void main(String[] args) {
        System.out.println( "begin park!" );

        LockSupport.park();

        System.out.println( "end park!" );// not print
    }
}
