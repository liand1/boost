package com.boost.core.java.jdk8.parallelstream;

import java.util.stream.LongStream;

public class Case1 {

    /**
     * 接受数字n作为参数，并返回从1到给定参数的所有数字的和
     */
    public static void case1() {
        long start = System.currentTimeMillis();
        long result = LongStream.range(1L, Long.MAX_VALUE)
                .limit(10000000000L)
                .reduce(0, Long::sum);

        long end = System.currentTimeMillis();
        System.out.println(result + ",case1 time is " + (end - start) / 1000);

    }

    public static void case2() {
        long start = System.currentTimeMillis();
        long result = LongStream.iterate(1, i -> i + 1)
                .limit(10000000000L)
                .reduce(0, Long::sum);

        long end = System.currentTimeMillis();
        System.out.println(result + ",case2 time is " + (end - start) / 1000);

    }

    public static void case3() {
        long start = System.currentTimeMillis();
        long result = 0;
        for (long i = 1L; i <= 10000000000L; i++) {
            result += i;
        }

        long end = System.currentTimeMillis();
        System.out.println(result + ",case3 time is " + (end - start) / 1000);

    }

    /**
     * 将顺序流转换为并行流
     * 数值过大，报OutOfMemoryError
     * <p>
     * 这里实际上有两个问题：
     * iterate生成的是装箱的对象，必须拆箱成数字才能求和；
     * 我们很难把iterate分成多个独立块来并行执行，iterate很难分割成能够独立执行的小块，因为每次应用这个函数都要依赖前
     * 一次应用的结果
     */
    public static void case4() {
        long start = System.currentTimeMillis();
        long result = LongStream.iterate(1, i -> i + 1)
                .limit(10000000000L)
                .parallel()
                .reduce(0L, Long::sum);

        long end = System.currentTimeMillis();
        System.out.println(result + ",case4 time is " + (end - start) / 1000);
    }

    /**
     * 将顺序流转换为并行流
     * 效率最快
     * <p>
     * LongStream.rangeClosed的方法。这个方法与iterate相比有两个优点。
     * LongStream.rangeClosed直接产生原始类型的long数字，没有装箱拆箱的开销。
     * LongStream.rangeClosed会生成数字范围，很容易拆分为独立的小块。例如，范围1~20
     * 可分为1~5、6~10、11~15和16~20。
     */
    public static void case5() {
        long start = System.currentTimeMillis();
        long result = LongStream.range(1, Long.MAX_VALUE)
                .limit(10000000000L)
                .parallel()
                .reduce(0L, Long::sum);

        long end = System.currentTimeMillis();
        System.out.println(result + ",case4 time is " + (end - start) / 1000);
    }


    public static void main(String[] args) {
//        case1();//17s
//        case2();//20s
//        case3();//7s
//        case4();//报OutOfMemoryError
//        case5();//1s
    }
}
