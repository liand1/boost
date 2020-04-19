case6: 函数接受多个输入参数，返回一个结果
## Function函数式接口本身还可以用来组合

case8: compose/andThen
case9: BinaryOperator 
case10: 方法引用
> 我们可以将方法引用看作一个函数指针function pointer,方法引用共分为四类：  
> + 类名::静态方法名  integers.forEach(System.out::println);   students.sort(Student::compareByAge);  
> + 引用名(对象名)::实例方法名  
> + `类名::实例方法名`    
> + 构造方法引用: 类名::new