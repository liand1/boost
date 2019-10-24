package com.boost.core.java.jdk8.lambda.consumer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Consumer是一个消费型接口
 */
public class Case1 {

    public static void accept(String name, Consumer<String> consumer) {
        consumer.accept(name);
    }

    public static void main(String[] args) {
        // case1
        accept("case1", (name) -> {
            String s = new StringBuffer(name).reverse().toString();
            System.out.println(s);
        });

        // case2
        forEach(Arrays.asList(1,2,3,4,5), (i) -> System.out.println(i));
    }

    public static <T> void forEach(List<T> list, Consumer1<T> c) {
        for (T i : list) {
            c.accept(i);
        }
    }

}

@FunctionalInterface
interface Consumer1<T> {
    void accept(T t);
}
