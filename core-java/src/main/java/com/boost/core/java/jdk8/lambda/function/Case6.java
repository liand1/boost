package com.boost.core.java.jdk8.lambda.function;

import com.boost.core.java.jdk8.lambda.entity.Apple;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * 构造函数引用
 */
public class Case6 {
    public static void main(String[] args) {
        // 构造函数引用指向默认的Apple()构造函数
        Supplier<Apple> supplier  = Apple::new;
        // 调用Supplier的get方法将产生一个新的Apple
        supplier.get();

        // 如果你的构造函数的签名是Apple(Integer weight)，那么它就适合Function接口的签
        //名，于是你可以这样写：
        // 指向Apple(Integer weight)的构造函数引用
        Function<Integer, Apple> c2 = Apple::new;
        // 调用该Function函数的apply方法，并给出要求的重量，将产生一个Apple
        Apple apple1 = c2.apply(150);
        System.out.println(apple1.getWeight());

        //如果你有一个具有两个参数的构造函数Apple(String color, Integer weight)，那么
        //它就适合BiFunction接口的签名，于是你可以这样写：
        // 指向Apple(String color,Integer weight)的构造函数引用
        BiFunction<Integer, String, Apple> c3 = Apple::new;
        //调用该BiFunction函数的apply方法，并给出要求的颜色和重量，将产生一个新的Apple对象
        Apple apple2 = c3.apply(110, "green");

        //怎么样才能对具有三个参数的构造函数，比如Color(int, int, int)，使用构造函数引用呢？
        // 是语言本身并没有提供这样的函数式接口，你可以自己创建一个
        TriFunction<Integer, Integer, Integer, Color> colorFactory = Color::new;
        Color color = colorFactory.apply(1, 2, 3);

        List<Integer> weights = Arrays.asList(7, 3, 4, 10);
        List<Apple> apples = map(weights, Apple::new);
    }

    public static List<Apple> map(List<Integer> list,
                                  Function<Integer, Apple> f){
        List<Apple> result = new ArrayList<>();
        for(Integer e: list){
            result.add(f.apply(e));
        }
        return result;
    }

}

/**
 * 接受三个参数
 * @param <T>
 * @param <U>
 * @param <V>
 * @param <R>
 */
interface TriFunction<T, U, V, R>{
    R apply(T t, U u, V v);
}
