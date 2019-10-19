package com.boost.core.java.thread.chapter3_atomicobject;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * AtomicLong 是多个线程同时竞争同一个变量情景
 * JDK8新增了一个原子性递增或者递减类LongAdder,LongAdder则是内部维护多个Cell变量，每个Cell里面有一个初始值为0的long型变
 * 量，在同等并发量的情况下，争夺单个变量的线程会减少，这是变相的减少了争夺共享资源的并发量，另外多个线程在争夺同一个原
 * 子变量时候，如果失败并不是自旋CAS重试，而是尝试获取其他原子变量上进行CAS尝试，这个增加了当前线程重试CAS成功的可能性，
 * 最后当获取当前值时候是把所有变量的值累加后再加上base的值返回的
 * <p>
 * LongAdder维护了一个延迟初始化的原子性更新数组和一个基值变量base.数组的大小保持是2的N次方大小，数组表的下标使用每个
 * 线程的hashcode值的掩码表示，数组里面的变量实体是Cell类型，Cell类型是AtomicLong的一个改进，用来减少缓存的争用，对于
 * 大多数原子操作字节填充是浪费的，因为原子性操作都是无规律的分散在内存中进行的，多个原子性操作彼此之间是没有接触的，但
 * 是原子性数组元素彼此相邻存放将能经常共享缓存行，所以这在性能上是一个提升
 */
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Mode.Throughput)// 测试吞吐量  Mode.AverageTime，测试平均耗时
public class Case2 {

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
