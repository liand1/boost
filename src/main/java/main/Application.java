package main;

public class Application {
    public static void main(String[] args) {
        trimStr("LongAdder类继承自 Striped64类，在 Striped64 内部维护着三个变量。 LongAdder 的真实值其实是 base 的值与 Cell数组里\n" +
                "      面所有 Cell元素中的 value值的累加， base是个基础值，默认为 0。 cellsBusy用来实现自旋锁，状态值只有 0和1当创建 \n" +
                "      Cell元素， 扩容 Cell 数组或者初始化 Cel 数组时，使用 CAS 操作该变量来保证同时只有一个线程可以进行其中之一的操作 。\n" +
                "     ");

    }

    private static void trimStr(String string) {

        System.out.println(string.replaceAll(" ", "").replaceAll("。", "."));
    }
}
