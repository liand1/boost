package com.boost.core.java.jdk8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 计算reduce
 * 此类查询需要将流中所有元素反复结合起来，得到一个值，比如一个Integer。这样的查询可以被归类为归约操作
 * （将流归约成一个值）。用函数式编程语言的术语来说，这称为折叠（fold），因为你可以将这个操
 * 作看成把一张长长的纸（你的流）反复折叠成一个小方块，而这就是折叠操作的结果。
 */
public class Case7 {

    private static List<Integer> numbers = Arrays.asList(4,5,3,9);

    /**
     * 求和
     * 在我们研究如何使用reduce方法之前，先来看看如何使用for-each循环来对数字列表中的
     * 元素求和：
     */
    public static void case1() {
        int count = 0;
        for (Integer number : numbers) {
            count += number;
        }
        System.out.println("total is " + count);
    }

    /**
     * 使用reduce
     * reduce接受两个参数：
     *  一个初始值，这里是0；
     *  一个BinaryOperator<T>来将两个元素结合起来产生一个新值，这里我们用的是
     * lambda (a, b) -> a + b。
     *
     * 让我们深入研究一下reduce操作是如何对一个数字流求和的。首先，0作为Lambda（a）的
     * 第一个参数，从流中获得4作为第二个参数（b）。0 + 4得到4，它成了新的累积值。然后再用累
     * 积值和流中下一个元素5调用Lambda，产生新的累积值9。接下来，再用累积值和下一个元素3
     * 调用Lambda，得到12。最后，用12和流中最后一个元素9调用Lambda，得到最终结果21
     */
    public static void case2() {
        int sum = numbers.stream().reduce(0, (a, b) -> a + b);
        System.out.println("total is " + sum);
    }

    /**
     * 基于case2做了精简
     * 。在Java 8中，Integer类现在有了一个静态的sum
     * 方法来对两个数求和，这恰好是我们想要的，用不着反复用Lambda写同一段代码了
     */
    public static void case3() {
        int sum =  numbers.stream().reduce(0, Integer::sum);
    }

    /**
     * max/min val
     */
    public static void case4() {
        Optional<Integer> max = numbers.stream().reduce(Integer::max);
        Optional<Integer> min = numbers.stream().reduce(Integer::min);

        System.out.println(max.get());
        System.out.println(min.get());
    }


    public static void main(String[] args) {
        case4();
    }
}
