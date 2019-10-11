package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example.Case15;

public class ThreadB extends Thread {

    private Service service;

    public ThreadB(Service service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.methodB();
    }
}
