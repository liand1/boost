package com.boost.core.java.jdk8.lambda.supplier;

import java.util.function.Supplier;

/**
 * Supplier为生产型接口, 不接受参数，同时返回一个结果
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
