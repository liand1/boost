package com.boost.core.java.thread.chapter1_threadsafety.unsafeobject.random;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 阅读源码
 */
public class Case1 {
    public static void main(String[] args) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextInt(5));
        }
    }
}
