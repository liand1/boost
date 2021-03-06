package com.boost.core.java.thread.chapter2_container.c6_threadlocal.example;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * InheritableThreadLocal可以在子线程中继承父线程的值
 * 如果要在子类中有其他逻辑，重写这个方法即可childValue(Long parentValue)
 * 如果子线程取得值的同时，主线程将InheritableThreadLocal的值进行了修改，那么子线程得到的值仍然是旧值。
 *
 * 查看Thread的源码，找到init方法，有如下代码块
 * if (inheritThreadLocals && parent.inheritableThreadLocals != null)
 *             this.inheritableThreadLocals =
 *                 ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);
 * 当父线程创建子线程时，构造函数会把父线程中inheritableThreadLocals变量里面的本地变量复制一份保存到子线程的
 * inheritableThreadLocals变量里面
 *
 * 那么在什么情况下需要子线程可以获取父线程的threadLocals变量呢？情况还是蛮多的，比如子线程需要使用存放在threadLocals
 * 变量中的用户登录信息，再比如一些中间件需要把同意的id追踪的整个调用链路记录下来。其实子线程使用父线程中的threadLocals
 * 方法有多种方式，比如创建线程时传入父线程中的变量，并将其复制到子线程中，或者再父线程中构造一个map作为参数传递给子线
 * 程，但是这些都改变了我们的使用习惯，所以在这些情况下InheritableThreadLocal就显得比较有用。

 */
public class Case4 {

    public static void main(String[] args) {
        try {

            for (int i = 0; i < 5; i++) {
                System.out.println("---------------------main.get = " + InheritableThreadLocalTools.tl.get());
                TimeUnit.SECONDS.sleep(1);
            }

            getT1().start();
            TimeUnit.SECONDS.sleep(1);
            InheritableThreadLocalTools.tl.set(1L);
            System.out.println("---------------------main.get = " + InheritableThreadLocalTools.tl.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Thread getT1() {
        return new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println("t1.get = " + InheritableThreadLocalTools.tl.get());
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}

class InheritableThreadLocalExt extends InheritableThreadLocal<Long> {

    @Override
    protected Long initialValue() {
        return new Date().getTime();
    }

    /**
     * 如果要在子类中有其他逻辑，重写这个方法即可
     * @param parentValue
     * @return
     */
    @Override
    protected Long childValue(Long parentValue) {
        return parentValue + 1L;
    }

}

class InheritableThreadLocalTools {
    public static InheritableThreadLocalExt tl = new InheritableThreadLocalExt();
}
