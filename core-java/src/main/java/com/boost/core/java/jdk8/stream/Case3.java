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
 *
 * 从表面上来看，好像流在执行了多个中间操作和一个终止操作之后，对于每一个操作，流中的元素都会遍历执行，也就是有几个操作，流中
 * 的元素就会进行几次遍历。这种观点是大错特错的。
 *
 * 流的实际执行流程是这样的，在遇到中间操作的时候，其实只是构建了一个Pipeline对象，而该对象是一个双向链表的数据结构，只有在
 * 遇到终止操作的时候，那些中间操作和终止操作会被封装成链表的数据结构链接起来，而流中每一个元素只会按照顺序链接的去执行这些操作，
 * 也就是说，流中的元素最终只会在遇到终止操作后遍历一次，而每个元素会将所有操作按顺序执行一遍。
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
