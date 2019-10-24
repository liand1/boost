package com.boost.core.java.jdk8.lambda.supplier;

import lombok.val;

import java.util.function.Supplier;

/**
 * Supplier为生产型接口
 */
public class Case1 {

    public static String getString(Supplier<String> supplier) {
        return supplier.get();
    }

    public static void main(String[] args) {
        String s = getString(() -> "case1");

        System.out.println(s);

    }

}
