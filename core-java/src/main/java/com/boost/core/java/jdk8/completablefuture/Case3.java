package com.boost.core.java.jdk8.completablefuture;

import com.boost.core.java.jdk8.completablefuture.entity.Shop;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 如何进行异常处理 getPriceAsyncWithDealError()
 * 如果价格计算过程中产生了错误
 * 会怎样呢？非常不幸，这种情况下你会得到一个相当糟糕的结果：用于提示错误的异常会被限制
 * 在试图计算商品价格的当前线程的范围内，最终会杀死该线程，而这会导致等待get方法返回结
 * 果的客户端永久地被阻塞。
 *
 * 客户端可以使用重载版本的get方法，它使用一个超时参数来避免发生这样的情况。这是一
 * 种值得推荐的做法，你应该尽量在你的代码中添加超时判断的逻辑，避免发生类似的问题。使用
 * 这种方法至少能防止程序永久地等待下去，超时发生时，程序会得到通知发生了TimeoutException。不过，也因为如此，
 * 你不会有机会发现计算商品价格的线程内到底发生了什么问题
 * 才引发了这样的失效。为了让客户端能了解商店无法提供请求商品价格的原因，你需要使用
 * CompletableFuture的completeExceptionally方法将导致CompletableFuture内发生问
 * 题的异常抛出。
 */
public class Case3 {

    public static void main(String[] args) {

        Shop shop = new Shop("BestShop");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsyncWithDealError("my favorite product");
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
