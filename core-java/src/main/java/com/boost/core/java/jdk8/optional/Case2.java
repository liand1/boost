package com.boost.core.java.jdk8.optional;

import com.boost.core.java.jdk8.optional.entity.Car;
import com.boost.core.java.jdk8.optional.entity.Insurance;
import com.boost.core.java.jdk8.optional.entity.Person;

import java.util.Optional;

/**
 * 使用 map 从 Optional 对象中提取和转换值
 */
public class Case2 {

    private static Insurance insurance = new Insurance("ping an");

    public static void case1() {
        String name = null;

        if(insurance != null) {
            name = insurance.getName();
        }
        System.out.println("name is " + name);
    }

    public static void case2() {
        Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
        Optional<String> name = optInsurance.map(Insurance::getName);

        System.out.println("name is " + name.get());
    }

    /**
     * person.getCar().getInsurance().getName()
     *
     *  使用 flatMap 链接 Optional 对象
     *  flatMap方法接受一个函数作为参数，这个函数的返回值是另一个流。
     * 这个方法会应用到流中的每一个元素，最终形成一个新的流的流,但是flagMap会用流的内容替
     * 换每个新生成的流
     */
    public static void case3() {
        Person person = new Person();
        Optional<Person> optPerson = Optional.ofNullable(person);

        String name = optPerson.flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("CCI");// 如果Optional的结果值为空，设置默认值

        System.out.println("name is " + name);
    }

    public static void main(String[] args) {
        case3();
    }
}
