package com.boost.core.java.thread.chapter1_threadsafety.basicconcept;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 三种创建方式
 * 实现Runnable接口
 * 使用FutureTask方式
 */
public class How2Create {

    /**
     * 建完Thread对象后该线程并没有被启动执行，直到调用了start方法后才真正启动了
     * 线程。
     * 其实调用 start 方法后线程并没有马上执行而是处于就绪状态， 这个就绪状态是指该
     * 线程已经获取了除 CPU 资源外的其它资源，等待获取 CPU 资源后才会真正处于运行状态。
     * 一旦 run 方法执行完毕， 该线程就处于终止状态
     * @param args
     */
    public static void main(String[] args) {
        //extend Thread
        new CreateByExtendThread().start();

        //impl Runnable
        CreateByImpRunnable impRunnable = new CreateByImpRunnable();
        new Thread(impRunnable).start();
        new Thread(impRunnable).start();

        //FutureTask
        // 创建异步任务
        FutureTask<String> futureTask  = new FutureTask<>(new CallerTask());
        //启动线程
        new Thread(futureTask).start();
        try {
            String result = futureTask.get();
            System.out.println("result=" + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}

/**
 *  继承Thread: 在run（）方法内获取当前线程直接使用this就可以了，无须使用Thread.currentThread（）方法；
 *  不好的地方是Java不支持多继承，如果继承了Thread类，那么就不能再继承其他类。另外任务与代码没有分离，
 *  当多个线程执行一样的任务时需要多份任务代码，而Runnable则没有这个限制。
 */
class CreateByExtendThread extends Thread{
    @Override
    public void run() {
        System.out.println("I am a CreateByExtendThread thread.");
    }
}

class CreateByImpRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("I am a CreateByImpRunnable thread.");
    }
}

/**
 * 前两种方式都没办法拿到任务的返回结果，但是FutureTask方式可以
 */
class CallerTask implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "hello";
    }
}
