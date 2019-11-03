package com.boost.core.java.jdk8.completablefuture;

import com.boost.core.java.jdk8.completablefuture.entity.Shop;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 使用工厂方法supplyAsync创建CompletableFuture  : getPriceAsyncWithSupplyAsync()
 * supplyAsync方法接受一个生产者（Supplier）作为参数，返回一个CompletableFuture
 * 对象，该对象完成异步执行后会读取调用生产者方法的返回值。生产者方法会交由ForkJoinPool
 * 池中的某个执行线程（Executor）运行，但是你也可以使用supplyAsync方法的重载版本，传
 * 递第二个参数指定不同的执行线程执行生产者方法。一般而言，向CompletableFuture的工厂
 * 方法传递可选参数，指定生产者方法的执行线程是可行的
 * 此外，代码中shop.getPriceAsyncWithSupplyAsync方法返回的CompletableFuture对象和代码清单
 * shop.getPriceAsyncWithDealError中你手工创建和完成的CompletableFuture对象是完全等价的，这意味着它提供了同样的
 * 错误管理机制，而前者你花费了大量的精力才得以构建。
 */
public class Case4 {

    public static void main(String[] args) {

        Shop shop = new Shop("BestShop");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsyncWithSupplyAsync("my favorite product");
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
