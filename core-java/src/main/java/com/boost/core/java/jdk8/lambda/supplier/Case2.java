package com.boost.core.java.jdk8.lambda.supplier;

import java.util.function.Supplier;

/**
 * 求数组最大值
 */
public class Case2 {
    public static int getMax(Supplier<Integer> supplier) {
        return supplier.get();
    }

    public static void main(String[] args) {
        int[] arr = {100, 22, 21, 1, 156, 97};

        int maxValue = getMax(() -> {
            // 获取数组最大值并返回,定义一个变量，把数组中的第一个元素赋值给该变量，记录数组中
            // 元素的最大值.
            int max = arr[0];
            for (int i : arr) {
                if (i > max) {
                    max = i;
                }
            }
            return max;
        });
        System.out.println("max value is " + maxValue);
    }
}
