package com.boost.core.java.thread.chapter5_advance.jmhplugin;

import com.boost.core.java.thread.chapter3_atomicobject.Case2;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Mode.Throughput)// 测试吞吐量  Mode.AverageTime，测试平均耗时
public class Case1 {

    private static AtomicLong count = new AtomicLong();
    private static LongAdder longAdder = new LongAdder();
    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder().include(Case2.class.getName()).forks(1).build();
        new Runner(options).run();
    }

    @Benchmark
    @Threads(10)
    public void run0(){
        count.getAndIncrement();
    }

    @Benchmark
    @Threads(10)
    public void run1(){
        longAdder.increment();
    }
}
