package com.boost.core.java.jdk8.parallelstream;

import com.boost.core.java.jdk8.parallelstream.entity.Accumulator;

import java.util.stream.LongStream;

/**
 * 正确使用并行流
 */
public class Case2 {

    /**
     * 错用并行流而产生错误的首要原因，就是使用的算法改变了某些共享状态。下面是另一种实
     * 现对前n个自然数求和的方法，但这会改变一个共享累加器,的。每
     * 次访问total都会出现数据竞争。如果你尝试用同步来修复，那就完全失去并行的意义了
     *
     * 多次打印肯定会出现不同的结果,这个时候方法的性能无关紧要了
     */
    public static void case1() {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, 10).parallel().forEach(accumulator::add);

        System.out.println(accumulator.total);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            case1();
        }

    }
}
