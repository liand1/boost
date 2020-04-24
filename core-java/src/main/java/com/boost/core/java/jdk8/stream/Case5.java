package com.boost.core.java.jdk8.stream;

import com.boost.core.java.jdk8.stream.entity.Dish;
import com.boost.core.java.jdk8.stream.util.DishUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 流支持map方法，它会接受一个函数作为参数。这个函数会被应用到每个元素上，并将其映
 * 射成一个新的元素（使用映射一词，是因为它和转换类似，但其中的细微差别在于它是“创建一
 * 个新版本”而不是去“修改”）。
 */
public class Case5 {

    private static List<Dish> menu = DishUtil.buildMenu();

    // 下面的代码把方法引用Dish::getName传给了map方法，
    //来提取流中菜肴的名称, 因为getName方法返回一个String，所以map方法输出的流的类型就是Stream<String>。
    public static void case1() {
        List<String> dishNames = menu.stream()
                .map(Dish::getName)
                .collect(toList());
    }

    // 给定一个单词列表，你想要返回另
    //一个列表，显示每个单词中有几个字母。怎么做呢？你需要对列表中的每个元素应用一个函数。
    //这听起来正好该用map方法去做！应用的函数应该接受一个单词，并返回其长度。你可以像下面
    //这样，给map传递一个方法引用String::length来解决这个问题：
    public static void case2() {
        List<String> words = Arrays.asList("Java 8", "Lambdas", "In", "Action");
        List<Integer> wordLengths = words.stream()
                .map(String::length)
                .collect(toList());
    }

    // 现在让我们回到提取菜名的例子。如果你要找出每道菜的名称有多长，怎么做？你可以像下
    //面这样，再链接上一个map：
    public static void case3() {
        List<Integer> dishNameLengths = menu.stream()
                .map(Dish::getName)
                .map(String::length)
                .collect(toList());
    }

    // 使用flatMap 对于一张单
    //词表，如何返回一张列表，列出里面 各不相同的字符 呢？例如，给定单词列表
    //["Hello","World"]，你想要返回列表["H","e","l", "o","W","r","d"]。
    //你可能会认为这很容易，你可以把每个单词映射成一张字符表，然后调用distinct来过滤
    //重复的字符。
    public static void case4() {
        // 使用flatMap方法的效果是，各个数组并不是分别映射成一个流，而是映射成流的内容。所
        //有使用map(Arrays::stream)时生成的单个流都被合并起来，即扁平化为一个流
        // 一言以蔽之，flatmap方法让你把一个流中的每个值都换成另一个流，然后把所有的流连接起来成为一个流。
        List<String> words = Arrays.asList("Goodbye", "World");
        List<String> collect = words
                .stream()
                .map(w -> w.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(toList());

        System.out.println(collect);
    }

    /**
     *  给定一个数字列表，如何返回一个由每个数的平方构成的列表呢？例如，给定[1, 2, 3, 4,
     * 5]，应该返回[2, 4, 6, 8, 10]。
     */
    public static void case5() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println(numbers.stream().map(num -> num<<1).collect(toList()));

    }

    /**
     *  给定两个数字列表，如何返回所有的数对呢？例如，给定列表[1, 2, 3]和列表[3, 4]，应
     * 该返回[(1, 3), (1, 4), (2, 3), (2, 4), (3, 3), (3, 4)]
     */
    public static void case6() {
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);

        List<int[]> result = numbers1.stream().flatMap(i -> numbers2.stream().map(j -> new int[]{i, j}))
                .collect(toList());

    }

    /**
     如何扩展前一个例子，只返回总和能被3整除的数对呢？例如(2, 4)和(3, 3)是可以的。
     */
    public static void case7() {
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);

//        List<int[]> result = numbers1.stream().flatMap(i -> numbers2.stream().map(j -> new int[]{i, j}))
//                .filter(arr -> (arr[0] + arr[1])%3 == 0 )
//                .collect(toList());

        List<int[]> result = numbers1.stream().flatMap(i-> numbers2.stream().filter(j -> (i+j)%3 == 0).map(j -> new int[]{i, j}))
                .collect(toList());

        System.out.println(result);
    }

    /**
     * 过滤下列集合中的单词， 不重复显示
     */
    public static void case8() {
        List<String> list = Arrays.asList("hello world", "world hello", "hello world hello", "hello welcome");
        list.stream().flatMap(item -> Arrays.stream(item.split(" "))).distinct().forEach(System.out::println);
    }

    /**
     * 输入"hi, lee","hi, wang", "hi, depu","hello, lee","hello, wang", "hello, depu","你好, lee", "你好, wang", "你好, depu"
     */
    public static void case9() {
        List<String> list1 = Arrays.asList("hi", "hello", "你好");
        List<String> list2 = Arrays.asList("lee","wang","depu");
        list1.forEach(greet -> {
            list2.forEach(name -> {
                System.out.println(greet + "," + name);
            });
        });
    }

    public static void case9_refactor() {
        List<String> list1 = Arrays.asList("hi", "hello", "你好");
        List<String> list2 = Arrays.asList("lee","wang","depu");
        List<String> collect = list1.stream().flatMap(greet -> list2.stream().map(name -> greet + ", " + name)).collect(Collectors.toList());

        collect.forEach(System.out::println);
    }

    public static void main(String[] args) {
        case7();

    }

}
