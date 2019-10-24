package com.boost.core.java.jdk8.lambda.predicate;

import com.boost.core.java.jdk8.lambda.entity.Apple;

public class AppleGreenColorPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return  "green".equals(apple.getColor());
    }
}
