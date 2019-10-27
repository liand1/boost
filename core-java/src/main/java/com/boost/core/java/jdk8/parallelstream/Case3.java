package com.boost.core.java.jdk8.parallelstream;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * 分支/合并框架的目的是以递归方式将可以并行的任务拆分成更小的任务，然后将每个子任
 * 务的结果合并起来生成整体结果。它是ExecutorService接口的一个实现，它把子任务分配给
 * 线程池（称为ForkJoinPool）中的工作线程
 */
public class Case3 extends RecursiveTask<Long> {

    private final long[] numbers;
    private final int start;
    private final int end;

    public static final long THRESHOLD = 10000;// 不再将任务分解为子任务的数组大小

    public Case3(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    public Case3(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    /**
     * 使用 RecursiveTask
     * 要把任务提交到这个池，必须创建RecursiveTask<R>的一个子类，其中R是并行化任务（以
     * 及所有子任务）产生的结果类型，或者如果任务不返回结果，则是RecursiveAction类型（当
     * 然它可能会更新其他非局部机构）。要定义RecursiveTask，只需实现它唯一的抽象方法
     * compute：
     * protected abstract R compute();
     * 这个方法同时定义了将任务拆分成子任务的逻辑，以及无法再拆分或不方便再拆分时，生成
     * 单个子任务结果的逻辑.正由于此，这个方法的实现类似于下面的伪代码：
     * if (任务足够小或不可分) {
     * 顺序计算该任务
     * } else {
     * 将任务分成两个子任务
     * 递归调用本方法，拆分每个子任务，等待所有子任务完成
     * 合并每个子任务的结果
     * }
     *
     * 请注意在实际应用时，使用多个ForkJoinPool是没有什么意义的。正是出于这个原因，一
     * 般来说把它实例化一次，然后把实例保存在静态字段中，使之成为单例，这样就可以在软件中任
     * 何部分方便地重用了
     */
    public static void case1() {
        // 这个性能差，但这只是因为必须先要把整个数字流都放进一个long[]，
        long[] numbers = LongStream.rangeClosed(1L, 10000000L).toArray();
        ForkJoinTask<Long> task = new Case3(numbers);
        Long result = new ForkJoinPool().invoke(task);

        System.out.println("result is " + result);
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        case1();
        long end = System.currentTimeMillis();

        System.out.println("execute complete, time is " + (end - start)/1000);
    }

    @Override
    protected Long compute() {
        int length = end - start;
        // 如果大小小于或等于阈值，顺序计算结果
        if (length <= THRESHOLD) {
            return computeSequentially();
        }
        // 创建一个子任务来为数组的前一半求和
        Case3 leftTask = new Case3(numbers, start, start + length / 2);
        // 利用另一个ForkJoinPool线程异步执行新创建的子任务
        leftTask.fork();
        // 创建一个任务为数组的后一半求和
        Case3 rightTask = new Case3(numbers, start + length / 2, end);
        // 同步执行第二个子任务，有可能允许进一步递归划分
        Long rightResult = rightTask.compute();
        // 读取第一个子任务的结果，如果尚未完成就等待
        Long leftResult = leftTask.join();
        // 该任务的结果是两个子任务结果的组合
        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }
}
