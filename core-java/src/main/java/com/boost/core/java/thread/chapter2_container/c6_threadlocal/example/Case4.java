package com.boost.core.java.thread.chapter2_container.c6_threadlocal.example;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * InheritableThreadLocal可以在子线程中继承父线程的值
 * 如果要在子类中有其他逻辑，重写这个方法即可childValue(Long parentValue)
 * 如果子线程取得值的同时，主线程将InheritableThreadLocal的值进行了修改，那么子线程得到的值仍然是旧值。
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
