package com.boost.core.java.jdk8.stream.collectors;

import com.boost.core.java.jdk8.stream.entity.Dish;
import com.boost.core.java.jdk8.stream.util.DishUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

/**
 * 分组
 */
public class Case2 {

    private static List<Dish> menu = DishUtil.buildMenu();

    public enum CaloricLevel {DIET, NORMAL, FAT}

    public static void case1() {
        Map<Dish.Type, List<Dish>> dishesByType =
                menu.stream().collect(groupingBy(Dish::getType));
    }

    /**
     * 到400卡路里的菜划分为“低热量”（diet），热量400到700
     * 卡路里的菜划为“普通”（normal），高于700卡路里的划为“高热量”（fat）
     */
    public static void case2() {
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(
                groupingBy(dish -> {
                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                }));
    }

    /**
     *  多级分组
     *  这个二级分组的结果就是像下面这样的两级Map：
     *  这里的外层Map的键就是第一级分类函数生成的值：“fish, meat, other”，而这个Map的值又是
     * 一个Map，键是二级分类函数生成的值：“normal, diet, fat”
     * {MEAT={DIET=[chicken], NORMAL=[beef], FAT=[pork]},
     *  FISH={DIET=[prawns], NORMAL=[salmon]},
     *  OTHER={DIET=[rice, seasonal fruit], NORMAL=[french fries, pizza]}}
     */
    public static void case3() {
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel =
                menu.stream().collect(
                        groupingBy(Dish::getType,
                                groupingBy(dish -> {
                                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                    else return CaloricLevel.FAT;
                                })
                        )
                );
    }

    /**
     * 按子组收集数据
     * 要数一数菜单中每类菜有多少个
     * {MEAT=3, FISH=2, OTHER=4}
     */
    public static void case4() {
        Map<Dish.Type, Long> typesCount = menu.stream().collect(
                groupingBy(Dish::getType, counting()));
    }

    /**
     *  把收集器的结果转换为另一种类型,因为分组操作的Map结果中的每个值上包装的Optional没什么用，所以你可能想要把它们
     * 去掉。要做到这一点，或者更一般地来说，把收集器返回的结果转换为另一种类型，你可以使用
     * Collectors.collectingAndThen工厂方法返回的收集器
     *
     * 这个工厂方法接受两个参数——要转换的收集器以及转换函数，并返回另一个收集器。这个
     * 收集器相当于旧收集器的一个包装，collect操作的最后一步就是将返回值用转换函数做一个映
     * 射。在这里，被包起来的收集器就是用maxBy建立的那个，而转换函数Optional::get则把返
     * 回的Optional中的值提取出来。前面已经说过，这个操作放在这里是安全的，因为reducing
     * 收集器永远都不会返回Optional.empty()。
     *
     * 查找每个子组中热量最高的Dish {FISH=salmon, OTHER=pizza, MEAT=pork}
     */
    public static void case5() {
        Map<Dish.Type, Dish> mostCaloricByType =
                menu.stream()
                        .collect(groupingBy(Dish::getType,// 分类函数
                                collectingAndThen(
                                    maxBy(comparingInt(Dish::getCalories)),
                                Optional::get)));
    }
}
