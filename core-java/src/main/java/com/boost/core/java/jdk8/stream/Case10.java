package com.boost.core.java.jdk8.stream;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * 构建流
 */
public class Case10 {

    /**
     * 可以使用静态方法Stream.of，通过显式值创建一个流。它可以接受任意数量的参数。例
     * 如，以下代码直接使用Stream.of创建了一个字符串流。然后，你可以将字符串转换为大写，再
     * 一个个打印出来：
     */
    public static void case1() {
        Stream.of("Java 8 ", "Lambdas ", "In ", "Action")
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    /**
     * 由数组创建流
     */
    public static void case2() {
        int sum = Arrays.stream( new int[]{2, 3, 5, 7, 11, 13}).sum();
    }

    /**
     *  由文件生成流
     *  看看一个文件中有多少各不相同的词
     */
    public static void case3() {
        long uniqueWords = 0;
        try(Stream<String> lines =
                    Files.lines(Paths.get("data.txt"), Charset.defaultCharset())){
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))// 生成单词流
                    .distinct()
                    .count();
        }
        catch(IOException e){

        }
    }

    /**
     *  由函数生成流：创建无限流
     *  Stream API提供了两个静态方法来从函数生成流：Stream.iterate和Stream.generate。
     * 这两个操作可以创建所谓的无限流：不像从固定集合创建的流那样有固定大小的流。由iterate
     * 和generate产生的流会用给定的函数按需创建值，因此可以无穷无尽地计算下去！一般来说，
     * 应该使用limit(n)来对这种流加以限制，以避免打印无穷多个值
     *
     * iterate方法接受一个初始值（在这里是0），还有一个依次应用在每个产生的新值上的
     * Lambda（UnaryOperator<t>类型）。这里，我们使用Lambda n -> n + 2，返回的是前一个元
     * 素加上2。因此，iterate方法生成了一个所有正偶数的流：流的第一个元素是初始值0。然后加
     * 上2来生成新的值2，再加上2来得到新的值4，以此类推。这种iterate操作基本上是顺序的，
     * 因为结果取决于前一次应用。请注意，此操作将生成一个无限流——这个流没有结尾，因为值是
     * 按需计算的，可以永远计算下去。我们说这个流是无界的。正如我们前面所讨论的，这是流和集
     * 合之间的一个关键区别。我们使用limit方法来显式限制流的大小。这里只选择了前10个偶数。
     * 然后可以调用forEach终端操作来消费流，并分别打印每个元素
     */
    public static void case4() {
        Stream.iterate(0, n -> n + 2)
                .limit(10)
                .forEach(System.out::println);

    }

    /**
     * generate
     */
    public static void case5() {
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);
    }
}
