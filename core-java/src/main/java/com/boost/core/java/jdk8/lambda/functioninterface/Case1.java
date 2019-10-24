package com.boost.core.java.jdk8.lambda.functioninterface;

import com.boost.core.java.jdk8.lambda.entity.Apple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Case1 {

    public static void main(String ... args){

        List<Apple> inventory = Arrays.asList(new Apple(80,"green"),
                new Apple(155, "green"),
                new Apple(120, "red"));

        // [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        List<Apple> greenApples = filterApples(inventory, Case1::isGreenApple);
        System.out.println(greenApples);

        // [Apple{color='green', weight=155}]
        List<Apple> heavyApples = filterApples(inventory, Case1::isHeavyApple);
        System.out.println(heavyApples);

        // [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        List<Apple> greenApples2 = filterApples(inventory, (Apple a) -> "green".equals(a.getColor()));
        System.out.println(greenApples2);

        // [Apple{color='green', weight=155}]
        List<Apple> heavyApples2 = filterApples(inventory, (Apple a) -> a.getWeight() > 150);
        System.out.println(heavyApples2);

        // []
        List<Apple> weirdApples = filterApples(inventory, (Apple a) -> a.getWeight() < 80 ||
                "brown".equals(a.getColor()));
        System.out.println(weirdApples);
    }

    /**
     * 筛选绿苹果
     * 突出显示的行就是筛选绿苹果所需的条件.但是现在农民改主意了，他还想要筛选红苹果.
     * 你该怎么做呢？简单的解决办法就是复制这个方法，把名字改成filterRedApples，然后更改
     * if条件来匹配红苹果.然而，要是农民想要筛选多种颜色：浅绿色、暗红色、黄色等，这种方法
     * 就应付不了了.一个良好的原则是在编写类似的代码之后，尝试将其抽象化
     */
    public static List<Apple> filterGreenApples(List<Apple> inventory){
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory){
            if ("green".equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    /**
     * 只能区分苹果颜色
     */
    public static List<Apple> filterApplesByColor(List<Apple> inventory, String color){
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory){
            if (color.equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    /**
     * 只能筛选重量，但是复制了大部分的代码实现遍历库存 新的需求筛选颜色和重量
     */
    public static List<Apple> filterHeavyApples(List<Apple> inventory){
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory){
            if (apple.getWeight() > 150) {
                result.add(apple);
            }
        }
        return result;
    }

    /**
     * 新的需求筛选颜色和重量, 但真的很笨拙
     */
    public static List<Apple> filterApples(List<Apple> inventory, int weight, String color, boolean flag){
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory){
            if ((flag || apple.getColor().equals(color) ) ||
                    (!flag || apple.getWeight() > weight)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static boolean isGreenApple(Apple apple) {
        return "green".equals(apple.getColor());
    }

    public static boolean isHeavyApple(Apple apple) {
        return apple.getWeight() > 150;
    }

    public static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p){
        List<Apple> result = new ArrayList<>();
        for(Apple apple : inventory){
            if(p.test(apple)){
                result.add(apple);
            }
        }
        return result;
    }

}
