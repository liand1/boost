package com.boost.core.java.jdk8.stream.collectors;

import com.boost.core.java.jdk8.stream.entity.Dish;
import com.boost.core.java.jdk8.stream.util.DishUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Case4 {

    private static List<Dish> menu = DishUtil.buildMenu();

    public static void main(String[] args) {
//        case1();
        case5();
    }

    // order by name
    private static void case1() {
        Collections.sort(menu, Comparator.comparing(Dish::getName));

        menu.stream().forEach(System.out::println);
    }

    // order by name desc
    private static void case2() {
        Collections.sort(menu, Comparator.comparing(Dish::getName).reversed());

        menu.stream().forEach(System.out::println);
    }

    // order by name length
    private static void case3() {
        Collections.sort(menu, Comparator.comparingInt(dish -> dish.getName().length()));

        menu.stream().forEach(System.out::println);
    }

    // order by name length desc
    private static void case4() {
        //下面这行注释的是不可行的，这里涉及到编译器的类型推断
//        Collections.sort(menu, Comparator.comparingInt(dish -> dish.getName().length()).reversed());

        Collections.sort(menu, Comparator.comparingInt((Dish dish) -> dish.getName().length()).reversed());

        menu.stream().forEach(System.out::println);
    }

    // order by name length then by name
    private static void case5() {

        Collections.sort(menu, Comparator.comparingInt((Dish dish) -> dish.getName().length()).thenComparing(Dish::getName));

        menu.stream().forEach(System.out::println);
    }
}
