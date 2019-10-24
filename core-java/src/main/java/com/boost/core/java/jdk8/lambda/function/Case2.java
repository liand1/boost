package com.boost.core.java.jdk8.lambda.function;

import java.util.function.Predicate;

/**
 * Java类型要么是引用类型（比如Byte、Integer、Object、List），要么是原
 * 始类型（比如int、double、byte、char）。但是泛型（比如Consumer<T>中的T）只能绑定到
 * 引用类型。这是由泛型内部的实现方式造成的。①因此，在Java里有一个将原始类型转换为对应
 * 的引用类型的机制。这个机制叫作装箱（boxing）。相反的操作，也就是将引用类型转换为对应
 * 的原始类型，叫作拆箱（unboxing）。Java还有一个自动装箱机制来帮助程序员执行这一任务：装
 * 箱和拆箱操作是自动完成的,比如，这就是为什么下面的代码是有效的（一个int被装箱成为
 * Integer）：
 * List<Integer> list = new ArrayList<>();
 * for (int i = 300; i < 400; i++){
 *  list.add(i);
 * }
 * 但这在性能方面是要付出代价的。装箱后的值本质上就是把原始类型包裹起来，并保存在堆
 * 里。因此，装箱后的值需要更多的内存，并需要额外的内存搜索来获取被包裹的原始值。
 *
 * Java 8为我们前面所说的函数式接口带来了一个专门的版本，以便在输入和输出都是原始类
 * 型时避免自动装箱的操作
 */
public class Case2 {
    public static void main(String[] args) {
        IntPredicate evenNum = i -> i%2 == 0; // true（无装箱）
        System.out.println(evenNum.test(1000));

        Predicate<Integer> oddNumbers = (Integer i) -> i % 2 == 1;// false（装箱）
        System.out.println(oddNumbers.test(999));
    }
}

@FunctionalInterface
interface IntPredicate{
    boolean test(int t);
}
