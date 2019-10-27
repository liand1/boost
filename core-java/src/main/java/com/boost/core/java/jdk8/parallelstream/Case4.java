package com.boost.core.java.jdk8.parallelstream;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Spliterator
 */
public class Case4 {

    private static final String SENTENCE = " Nel mezzo del cammin di nostra vita mi ritrovai in una selva oscura" +
            " ché la dritta via era smarrita ";


    /**
     * 一个迭代式字数统计方法来数数一个String中的单词数
     */
    public static void case1() {
        int counter = 0;
        boolean lastSpace = true;
        for (char c : SENTENCE.toCharArray()) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            } else {
                if (lastSpace) counter++;
                lastSpace = false;
            }
        }
        System.out.println("Found " + counter + " words");
    }

    /**
     *  以函数式风格重写单词计数器, stream不能并行
     */
    public static void case2() {
        Stream<Character> stream = IntStream.range(0, SENTENCE.length())
                .mapToObj(SENTENCE::charAt);

        System.out.println("Found " + countWords(stream).getCounter() + " words");
    }

    public static void case3() {
        Spliterator<Character> spliterator = new WordCounterSpliterator(SENTENCE);
        Stream<Character> stream = StreamSupport.stream(spliterator, true);

        System.out.println("Found " + countWords(stream).getCounter() + " words");
    }

    private static WordCounter countWords(Stream<Character> stream) {
        return stream.reduce(new WordCounter(0, true),
                WordCounter::accumulate,
                WordCounter::combine);
    }

    public static void main(String[] args) {
//        case1();
//        case2();
        case3();
    }
}

class WordCounter {
    private final int counter;
    private final boolean lastSpace;
    public WordCounter(int counter, boolean lastSpace) {
        this.counter = counter;
        this.lastSpace = lastSpace;
    }
    // 和迭代算法一样 ，accumulate 方法一个个遍历Character
    public WordCounter accumulate(Character c) {
        if (Character.isWhitespace(c)) {
            return lastSpace ? this : new WordCounter(counter, true);
        } else {
            // 上一个字符是空格，而当前遍历的字符不是空格时，将单词计数器加一
            return lastSpace ? new WordCounter(counter + 1, false) : this;
        }
    }
    // 合并两个WordCounter，把其计数器加起来, 仅需要计数器的总和，无需关心lastSpace
    public WordCounter combine(WordCounter wordCounter) {
        return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
    }
    public int getCounter() {
        return counter;
    }
}

/**
 * 1.tryAdvance方法把String中当前位置的Character传给了Consumer，并让位置加一。
 * 作为参数传递的Consumer是一个Java内部类，在遍历流时将要处理的Character传给了
 * 一系列要对其执行的函数。这里只有一个归约函数，即WordCounter类的accumulate
 * 方法。如果新的指针位置小于String的总长，且还有要遍历的Character， 则
 * tryAdvance返回true。
 * 2.trySplit方法是Spliterator中最重要的一个方法，因为它定义了拆分要遍历的数据
 * 结构的逻辑。就像在代码清单7-1中实现的RecursiveTask的compute方法一样（分支
 * /合并框架的使用方式），首先要设定不再进一步拆分的下限。这里用了一个非常低的下
 * 限——10个Character，仅仅是为了保证程序会对那个比较短的String做几次拆分。
 * 在实际应用中，就像分支/合并的例子那样，你肯定要用更高的下限来避免生成太多的
 * 任务。如果剩余的Character数量低于下限，你就返回null表示无需进一步拆分。相
 * 反，如果你需要执行拆分，就把试探的拆分位置设在要解析的String块的中间。但我
 * 们没有直接使用这个拆分位置，因为要避免把词在中间断开，于是就往前找，直到找到
 * 一个空格。一旦找到了适当的拆分位置，就可以创建一个新的Spliterator来遍历从
 * 当前位置到拆分位置的子串；把当前位置this设为拆分位置，因为之前的部分将由新
 * Spliterator来处理，最后返回。
 * 3.还需要遍历的元素的estimatedSize就是这个Spliterator解析的String的总长度和
 * 当前遍历的位置的差。
 * 4.最后，characteristic方法告诉框架这个Spliterator是ORDERED（顺序就是String
 * 中各个Character的次序）、SIZED（estimatedSize方法的返回值是精确的）、
 * SUBSIZED（trySplit方法创建的其他Spliterator也有确切大小）、NONNULL（String
 * 中不能有为 null 的 Character ） 和 IMMUTABLE （在解析 String 时不能再添加
 * Character，因为String本身是一个不可变类）的
 */
class WordCounterSpliterator implements Spliterator<Character> {
    private final String string;
    private int currentChar = 0;
    public WordCounterSpliterator(String string) {
        this.string = string;
    }
    // 处理当前字符
    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(string.charAt(currentChar++));
        // 如果还有字符要处理，则返回true
        return currentChar < string.length();
    }
    @Override
    public Spliterator<Character> trySplit() {
        int currentSize = string.length() - currentChar;
        // 返回null表示要解析的 String已经足够小，可以顺序处理
        if (currentSize < 10) {
            return null;
        }
        // 将试探拆分位置设定为要解析的String的中间
        for (int splitPos = currentSize / 2 + currentChar; splitPos < string.length(); splitPos++) {
            // 让拆分位置前进直到下一个空格
            if (Character.isWhitespace(string.charAt(splitPos))) {
                // 创建一个新WordCounterSpliterator来解析String从开始到拆分位置的部分
                Spliterator<Character> spliterator =
                        new WordCounterSpliterator(string.substring(currentChar, splitPos));
                // 将这个WordCounterSpliterator 的起始位置设为拆分位置
                currentChar = splitPos;
                return spliterator;
            }
        }
        return null;
    }
    @Override
    public long estimateSize() {
        return string.length() - currentChar;
    }
    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }
}
