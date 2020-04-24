package com.boost.core.java.jdk8.stream.collectors;

import com.boost.core.java.jdk8.stream.entity.Dish;
import com.boost.core.java.jdk8.stream.util.DishUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * 分区是分组的特殊情况：由一个谓词（返回一个布尔值的函数）作为分类函数，它称分区函
 * 数。分区函数返回一个布尔值，这意味着得到的分组Map的键类型是Boolean，于是它最多可以
 * 分为两组——true是一组，false是一组
 */
public class Case3 {

    private static List<Dish> menu = DishUtil.buildMenu();

    public enum CaloricLevel {DIET, NORMAL, FAT}

    /**
     * 把菜单按照素食和非素食分开
     */
    public static void case1() {
        Map<Boolean, List<Dish>> partitionedMenu = menu.stream().collect(partitioningBy(Dish::isVegetarian));
    }

    /**
     * 二级分组，按照素食和非素食分开，并且获取里面具体有哪些类型
     */
    public static void case2() {
        Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType =
                menu.stream().collect(
                        partitioningBy(Dish::isVegetarian,
                                groupingBy(Dish::getType)));
    }

    /**
     * 素食和非素食中热量最高的菜
     */
    public static void case3() {
        menu.stream().collect(
                partitioningBy(Dish::isVegetarian,
                        collectingAndThen(
                                maxBy(Comparator.comparing(Dish::getCalories)),
                                Optional::get)));
    }


}
