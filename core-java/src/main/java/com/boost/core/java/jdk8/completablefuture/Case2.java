package com.boost.core.java.jdk8.completablefuture;

import com.boost.core.java.jdk8.completablefuture.entity.Shop;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 使用异步API  getPriceAsync
 */
public class Case2 {

    public static void main(String[] args) {

        Shop shop = new Shop("BestShop");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Invocation returned after " + invocationTime
                + " msecs");
        // 执行更多任务，比如查询其他商店
        //doSomethingElse();
        // 在计算商品价格的同时
        try {
            // 休眠了3秒，但这时price结果已经计算出来了，不会再阻塞
            TimeUnit.SECONDS.sleep(3);
            // 从Future对象中读取价格，如果价格未知，会发生阻塞
            double price = futurePrice.get();
            System.out.printf("Price is %.2f%n", price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs");

    }
}
