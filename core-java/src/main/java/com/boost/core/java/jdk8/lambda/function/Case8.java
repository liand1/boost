package com.boost.core.java.jdk8.lambda.function;

import java.util.function.Function;

public class Case8 {

    public static void main(String[] args) {
        System.out.println(compute1(2, val -> val * 3, val -> val * val)); // 12
        System.out.println(compute2(2, val -> val * 3, val -> val * val)); // 36
    }

    public static int compute1(int a, java.util.function.Function<Integer, Integer> function1, Function<Integer, Integer> function2) {
        return function1.compose(function2).apply(a);
    }

    public static int compute2(int a, java.util.function.Function<Integer, Integer> function1, Function<Integer, Integer> function2) {
        return function1.andThen(function2).apply(a);
    }
}
