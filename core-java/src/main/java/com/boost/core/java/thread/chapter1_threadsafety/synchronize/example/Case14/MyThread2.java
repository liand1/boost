package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example.Case14;

public class MyThread2 extends Thread {

    private MyOneList list;

    public MyThread2(MyOneList list) {
        super();
        this.list = list;
    }

    @Override
    public void run() {
        MyService msRef = new MyService();
        msRef.addServiceMethod(list, "B");
    }
}
