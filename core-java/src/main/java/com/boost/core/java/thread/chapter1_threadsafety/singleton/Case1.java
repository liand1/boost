package com.boost.core.java.thread.chapter1_threadsafety.singleton;


import java.util.Arrays;

/**
 * 线程安全的单例模式：
 * 阅读文章：http://www.cnblogs.com/xudong-bupt/p/3433643.html
 * 更好的是采用下面的方式，既不用加锁，也能实现懒加载
 *
 * 实际上是外部类被加载时内部类并不需要立即加载内部类，内部类不被加载则不需要进行类初始化
 */
public class Case1 {

    private Case1() {
        System.out.println("single");
    }

    private static class Inner {
        private static Case1 s = new Case1();
    }

    public static Case1 getSingle() {
        return Inner.s;
    }

    public static void main(String[] args) {
        Thread[] ths = new Thread[200];
        for(int i=0; i<ths.length; i++) {
            ths[i] = new Thread(()->{
                Case1.getSingle();
            });
        }

        Arrays.asList(ths).forEach(o->o.start());
    }
}
