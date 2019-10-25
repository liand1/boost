package com.boost.core.java.jdk8.stream.collectors;

import com.boost.core.java.jdk8.stream.collectors.entity.Currency;
import com.boost.core.java.jdk8.stream.collectors.entity.Transaction;
import com.boost.core.java.jdk8.stream.collectors.util.TransactionUtil;
import com.boost.core.java.jdk8.stream.entity.Dish;
import com.boost.core.java.jdk8.stream.util.DishUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summingInt;

public class Case1 {

    private static List<Transaction> transactions = TransactionUtil.createTransactions();

    private static List<Dish> menu = DishUtil.buildMenu();

    /**
     * 按照货币分组
     */
    public static void case1() {
        Map<Currency, List<Transaction>> map = transactions.stream().collect(Collectors.groupingBy(Transaction::getCurrency));

    }

    /**
     * 查找流中的最大值和最小值
     */
    public static void case2() {
        Optional<Dish> collect = menu.stream()
                .collect(Collectors.minBy(Comparator.comparing(Dish::getCalories)));

        System.out.println(collect.get());
    }

    /**
     * 汇总
     * Collectors类专门为汇总提供了一个工厂方法：Collectors.summingInt。它可接受一
     * 个把对象映射为求和所需int的函数，并返回一个收集器；该收集器在传递给普通的collect方
     * 法后即执行我们需要的汇总操作
     * Collectors.summingLong和Collectors.summingDouble方法的作用完全一样
     *
     * 举个例子来说，你可以这样求出菜单列表的总热量
     */
    public static void case3() {
        int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
    }

    /**
     * joining工厂方法返回的收集器会把对流中每一个对象应用toString方法得到的所有字符
     * 串连接成一个字符串。这意味着你把菜单中所有菜肴的名称连接起来
     */
    public static void case4() {
        String shortMenu = menu.stream().map(Dish::getName).collect(joining(", "));
    }

    public static void main(String[] args) {
        case2();
    }
}
