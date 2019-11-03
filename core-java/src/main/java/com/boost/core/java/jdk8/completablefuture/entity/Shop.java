package com.boost.core.java.jdk8.completablefuture.entity;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Shop {

    private String name;

    ThreadLocalRandom random =  ThreadLocalRandom.current();

    public Shop(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    private double calculatePrice(String product) {
        delay();
        //if (0/0 ==0)return 0.0;// throw Exception
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    // 模拟的延迟
    public static void delay() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Future<Double> getPriceAsync(String product) {
        // 创建CompletableFuture 对象，它会包含计算的结果
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        // 在另一个线程中以异步方式执行计算
        new Thread( () -> {
            double price = calculatePrice(product);
            // 需长时间计算的任务结束并得出结果时，设置Future的返回值
            futurePrice.complete(price);
        }).start();
        // 无需等待还没结束的计算，直接返回Future对象
        return futurePrice;
    }

    public Future<Double> getPriceAsyncWithDealError(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread( () -> {
            try {
                // 如果价格计算正常结束，完成Future操作并设置商品价格
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Exception ex) {
                // 否则就抛出导致失败的异常，完成这次Future操作
                futurePrice.completeExceptionally(ex);
            }
        }).start();
        return futurePrice;
    }

    /**
     * 精简了代码，等同于getPriceAsyncWithDealError()
     * @param product
     * @return
     */
    public Future<Double> getPriceAsyncWithSupplyAsync(String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }
}
