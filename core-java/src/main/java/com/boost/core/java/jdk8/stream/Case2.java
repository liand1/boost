package com.boost.core.java.jdk8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 只能遍历一次
 * 请注意，和迭代器类似，流只能遍历一次。遍历完之后，我们就说这个流已经被消费掉了。
 * 你可以从原始数据源那里再获得一个新的流来重新遍历一遍，就像迭代器一样（这里假设它是集
 * 合之类的可重复的源，如果是I/O通道就没戏了）。例如，以下代码会抛出一个异常，说流已被消
 * 费掉了：所以要记得，流只能消费一次！
 */
public class Case2 {

    public static void main(String[] args) {
        List<String> title = Arrays.asList("Java8", "In", "Action");
        Stream<String> s = title.stream();

        s.forEach(System.out::println);
        // throw IllegalStateException: stream has already been operated upon or closed
        s.forEach(System.out::println);
    }
}
