package com.boost.core.java.thread.chapter1_threadsafety.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class Case2 {
    static final Unsafe unsafe;

    // 记录变量state在Case1中的偏移值
    static final long stateOffset;

    // 变量
    private volatile long state = 0;

    static {
        try {
            // 使用反射获取Unsafe的成员交量 theUnsafe
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            // 设置为可存取
            field.setAccessible(true);
            // 获取该变量的值
            unsafe = (Unsafe)field.get(null);
            // 获取state在Case2中的偏移量
            stateOffset = unsafe.objectFieldOffset(Case2.class.getDeclaredField("state"));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            throw new Error(e);
        }
    }

    public static void main(String[] args) {
        Case2 c = new Case2();
        // 设置值为1
        boolean success = unsafe.compareAndSwapInt(c, stateOffset, 0, 1);
        System.out.println("success=" + success);

    }
}
