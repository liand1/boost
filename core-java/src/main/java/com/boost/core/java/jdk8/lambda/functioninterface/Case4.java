package com.boost.core.java.jdk8.lambda.functioninterface;

/**
 * 延迟执行
 */
public class Case4 {

    // 存在资源浪费, 第二个参数是一个拼接后的字符串。如果level为其它值，字符串就白拼接了
    public static void showLog(int level, String msg) {
        if(level == 1) {
            System.out.println(msg);
        }
    }

    /**
     * 使用lambda表达式作为参数传递，仅仅是参数传递到showLogWithLambda方法中
     * 只有条件满足，才会调用接口中的方法才会进行字符串拼接
     * @param level
     * @param messageBuilder
     */
    public static void showLogWithLambda(int level, MessageBuilder messageBuilder) {
        if(level == 1) {
            System.out.println(messageBuilder.buildMessage());
        }
    }

    public static void main(String[] args) {
        String msg1 = "java,";
        String msg2 = "scala,";
        String msg3 = "kotlin";

        showLog(1, msg1+msg2+msg3);

        showLogWithLambda(2, ()-> {
            System.out.println("满足条件(level==1)才打印这一行");
            return msg1+msg2+msg3;
        });
    }


}

@FunctionalInterface
interface MessageBuilder {
    String buildMessage();
}