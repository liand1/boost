package main;

public class Application {
    public static void main(String[] args) {
        trimStr("，行为参数化是一个很有用的模式，它能够轻松地适应不断变化的需求。这\n" +
                "种模式可以把一个行为（一段代码）封装起来，并通过传递和使用创建的行为（例如对Apple的\n" +
                "不同谓词）将方法的行为参数化。前面提到过，这种做法类似于策略设计模式");

    }

    private static void trimStr(String string) {

        System.out.println(string.replaceAll(" ", "").replaceAll("。", "."));
    }
}
