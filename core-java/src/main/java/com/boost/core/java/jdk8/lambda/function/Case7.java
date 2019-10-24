package com.boost.core.java.jdk8.lambda.function;

/**
 * 函数复合
 */
public class Case7 {

    public static void main(String[] args) {
        java.util.function.Function<Integer, Integer> f = x -> x + 1;
        java.util.function.Function<Integer, Integer> g = x -> x * 2;
        java.util.function.Function<Integer, Integer> h = f.andThen(g);
        java.util.function.Function<Integer, Integer> i = f.compose(g);

        System.out.println(h.apply(3));// 8  (3+1)*2
        System.out.println(i.apply(3));// 7  (3*2)+1

        //================================================================

        java.util.function.Function<String, String> addHeader = Letter::addHeader;
        java.util.function.Function<String, String> transformationPipeline
                = addHeader.andThen(Letter::checkSpelling)
                .andThen(Letter::addFooter);

        System.out.println(transformationPipeline.apply("start, labda "));
    }
}

class Letter {
    public static String addHeader(String text) {
        return "From Raoul, Mario and Alan: " + text;
    }

    public static String addFooter(String text) {
        return text + " Kind regards";
    }

    public static String checkSpelling(String text) {
        return text.replaceAll("labda", "lambda");
    }
}
