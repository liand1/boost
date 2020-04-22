package com.boost.core.java.jdk8.stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Case11 {

    public static void main(String[] args) {
//        case4();
//        case6();
        case7();

    }

    // reduce是汇聚，会终止流
    private static void case1() {
        System.out.println(Stream.of(1, 2, 3, 4).map(x -> x * 2).reduce(0, Integer::sum));
    }

    // Stream.of(1, 2, 3, 4).collect(Collectors.toList())的内部执行流程
    private static void case2() {
        List<Integer> integers = Stream.of(1, 2, 3, 4).collect(Collectors.toList());
        //等同于上， 操作流程如下，先new一个list，然后，第二个就是把一个元素放到一个这个list集合中去，第三个就是把添加好元素的这个集合放入一个最终的list然后进行返回
        List<Integer> integers_1 = Stream.of(1, 2, 3, 4).collect(() -> new ArrayList<Integer>(), (theList, item) -> theList.add(item), (theList1, theList2) -> theList1.addAll(theList2));

        List<Integer> integers_2 = Stream.of(1, 2, 3, 4).collect(LinkedList::new, LinkedList::add, LinkedList::addAll);

    }

    // 转换成指定类型的集合
    private static void case3() {
        List<Integer> integers = Stream.of(1, 2, 3, 4).collect(Collectors.toCollection(ArrayList::new));
        Set<Integer> treeSet = Stream.of(1, 2, 3, 4).collect(Collectors.toCollection(TreeSet::new));
    }

    // 将集合中的元素转换成大写然后输出
    private static void case4() {
        Stream.of("a", "b", "c", "d").map(String::toUpperCase).forEach(System.out::println);
    }

    // flatmap, 将多个集合扁平化处理(合成一个stream)， 然后每个值取它的平方后输出
    private static void case5() {
        Stream<List<Integer>> list = Stream.of(Arrays.asList(1), Arrays.asList(2, 3), Arrays.asList(4, 5, 6));
        list.flatMap(itemList -> itemList.stream()).map(val -> val * val).forEach(System.out::println);
    }

    //iterate
    private static void case6() {
        Stream.iterate(1, item -> item * 2).limit(5).forEach(System.out::println);
    }

    //找出流中的大于2的元素，然后将每个元素乘以2，然后忽略掉流中的前两个元素，然后再取流中的前两个元素，最后求出流中元素的总和
    private static void case7() {
        System.out.println(Stream.iterate(1, item -> item * 2).limit(7).filter(item -> item > 2).skip(2).limit(2).collect(Collectors.reducing(0, Integer::sum)));
    }


}
