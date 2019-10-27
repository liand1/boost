package com.boost.core.java.jdk8.parallelstream.entity;

public class Accumulator {
    public long total = 0;

    public void add(long value) {
        total += value;
    }
}
