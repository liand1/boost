package com.boost.core.java.jdk8.lambda.predicate;

import com.boost.core.java.jdk8.lambda.entity.Apple;

public class AppleHeavyWeightPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return apple.getWeight()>150;
    }
}
