package com.boost.core.java.jdk8.defaultmethod;

/**
 * 菱形继承问题, 因为类的继承关系图形状像菱形
 */
public class Case2 {

    public static void main(String[] args) {
        new D().hello();
    }
}

interface AA {
    default void hello() {
        System.out.println("Hello from A");
    }
}

interface BB extends AA {
    default void hello() {
        System.out.println("Hello from B");
    }
}

interface CC extends AA {
    default void hello() {
        System.out.println("Hello from C");
    }
}

class DD implements BB, CC {
    // 需要显式地指定使用哪个方法, 否则该程序无法通过编译
    public void hello() {
        System.out.println("Hello from D");
    }
}
