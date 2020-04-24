package com.boost.core.java.jdk8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Case13 {

    public static void main(String[] args) {
//        case1();
        case1_refactor();
    }

    /**
     * 输入"hi, lee","hi, wang", "hi, depu","hello, lee","hello, wang", "hello, depu","你好, lee", "你好, wang", "你好, depu"
     */
    public static void case1() {
        List<String> list1 = Arrays.asList("hi", "hello", "你好");
        List<String> list2 = Arrays.asList("lee","wang","depu");
        list1.forEach(greet -> {
            list2.forEach(name -> {
                System.out.println(greet + "," + name);
            });
        });
    }

    public static void case1_refactor() {
        List<String> list1 = Arrays.asList("hi", "hello", "你好");
        List<String> list2 = Arrays.asList("lee","wang","depu");
        List<String> collect = list1.stream().flatMap(greet -> list2.stream().map(name -> greet + ", " + name)).collect(Collectors.toList());

        collect.forEach(System.out::println);
    }
}
