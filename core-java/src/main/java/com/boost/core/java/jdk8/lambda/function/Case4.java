package com.boost.core.java.jdk8.lambda.function;

/**
 * 但Lambda表达式
 * 也允许使用自由变量（不是参数，而是在外层作用域中定义的变量），就像匿名类一样。 它们被
 * 称作捕获Lambda
 * 实例变量都存储在堆中，而局部变量则保存在栈上。如果Lambda可以直接访问局
 * 部变量，而且Lambda是在一个线程中使用的，则使用Lambda的线程，可能会在分配该变量的线
 * 程将这个变量收回之后，去访问该变量。因此，Java在访问自由局部变量时，实际上是在访问它
 * 的副本，而不是访问原始变量。如果局部变量仅仅赋值一次那就没有什么区别了——因此就有了
 * 这个限制,第二，这一限制不鼓励你使用改变外部变量的典型命令式编程模式
 */
public class Case4 {

    /**
     * Lambda可以没有限
     * 制地捕获（也就是在其主体中引用）实例变量和静态变量。但局部变量必须显式声明为final，
     * 或事实上是final。换句话说，Lambda表达式只能捕获指派给它们的局部变量一次。（注：捕获
     * 实例变量可以被看作捕获最终局部变量this。）
     */
    public static void main(String[] args) {
        /**final **/int portNum = 1337;
        Runnable r = () -> System.out.println(portNum);
//        portNum = 1; // 再次被赋值会被报错
    }
}
