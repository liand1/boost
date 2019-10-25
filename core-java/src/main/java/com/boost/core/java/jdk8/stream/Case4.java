package com.boost.core.java.jdk8.stream;

import com.boost.core.java.jdk8.stream.entity.Dish;
import com.boost.core.java.jdk8.stream.util.DishUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Case4 {

    private static List<Dish> menu = DishUtil.buildMenu();

    public static void main(String[] args) {
        distinct();
    }

    // 筛选出所有素菜
    public static void filter() {
        List<Dish> vegetarianMenu = menu.stream()
                .filter(Dish::isVegetarian)
                .collect(Collectors.toList());
    }

    //筛选各异的元素, 去重
    public static void distinct() {
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .filter(i -> i % 2 == 0)
                .distinct()
                .forEach(System.out::println);
    }

    // 选出热量超过300卡路里的头三道菜
    public static void limit() {
        List<Dish> dishes = menu.stream()
                .filter(d -> d.getCalories() > 300)
                .limit(3)
                .collect(Collectors.toList());
    }

    // 跳过元素
    public static void skip() {
        List<Dish> dishes = menu.stream()
                .filter(d -> d.getCalories() > 300)
                .skip(2)// 跳过前两个
                .collect(Collectors.toList());
    }
}
