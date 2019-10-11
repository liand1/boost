package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example.Case14;

import java.util.concurrent.TimeUnit;

public class MyService {

    public MyOneList addServiceMethod(MyOneList list, String data){

        try {
            /*synchronized (list) {*/
                if(list.getSize()<1) {
                    TimeUnit.SECONDS.sleep(2);// 模拟读取数据
                    list.add(data);
                }
            /*}*/
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }
}
