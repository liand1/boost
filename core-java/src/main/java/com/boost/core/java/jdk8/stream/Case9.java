package com.boost.core.java.jdk8.stream;

import com.boost.core.java.jdk8.stream.entity.Dish;
import com.boost.core.java.jdk8.stream.util.DishUtil;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 我们在前面看到了可以使用reduce方法计算流中元素的总和。例如，你可以像下面这样计
 * 算菜单的热量：
 * int calories = menu.stream()
 * .map(Dish::getCalories)
 * .reduce(0, Integer::sum);
 * 这段代码的问题是，它有一个暗含的装箱成本。每个Integer都必须拆箱成一个原始类型，
 * 再进行求和
 * <p>
 * Java 8引入了三个原始类型特化流接口来解决这个问题：IntStream、DoubleStream和
 * LongStream, 从而避免了暗含的装箱成本
 */
public class Case9 {

    private static List<Dish> menu = DishUtil.buildMenu();

    /**
     * 映射到数值流
     * 这里，mapToInt会从每道菜中提取热量（用一个Integer表示），并返回一个IntStream
     * （而不是一个Stream<Integer>）。然后你就可以调用IntStream接口中定义的sum方法，对卡
     * 路里求和了！请注意，如果流是空的，sum默认返回0。IntStream还支持其他的方便方法，如
     * max、min、average等
     */
    public static void case1() {
        System.out.println(menu.stream().mapToInt(Dish::getCalories).sum());
    }

    /**
     * 转换回对象流
     */
    public static void case2() {
        IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
        Stream<Integer> stream = intStream.boxed();
    }

    /**
     * 默认值OptionalInt
     * 求和的那个例子很容易，因为它有一个默认值：0。但是，如果你要计算IntStream中的最
     * 大元素，就得换个法子了，因为0是错误的结果。如何区分没有元素的流[]和最大值真的是0的流呢[-2,-5,0,-4]
     */
    public static void case3() {
        OptionalInt maxCalories = menu.stream()
                .mapToInt(Dish::getCalories)
                .max();

        int max = maxCalories.orElse(1);// ，如果没有最大值的话，你就可以显式处理OptionalInt去定义一个默认值了
    }

    /**
     * 统计1~100之间有多少个偶数(包含100)
     */
    public static void case4_1() {
        IntStream evenNumbers = IntStream.rangeClosed(1, 100) //这里我们用了rangeClosed方法来生成1到100之间的所有数字
                .filter(n -> n % 2 == 0);// 只选出偶数

        evenNumbers.count();// 50
    }

    /**
     * 统计1~100之间有多少个偶数(不包含100)
     */
    public static void case4_2() {
        IntStream evenNumbers = IntStream.range(1, 100) //这里我们用了rangeClosed方法来生成1到100之间的所有数字
                .filter(n -> n % 2 == 0);// 只选出偶数

        evenNumbers.count();// 49
    }

    public static void main(String[] args) {

    }

}
