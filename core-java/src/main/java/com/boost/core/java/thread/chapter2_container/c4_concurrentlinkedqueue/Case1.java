package com.boost.core.java.thread.chapter2_container.c4_concurrentlinkedqueue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Case1 {

    public static void main(String[] args) {
        Queue<String> strs = new ConcurrentLinkedQueue<>();

        for(int i=0; i<10; i++) {
            strs.offer("a" + i);  //add
        }

        System.out.println(strs);

        System.out.println(strs.size());

        System.out.println(strs.poll());// remove
        System.out.println(strs.size());

        System.out.println(strs.peek());// get but not remove
        System.out.println(strs.size());

        // 双端队列
//        strs = new ConcurrentLinkedDeque<>();
    }
}
