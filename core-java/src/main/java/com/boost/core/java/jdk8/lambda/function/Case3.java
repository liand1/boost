package com.boost.core.java.jdk8.lambda.function;

import com.boost.core.java.jdk8.lambda.entity.Apple;

import java.security.PrivilegedAction;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.ToIntBiFunction;

/**
 * 同样的 Lambda，不同的函数式接口
 * 有了目标类型的概念，同一个Lambda表达式就可以与不同的函数式接口联系起来，只要它
 * 们的抽象方法签名能够兼容。比如，前面提到的Callable和PrivilegedAction，这两个接口
 * 都代表着什么也不接受且返回一个泛型T的函数。 因此，下面两个赋值是有效的：
 * Callable<Integer> c = () -> 42;
 * PrivilegedAction<Integer> p = () -> 42;
 */
public class Case3 {

    public static void main(String[] args) {

    }

    public static void methodA(String[] args) {
        Callable<Integer> c = () -> 42;
        PrivilegedAction<Integer> p = () -> 42;
    }

    public static void methodB(String[] args) {
        Comparator<Apple> c1 =
                (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
        ToIntBiFunction<Apple, Apple> c2 =
                (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
        BiFunction<Apple, Apple, Integer> c3 =
                (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
    }
}
