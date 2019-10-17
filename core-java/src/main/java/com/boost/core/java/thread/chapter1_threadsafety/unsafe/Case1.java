package com.boost.core.java.thread.chapter1_threadsafety.unsafe;

import sun.misc.Unsafe;

public class Case1 {

    static final Unsafe unsafe = Unsafe.getUnsafe();

    // 记录变量state在Case1中的偏移值
    static final long stateOffset;

    // 变量
    private volatile long state = 0;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset(Case1.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {
            System.out.println(e.getLocalizedMessage());
            throw new Error(e);
        }
    }

    /**
     * Exception in thread "main" java.lang.ExceptionInInitializerError
     * Caused by: java.lang.SecurityException: Unsafe
     * 查看Unsafe.getUnsafe()代码,判断是不是 Bootstrap类加载器加载的 localClass ,在这里是看是
     * 不是Bootstrap加载器加载了Case1.class o
     * 很明显由于TestUnSafe.class是使用AppClassLoader加载的 , 所以这里直接抛出了异常
     *
     * 如果没有代码的限制,那么我们的应用程序就可以随意使用 Unsafe 做事情了,
     * 而 Unsafe 类可以直接操作内存,这是不安全的,所以 JDK 开发组特意做了这个限制,不
     * 让开发人员在正规渠道使用Unsafe类 ,而是在此jar包里面的核心类中使用 Unsafe 功能 。
     * (可以通过反射来获取Unsafe实例:Case2)
     */
    public static void main(String[] args) {
        Case1 c = new Case1();
        // 设置值为1
        boolean success = unsafe.compareAndSwapInt(c, stateOffset, 0, 1);
        System.out.println("success=" + success);

    }
}
