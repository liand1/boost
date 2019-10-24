package com.boost.core.java.jdk8.lambda.functioninterface;

import com.boost.core.java.jdk8.lambda.entity.Apple;
import com.boost.core.java.jdk8.lambda.predicate.AppleGreenColorPredicate;
import com.boost.core.java.jdk8.lambda.predicate.AppleHeavyWeightPredicate;
import com.boost.core.java.jdk8.lambda.predicate.ApplePredicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Case2 {

    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(new Apple(80,"green"),
                new Apple(155, "green"),
                new Apple(120, "red"));


        useApplePredicate(inventory);
        useAnonymousClass(inventory);
        useLambda(inventory);
    }

    private static void useLambda (List<Apple> inventory) {
        List<Apple> result =
                filterApples(inventory, (Apple apple) -> "red".equals(apple.getColor()));
    }

        private static void useApplePredicate(List<Apple> inventory) {
        List<Apple> heavyApples =
                filterApples(inventory, new AppleHeavyWeightPredicate());
        List<Apple> greenApples =
                filterApples(inventory, new AppleGreenColorPredicate());
    }

    /**
     * 匿名内部类在运行时会生成一个class文件，Case2$1.class，它会被加载到内存，如果过多会影响性能
     * Lambda就不会生成这些文件
     * @param inventory
     */
    private static void useAnonymousClass(List<Apple> inventory) {
        // 使用匿名类, 但匿名类还是不够好。第一，它往往很笨重, 第二，很多程序员觉得它用起来很让人费解
        List<Apple> redApples =
                filterApples(inventory, new ApplePredicate(){
                    @Override
                    public boolean test(Apple apple) {
                        return  "red".equals(apple.getColor());
                    }
                });
    }

    public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory){
            if (p.test(apple)){
                result.add(apple);
            }
        }
        return result;
    }

}

