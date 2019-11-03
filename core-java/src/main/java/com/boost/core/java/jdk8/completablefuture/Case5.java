package com.boost.core.java.jdk8.completablefuture;

import com.boost.core.java.jdk8.completablefuture.entity.Shop;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;

/**
 * 使用并行流对请求进行并行操作
 */
public class Case5 {

    List<Shop> shops = Arrays.asList(
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("bubugao"),
            new Shop("tongcheng"),
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("bubugao"),
            new Shop("tongcheng"),
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("bubugao"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("bubugao"),
            new Shop("tongcheng"),
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("bubugao"),
            new Shop("tongcheng"),
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("bubugao"),
            new Shop("tongcheng"),
            new Shop("BuyItAll"));

    // findPrices方法的执行时间仅比4秒钟多了那么几毫秒，因为对这4个商店
    // 的查询是顺序进行的，并且一个查询操作会阻塞另一个，每一个操作都要花费大约1秒左右的时
    // 间计算请求商品的价格
    public List<String> findPrices(String product) {
        return shops.stream().map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());
    }

    // 使用并行流进行操作
    public List<String> findPricesWithParallel(String product) {
        return shops.parallelStream().map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());
    }

    // 使用 CompletableFuture 发起异步请求,跟findPricesWithParallel比起来
    // 们看起来不相伯仲，究其原因都一样：它们内部
    //采用的是同样的通用线程池，默认都使用固定数目的线程，具体线程数取决于Runtime.
    //getRuntime().availableProcessors()的返回值然而，CompletableFuture具有一定的
    //优势，因为它允许你对执行器（Executor）进行配置，尤其是线程池的大小，让它以更适合应
    //用需求的方式进行配置，满足程序的要求，而这是并行流API无法提供的。让我们看看你怎样利
    //用这种配置上的灵活性带来实际应用程序性能上的提升。
    public List<String> findPricesWithCompletableFuture(String product) {
        List<CompletableFuture<String>> priceFutures =
                shops.stream()
                        .map(shop -> CompletableFuture.supplyAsync(
                                () -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))))
                        .collect(toList());
        // 等待所有异步操作结束
        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    /**
     * 使用定制的执行器
     * 线程池中线程的数目取
     * 决于你预计你的应用需要处理的负荷，但是你该如何选择合适的线程数目呢？
     *
     * 如果线程池中线程的数量过多，最终它们会竞争
     * 稀缺的处理器和内存资源，浪费大量的时间在上下文切换上。反之，如果线程的数目过少，正
     * 如你的应用所面临的情况，处理器的一些核可能就无法充分利用。Brian Goetz建议，线程池大
     * 小与处理器的利用率之比可以使用下面的公式进行估算：
     * Nthreads = NCPU * UCPU * (1 + W/C)
     * 其中：
     * ❑NCPU是处理器的核的数目，可以通过Runtime.getRuntime().availableProcessors()得到
     * ❑UCPU是期望的CPU利用率（该值应该介于0和1之间）
     * ❑W/C是等待时间与计算时间的比率
     * @param product
     * @return
     */
    public List<String> findPricesWithCompletableFuture2(String product) {
        List<CompletableFuture<String>> priceFutures =
                shops.stream()
                        .map(shop -> CompletableFuture.supplyAsync(
                                () -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)),executor))
                        .collect(toList());
        // 等待所有异步操作结束
        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    // 创建一个线程池，线程池中线程的数目为100和商店数目二者中较小的一个值
    private final Executor executor =
            Executors.newFixedThreadPool(Math.min(shops.size(), 100),
                    r -> {
                        Thread t = new Thread(r);
                        // 使用守护线程——这种方式不会阻止程序的关停
                        t.setDaemon(true);
                        return t;
                    });

    public static void main(String[] args) {
        Case5 c = new Case5();
        long start,duration;
// 最慢
//        start = System.currentTimeMillis();
//        System.out.println(c.findPrices("myPhone27S"));
//        duration = (System.currentTimeMillis() - start);
//        System.out.println("findPrices Done in " + duration + " msecs");

        start = System.currentTimeMillis();
        System.out.println(c.findPricesWithParallel("myPhone27S"));
        duration = (System.currentTimeMillis() - start);
        System.out.println("findPricesWithParallel Done in " + duration + " msecs");

        start = System.currentTimeMillis();
        System.out.println(c.findPricesWithCompletableFuture("myPhone27S"));
        duration = (System.currentTimeMillis() - start);
        System.out.println("findPricesWithCompletableFuture Done in " + duration + " msecs");

        start = System.currentTimeMillis();
        System.out.println(c.findPricesWithCompletableFuture2("myPhone27S"));
        duration = (System.currentTimeMillis() - start);
        System.out.println("findPricesWithCompletableFuture Done in " + duration + " msecs");
    }
}
