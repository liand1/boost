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

}
