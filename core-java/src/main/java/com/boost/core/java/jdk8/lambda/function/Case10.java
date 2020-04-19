package com.boost.core.java.jdk8.lambda.function;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

// 方法引用
public class Case10 {

    static List<Student> students = Arrays.asList(new Student("zs", 20), new Student("ls", 21), new Student("ww", 19), new Student("zl", 18));

    public static void main(String[] args) {
//        case2();
//        case4();
        case6();
    }

    // 方法引用：静态方法名
    public static void case1() {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        integers.forEach(item -> System.out.println(item));
    }

    // 方法引用：静态方法名
    public static void case2() {
        System.out.println("sort by age1");
        students.sort(Student::compareByAge);
        students.forEach(System.out::println);
        System.out.println("sort by age2");
        students.stream().sorted(Student::compareByAge).collect(Collectors.toList()).forEach(System.out::println);
        System.out.println("sort by name");
        students.stream().sorted(Student::compareByName).collect(Collectors.toList()).forEach(System.out::println);
    }

    // 方法引用：静态方法名
    public static void case3() {
        System.out.println("sort by age1");
        students.sort(Student::compareByAge);
        students.forEach(System.out::println);
        System.out.println("sort by age2");
        students.stream().sorted(Student::compareByAge).collect(Collectors.toList()).forEach(System.out::println);
        System.out.println("sort by name");
        students.stream().sorted(Student::compareByName).collect(Collectors.toList()).forEach(System.out::println);
    }

    // 方法引用：引用名::实例方法名
    public static void case4() {
        StudentComparator comparator = new StudentComparator();
        System.out.println("sort by age");
        students.stream().sorted(comparator::compareByAge).collect(Collectors.toList()).forEach(System.out::println);
        System.out.println("sort by name");
        students.stream().sorted(comparator::compareByName).collect(Collectors.toList()).forEach(System.out::println);
    }

    // 方法引用：类名::实例方法名
    public static void case5() {
        System.out.println("sort by age");
        students.stream().sorted(Student::compareStuByAge).collect(Collectors.toList()).forEach(System.out::println);
        System.out.println("sort by name");
        students.stream().sorted(Student::compareStuByName).collect(Collectors.toList()).forEach(System.out::println);
    }

    // 方法引用：类名::实例方法名
    // 这个方法只接受了一个参数，但是Comparator是接受了两个参数，主要是取决于是谁调用的！比如 Collections.sort(cities, String::compareToIgnoreCase);
    // 这个是sort接受到了集合中的第一个对象， 第二个就作为参数被传递进去
    public static void case6() {
        List<String> cities = Arrays.asList("bj", "sh", "cs", "cq", "cd", "dl", "hlj");
        Collections.sort(cities, String::compareToIgnoreCase);

        cities.forEach(System.out::println);
    }

    // 构造方法引用: 类名::new
    public static void case7() {
        Student student = getStudent("depu", 32, Student::new);
    }

    public static Student getStudent(String name, Integer age, BiFunction<String, Integer, Student> function) {
        return function.apply(name, age);
    }
}

class StudentComparator {
    public int compareByName(Student a, Student b) {
        return a.getName().compareTo(b.getName());
    }

    public int compareByAge(Student a, Student b) {
        return a.getAge() - b.getAge();
    }
}


class Student {
    String name;
    int age;

//    public Student() {
//    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static int compareByAge(Student a, Student b) {
        return a.getAge() - b.getAge();
    }

    public static int compareByName(Student a, Student b) {
        return a.getName().compareTo(b.getName());
    }

    // 这个方法只接受了一个参数，但是Comparator是接受了两个参数，主要是取决于是谁调用的！比如students.stream().sorted(Student::compareStuByAge)
    // 这个是sort接受到了students集合中的第一个对象， 第二个就作为参数被传递进去
    public  int compareStuByAge(Student a) {
        return this.getAge() - a.getAge();
    }

    public  int compareStuByName(Student a) {
        return this.getName().compareTo(a.getName());
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
