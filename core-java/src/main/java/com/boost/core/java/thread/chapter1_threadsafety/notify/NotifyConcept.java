package com.boost.core.java.thread.chapter1_threadsafety.notify;

/**
 *notify可以唤醒一个因调用了wait方法而处于阻塞状态中的线程，使其进入就绪状态。被重新唤醒的线程试图重新获得临界
 * 区的控制权，也就是锁，并继续执行临界区内wait方法后的代码。如果发出notify操作时没有处于阻塞状态中线程，那么该
 * 命名会被忽略
 *
 */
public class NotifyConcept {


}
