package com.boost.core.java.jdk8.stream.collectors;

import com.boost.core.java.jdk8.stream.collectors.entity.Currency;
import com.boost.core.java.jdk8.stream.collectors.entity.Transaction;
import com.boost.core.java.jdk8.stream.collectors.util.TransactionUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Case1 {

    private static List<Transaction> transactions = TransactionUtil.createTransactions();

    /**
     * 按照货币分组
     */
    public static void case1() {
        Map<Currency, List<Transaction>> map = transactions.stream().collect(Collectors.groupingBy(Transaction::getCurrency));

    }

    /**
     * 查找流中的最大值和最小值
     */
    public static void case2() {
    }
}
