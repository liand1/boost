package com.boost.core.java.thread.chapter1_threadsafety.currentthread.simpledataformat;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * SimpleDateFormat 非线程安全
 */
public class Case1 {

    public static void main(String[] args) {
//        errorOperate();
        rightOperate();
    }

    /**
     * 有可能会报错java.lang.NumberFormatException: For input string: ""
     */
    private static void errorOperate() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<String> dateStrList = Arrays.asList(
                "2018-04-01 10:00:01",
                "2018-04-02 11:00:02",
                "2018-04-03 12:00:03",
                "2018-04-04 13:00:04",
                "2018-04-05 14:00:05"
        );
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for (String str : dateStrList) {
            executorService.execute(() -> {
                try {
                    simpleDateFormat.parse(str);
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static void rightOperate() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<String> dateStrList = Arrays.asList(
                "2018-04-01 10:00:01",
                "2018-04-02 11:00:02",
                "2018-04-03 12:00:03",
                "2018-04-04 13:00:04",
                "2018-04-05 14:00:05"
        );
        for (String str : dateStrList) {
            executorService.execute(() -> {
                try {
                    //创建新的SimpleDateFormat对象用于日期-时间的计算
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    simpleDateFormat.parse(str);
                    TimeUnit.SECONDS.sleep(1);
                    simpleDateFormat = null; //销毁对象
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

}

