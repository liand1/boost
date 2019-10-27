package com.boost.core.java.jdk8.optional;

import com.boost.core.java.jdk8.optional.entity.Car;

import java.util.Optional;

public class Case1 {

    /**
     *  声明一个空的Optional
     */
    public static void case1() {
        Optional<Car> optCar = Optional.empty();

    }

    /**
     * 依据一个非空值创建Optional
     * 如果car是一个null，这段代码会立即抛出一个NullPointerException，而不是等到你
     * 试图访问car的属性值时才返回一个错误
     */
    public static void case2() {
        Optional<Car> optCar = Optional.of(new Car());
    }

    /**
     * 可接受null的Optional
     */
    public static void case3() {
        Car car = null;
        Optional<Car> optCar = Optional.ofNullable(car);
    }

    public static void main(String[] args) {
        case1();
    }
}
