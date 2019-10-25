package com.boost.core.java.jdk8.stream;

import com.boost.core.java.jdk8.stream.entity.Dish;
import com.boost.core.java.jdk8.stream.util.DishUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 查找和匹配
 * Stream API通过allMatch、anyMatch、noneMatch、findFirst和findAny方法提供了这样的工具
 * anyMatch、allMatch和noneMatch这三个操作都用到了我们所谓的短路，这就是大家熟悉的Java中&&和||运算符短路在流中的版本
 *
 * 有些操作不需要处理整个流就能得到结果。例如，假设你需要对一个用and连起来的大布
 * 尔表达式求值。不管表达式有多长，你只需找到一个表达式为false，就可以推断整个表达式
 * 将返回false，所以用不着计算整个表达式。这就是短路。
 * 对于流而言，某些操作（例如allMatch、anyMatch、noneMatch、findFirst和findAny）
 * 不用处理整个流就能得到结果。只要找到一个元素，就可以有结果了。同样，limit也是一个
 * 短路操作：它只需要创建一个给定大小的流，而用不着处理流中所有的元素。在碰到无限大小
 * 的流的时候，这种操作就有用了：它们可以把无限流变成有限流。
 *
 * 何时使用findFirst和findAny
 * 你可能会想，为什么会同时有findFirst和findAny呢？答案是并行。找到第一个元素
 * 在并行上限制更多。如果你不关心返回的元素是哪个，请使用findAny，因为它在使用并行流时限制较少。
 */
public class Case6 {

    private static List<Dish> menu = DishUtil.buildMenu();

    /**
     * anyMatch方法可以回答“流中是否有一个元素能匹配给定的谓词”
     * anyMatch方法返回一个boolean，因此是一个终端操作
     */
    public static void case1() {
        if (menu.stream().anyMatch(Dish::isVegetarian)) {
            System.out.println("The menu is (somewhat) vegetarian friendly!");
        }
    }

    /**
     * 检查谓词是否匹配所有元素
     * 看菜品是否有利健康（即所有菜的热量都低于1000卡路里）
     */
    public static void case2() {
        if (menu.stream().allMatch(dish -> dish.getCalories()<1000)) {
            System.out.println("It's all low calorie food.");
        }
    }

    /**
     * 和allMatch相对的是noneMatch。它可以确保流中没有任何元素与给定的谓词匹配
     */
    public static void case3() {
        if (menu.stream().noneMatch(dish -> dish.getCalories()>=1000)) {
            System.out.println("No high calorie food!");
        }
    }

    /**
     * 查找元素
     * findAny方法将返回当前流中的任意元素。它可以与其他流操作结合使用。比如，你可能想
     * 找到一道素食菜肴。你可以结合使用filter和findAny方法来实现这个查询：
     * 流水线将在后台进行优化使其只需走一遍，并在利用短路找到结果时立即结束。
     */
    public static void case4() {
        Optional<Dish> dish = menu.stream()
                .filter(Dish::isVegetarian)
                .findAny();

        System.out.println(dish.get());
    }

    /**
     * 查找第一个元素
     * 有些流有一个出现顺序（encounter order）来指定流中项目出现的逻辑顺序（比如由List或
     * 排序好的数据列生成的流）。
     * 下面的代码能找出第一个平方能被3整除的数
     */
    public static void case5() {
        List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> first = someNumbers.stream().map(x -> x * x).filter(x -> x % 3 == 0).findFirst();// 9

        System.out.println(first.get());
    }

    public static void main(String[] args) {
        case5();
    }
}
