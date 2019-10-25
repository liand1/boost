package com.boost.core.java.jdk8.stream.collectors.util;

import com.boost.core.java.jdk8.stream.collectors.entity.Currency;
import com.boost.core.java.jdk8.stream.collectors.entity.Transaction;

import java.util.Arrays;
import java.util.List;

public final class TransactionUtil {

    public static List<Transaction> createTransactions() {
        return Arrays.asList( new Transaction(Currency.EUR, 1500.0),
                new Transaction(Currency.USD, 2300.0),
                new Transaction(Currency.GBP, 9900.0),
                new Transaction(Currency.EUR, 1100.0),
                new Transaction(Currency.JPY, 7800.0),
                new Transaction(Currency.CHF, 6700.0),
                new Transaction(Currency.EUR, 5600.0),
                new Transaction(Currency.USD, 4500.0),
                new Transaction(Currency.CHF, 3400.0),
                new Transaction(Currency.GBP, 3200.0),
                new Transaction(Currency.USD, 4600.0),
                new Transaction(Currency.JPY, 5700.0),
                new Transaction(Currency.EUR, 6800.0) );
    }
}
