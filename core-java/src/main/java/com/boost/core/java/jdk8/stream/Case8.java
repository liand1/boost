package com.boost.core.java.jdk8.stream;

import joptsimple.internal.Strings;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;

public class Case8 {


    static {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");

        transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );
    }

    /**
     * 找出2011年发生的所有交易，并按交易额排序（从低到高）。
     */
    public static void case1() {
        List<Transaction> collect = transactions
                .stream()
                .filter(transaction -> transaction.getYear() == 2011)
                .sorted(comparing(Transaction::getValue))
                .collect(Collectors.toList());

        System.out.println(collect);
    }

    /**
     *  交易员都在哪些不同的城市工作过
     */
    public static void case2_1() {
        List<String> collect = transactions
                .stream()
                .map(transaction -> transaction.getTrader().getCity())
                .distinct()
                .collect(Collectors.toList());

       collect.forEach(System.out::println);
    }

    /**
     *  交易员都在哪些不同的城市工作过
     */
    public static void case2_2() {
        Set<String> collect = transactions
                .stream()
                .map(transaction -> transaction.getTrader().getCity())
                .collect(Collectors.toSet());

        collect.forEach(System.out::println);
    }

    /**
     * 查找所有来自于剑桥的交易员，并按姓名排序。
     */
    public static void case3() {
        List<Trader> traders = transactions.stream()
                .map(transaction -> transaction.getTrader())
                .filter(trader -> trader.getCity().equals("Cambridge"))
                .distinct()
                .sorted(comparing(Trader::getName))
                .collect(Collectors.toList());

        traders.forEach( trader -> System.out.println(trader.getName()));
    }

    /**
     * 返回所有交易员的姓名集合，按字母顺序排序
     */
    public static void case4() {
        Set<String> names = transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .sorted(String::compareTo)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        names.forEach(System.out::println);
    }

    /**
     * 返回所有交易员的姓名字符串，按字母顺序排序, 效率低joining基于String
     */
    public static void case5_1() {
        String names = transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .reduce(Strings.EMPTY, (a, b) -> a+b);

        System.out.println(names);
    }

    /**
     * 返回所有交易员的姓名字符串，按字母顺序排序, 效率高joining基于StringBuilder
     */
    public static void case5_2() {
        String names = transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .collect(joining());

        System.out.println(names);
    }

    /**
     * 有没有交易员是在米兰工作的
     */
    public static void case6() {
        System.out.println(transactions.stream()
                .anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan")));
    }

    /**
     * 打印生活在剑桥的交易员的所有交易额
     */
    public static void case7() {
        transactions.stream()
                .filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
                .map(transaction -> transaction.getValue())
                .forEach(System.out::println);
    }

    /**
     * 所有交易中，最高的交易额是多少
     */
    public static void case8() {
        System.out.println(transactions.stream()
                .map(transaction -> transaction.getValue())
                .reduce(Integer::max));
    }

    /**
     * 找到交易额最小的交易
     */
    public static void case9() {
        System.out.println(transactions.stream()
                .reduce((t1, t2) -> t1.getValue()< t2.getValue() ? t1 : t2).get());

        // 还可以做得更好。流支持min和max方法，它们可以接受一个Comparator作为参数，指定
        //计算最小或最大值时要比较哪个键值
        Optional<Transaction> smallestTransaction =
                transactions.stream()
                        .min(comparing(Transaction::getValue));
    }


    public static void main(String[] args) {
        case9();
    }

    private static List<Transaction> transactions;
}

class Trader{
    private final String name;
    private final String city;
    public Trader(String n, String c){
        this.name = n;
        this.city = c;
    }
    public String getName(){
        return this.name;
    }
    public String getCity(){
        return this.city;
    }
}

class Transaction{
    private final Trader trader;
    private final int year;
    private final int value;
    public Transaction(Trader trader, int year, int value){
        this.trader = trader;
        this.year = year;
        this.value = value;
    }
    public Trader getTrader(){
        return this.trader;
    }
    public int getYear(){
        return this.year;
    }
    public int getValue(){
        return this.value;
    }
    public String toString(){
        return "{" + this.trader + ", " +
                "year: "+this.year+", " +
                "value:" + this.value +"}";
    }
}
