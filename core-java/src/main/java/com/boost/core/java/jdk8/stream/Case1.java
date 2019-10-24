package com.boost.core.java.jdk8.stream;

import com.boost.core.java.jdk8.stream.entity.Dish;
import com.boost.core.java.jdk8.stream.util.DishUtil;

import java.util.List;
import java.util.stream.Collectors;

public class Case1 {

    private static List<Dish> menu = DishUtil.buildMenu();

    public static void main(String[] args) {
        case1();
    }

    /**
     * 查找热量最高的三道菜的菜名
     *
     * 在本例中，我们先是对menu调用stream方法，由菜单得到一个流。数据源是菜肴列表（菜
     * 单），它给流提供一个元素序列。接下来，对流应用一系列数据处理操作：filter、map、limit
     * 和collect。除了collect之外，所有这些操作都会返回另一个流，这样它们就可以接成一条流
     * 水线，于是就可以看作对源的一个查询。最后，collect操作开始处理流水线，并返回结果（它
     * 和别的操作不一样，因为它返回的不是流，在这里是一个List）。在调用collect之前，没有任
     * 何结果产生，实际上根本就没有从menu里选择元素。你可以这么理解：链中的方法调用都在排
     * 队等待，直到调用collect。
     */
    public static void case1() {
        List<String> threeHighCaloricDishNames = menu.stream()// 从menu获得流
                .filter(dish -> dish.getCalories() > 300)// 选出高热量的菜肴
                .map(Dish::getName)// 获取菜名
                .limit(3)
                .collect(Collectors.toList());

        System.out.println(threeHighCaloricDishNames);
    }
}
