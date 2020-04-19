package com.boost.core.java.jdk8.lambda.predicate;

import com.boost.core.java.jdk8.lambda.entity.Apple;

@FunctionalInterface
public interface ApplePredicate<T> {
    boolean test (Apple apple);
}
