package com.boost.core.java.jdk8.lambda.supplier;

import com.boost.core.java.jdk8.lambda.entity.Apple;

import java.util.function.Supplier;

public class Case3 {

    public static void main(String[] args) {

    }

    private static void case1() {
        Supplier<Apple> supplier = () -> new Apple();
        System.out.println(supplier.get().getColor());
    }

    private static void case2() {
        Supplier<Apple> supplier = Apple::new;
        System.out.println(supplier.get().getColor());
    }
}
