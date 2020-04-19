package com.boost.core.java.jdk8.stream;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Case11 {

    public static void main(String[] args) {

    }

    // reduce是汇聚，会终止流
    private static void case1() {
        System.out.println(Stream.of(1, 2, 3, 4).map(x -> x * 2).reduce(0, Integer::sum));
    }

    // Stream.of(1, 2, 3, 4).collect(Collectors.toList())的内部执行流程
    private static void case2() {
        List<Integer> integers = Stream.of(1, 2, 3, 4).collect(Collectors.toList());
        //等同于上， 操作流程如下，先new一个list，然后，第二个就是把每一个元素放放到一个list集合中去，第三个就是把添加好元素的这个集合放入一个最终的list然后进行返回
        List<Integer> integers_1 = Stream.of(1, 2, 3, 4).collect(() -> new ArrayList<Integer>(), (theList, item) -> theList.add(item), (theList1, theList2) -> theList1.addAll(theList2));

        List<Integer> integers_2 = Stream.of(1, 2, 3, 4).collect(LinkedList::new, LinkedList::add, LinkedList::addAll);

    }
}
