package com.boost.core.java.jdk8.lambda.function;

import com.boost.core.java.jdk8.lambda.entity.Apple;

import java.util.Arrays;
import java.util.List;

/**
 * 方法引用， 方法引用让你可以重复使用现有的方法定义，并像Lambda一样传递它们。在一些情况下，
 * 比起使用Lambda表达式，它们似乎更易读，感觉也更自然
 * 事实上，方法引用就是让你根据已有的方法实现来创建
 * Lambda表达式。但是，显式地指明方法的名称，你的代码的可读性会更好。它是如何工作的呢？
 * 当你需要使用方法引用时，目标引用放在分隔符::前，方法的名称放在后面。例如，
 * Apple::getWeight就是引用了Apple类中定义的方法getWeight。请记住，不需要括号，因为
 * 你没有实际调用这个方法
 */
public class Case5 {

    public static void main(String[] args) {
        Function<Apple, String> color = Apple::getColor;

        List<String> str = Arrays.asList("a","b","A","B");

//        str.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
        str.sort(String::compareToIgnoreCase);// 优化


    }
}
