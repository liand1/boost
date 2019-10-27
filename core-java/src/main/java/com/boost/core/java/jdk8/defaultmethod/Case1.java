package com.boost.core.java.jdk8.defaultmethod;

/**
 * 猜猜打印输出的是什么？
 *
 * 如果一个类使用相同的函数签名从多个地方（比如另一个类或接口）继承了方法，通过三条
 * 规则可以进行判断。
 * (1) 类中的方法优先级最高。类或父类中声明的方法的优先级高于任何声明为默认方法的优
 * 先级
 * (2) 如果无法依据第一条进行判断，那么子接口的优先级更高：函数签名相同时，优先选择
 * 拥有最具体实现的默认方法的接口，即如果B继承了A，那么B就比A更加具体。
 * (3) 最后，如果还是无法判断，继承了多个接口的类必须通过显式覆盖和调用期望的方法，
 *  显式地选择使用哪一个默认方法的实现。
 *
 *  依据规则(1)，类中声明的方法具有更高的优先级。D并未覆盖hello方法，可是它实现了接
 * 口A。所以它就拥有了接口A的默认方法。规则(2)说如果类或者父类没有对应的方法，那么就应
 * 该选择提供了最具体实现的接口中的方法。因此，编译器会在接口A和接口B的hello方法之间做
 * 选择。由于B更加具体，所以程序会再次打印输出“Hello from B”。你可以继续尝试测验9.2，考
 * 察一下你对这些规则的理解。
 */
public class Case1 extends D implements B, A {

    public static void main(String... args) {
        new Case1().hello();
    }
}

interface A {
    default void hello() {
        System.out.println("Hello from A");
    }
}

interface B extends A {
    default void hello() {
        System.out.println("Hello from B");
    }
}

class D implements A{ }
