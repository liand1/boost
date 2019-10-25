package com.boost.core.java.jdk8.stream;

import com.boost.core.java.jdk8.stream.entity.Dish;
import com.boost.core.java.jdk8.stream.util.DishUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 外部迭代和内部迭代
 *
 * —Streams库的内部迭代可以自动选择一种适合你硬件的数据表示和并行实现
 */
public class Case3 {

    public static void main(String[] args) {

    }

    /**
     * 外部迭代
     */
    public static void foreach() {
        List<Dish> menu = DishUtil.buildMenu();
        List<String> names = new ArrayList<>();
        for (Dish dish : menu) {// 显式顺序迭代菜单列表
            names.add(dish.getName());
        }
    }

    /**
     * 外部迭代
     */
    public static void iterator() {
        List<Dish> menu = DishUtil.buildMenu();
        List<String> names = new ArrayList<>();
        Iterator<Dish> iterator = menu.iterator();// 显式迭代
        while(iterator.hasNext()) {
            Dish d = iterator.next();
            names.add(d.getName());
        }
    }

    /**
     * 内部迭代
     */
    public static void stream() {
        List<Dish> menu = DishUtil.buildMenu();
        List<String> names = menu.stream()
                .map(Dish::getName)
                .collect(Collectors.toList());
    }

    /**
     * 中间操作与终端操作.总而言之，流的使用一般包括三件事：
     *  一个数据源（如集合）来执行一个查询；
     *  一个中间操作链，形成一条流的流水线；
     *  一个终端操作，执行流水线，并能生成结果。
     *
     * 流水线中最后一个操作count返回一个long，这是一个非Stream的值。因此它是一个终端操作。所有前面的操作，filter、
     * distinct、limit，都是连接起来的，并返回一个Stream，因此它们是中间操作。
     */
    public static void stream2() {
        List<Dish> menu = DishUtil.buildMenu();
        long count = menu.stream()
                .filter(d -> d.getCalories() > 300)
                .distinct()
                .limit(3)
                .count();
    }

}
