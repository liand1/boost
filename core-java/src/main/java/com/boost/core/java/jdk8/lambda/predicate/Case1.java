package com.boost.core.java.jdk8.lambda.predicate;

import java.util.function.Predicate;

public class Case1 {

    public static void main(String[] args) {
        Predicate<String> predicate = isEquals("Depu.li");

        System.out.println(predicate.test("Depu.li"));
    }

    private static Predicate<String> isEquals(Object object) {
        return Predicate.isEqual(object);
    }
}
