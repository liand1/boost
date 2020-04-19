package com.boost.core.java.jdk8.lambda.function;

import java.util.Comparator;
import java.util.function.BinaryOperator;

public class Case9 {

    public static void main(String[] args) {
//        System.out.println(compute(1, 2, (a, b) -> a + b));
        System.out.println(getMax(1, 2, (a, b) -> a - b));
    }

    public static int compute(int a, int b, BinaryOperator<Integer> binaryOperator) {
        return binaryOperator.apply(a, b);
    }

    public static int getMax(int a, int b, Comparator<Integer> comparator) {
        return BinaryOperator.maxBy(comparator).apply(a, b);
    }
}
