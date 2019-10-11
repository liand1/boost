package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example.Case14;

import java.util.ArrayList;
import java.util.List;

public class MyOneList {
    private List list = new ArrayList();

    public synchronized void add(String data) {
        list.add(data);
    }

    public synchronized int getSize() {
        return list.size();
    }
}
