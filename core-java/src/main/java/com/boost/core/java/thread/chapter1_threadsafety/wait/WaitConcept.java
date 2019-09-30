package com.boost.core.java.thread.chapter1_threadsafety.wait;


/**
 * 方法wait的作用是使当前执行代码的线程进行等待，该方法将当前线程置入"预执行队列"中
 * 。并且在wait所在的代码处停止执行，直到接到通过或者被中断为止。
 * 在调用wait之前，线程必须获得该对象的对象级别锁，即只能在同步方法或同步块中调用wait
 * 方法。在执行wait方法后，当前线程释放锁。在从wait返回前，线程与其他线程竞争重新获得锁。
 */
public class WaitConcept {
}
