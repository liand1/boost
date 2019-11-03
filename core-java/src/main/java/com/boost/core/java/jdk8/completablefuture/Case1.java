package com.boost.core.java.jdk8.completablefuture;

import java.util.concurrent.*;

/**
 * Future 接口
 * Future接口在Java 5中被引入，设计初衷是对将来某个时刻会发生的结果进行建模。它建模
 * 了一种异步计算，返回一个执行运算结果的引用，当运算结束后，这个引用被返回给调用方。在
 * Future中触发那些潜在耗时的操作把调用线程解放出来，让它能继续执行其他有价值的工作，
 * 不再需要呆呆等待耗时的操作完成
 *
 * Future 接口的局限性
 * 通过第一个例子，我们知道Future接口提供了方法来检测异步计算是否已经结束（使用
 * isDone方法），等待异步操作结束，以及获取计算的结果。但是这些特性还不足以让你编写简洁
 * 的并发代码。比如，我们很难表述Future结果之间的依赖性；从文字描述上这很简单，“当长时
 * 间计算任务完成时，请将该计算的结果通知到另一个长时间运行的计算任务，这两个计算任务都
 * 完成后，将计算的结果与另一个查询操作结果合并”。但是，使用Future中提供的方法完成这样
 * 的操作又是另外一回事。这也是我们需要更具描述能力的特性的原因，比如下面这些
 *  将两个异步计算合并为一个——这两个异步计算之间相互独立，同时第二个又依赖于第
 * 一个的结果。
 *  等待Future集合中的所有任务都完成。
 *  仅等待Future集合中最快结束的任务完成（有可能因为它们试图通过不同的方式计算同
 * 一个值），并返回它的结果。
 *  通过编程方式完成一个Future任务的执行（即以手工设定异步操作结果的方式）。
 *  应对Future的完成事件（即当Future的完成事件发生时会收到通知，并能使用Future
 * 计算的结果进行下一步的操作，不只是简单地阻塞等待操作的结果）。
 */
public class Case1 {

    public static void main(String[] args){
        // 创建ExecutorService，通过它你可以向线程池提交任务
        ExecutorService executor = Executors.newCachedThreadPool();
        // 向ExecutorService提交一个Callable对象
        Future<Double> future = executor.submit(new Callable<Double>() {
            public Double call() throws InterruptedException {
                //以异步方式在新的线程中执 异步操作进行的同时， 行耗时的操作
                TimeUnit.SECONDS.sleep(3);
                return 2.0;
                //return doSomeLongComputation();
            }});
        //异步操作进行的同时， 行耗时的操作 你可以做其他的事情
        //doSomethingElse();

        try {
            // 获取异步操作的结果，如果最终被阻塞，无法得到结果，那么在最多等待1秒钟之后退出
            Double result = future.get(1, TimeUnit.SECONDS);
            System.out.println(result);
        } catch (ExecutionException ee) {
            // 计算抛出一个异常
        } catch (InterruptedException ie) {
            // 当前线程在等待过程中被中断
        } catch (TimeoutException te) {
            // 在Future对象完成之前超过已过期
        }
    }
}
