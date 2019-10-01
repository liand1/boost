package com.boost.core.java.thread.chapter2_container.example.Case3;

import java.util.ArrayList;
import java.util.List;


/**
 * 有N张火车票，每张票都有一个编号
 * 同时有10个窗口对外售票
 * 请写一个模拟程序
 *
 * 分析下面的程序可能会产生哪些问题？
 * 重复销售？超量销售？
 *
 */
public class TicketSeller1 {

    static List<String> tickets = new ArrayList<>();

    static {
        for(int i=0; i<10000; i++) tickets.add("票编号：" + i);
    }

    // 可能会抛出ArrayIndexOutOfBoundsException
    // 在起多个线程，还有最后一张票的情况下，多个线程都会去执行remove方法
    public static void main(String[] args) {
        for(int i=0; i<10; i++) {
            new Thread(()->{
                while(tickets.size() > 0) {
                    System.out.println("销售了--" + tickets.remove(0));
                }
            }).start();
        }
    }
}
