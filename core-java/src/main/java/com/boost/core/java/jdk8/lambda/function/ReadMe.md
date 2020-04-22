case6: 函数接受多个输入参数，返回一个结果
## Function函数式接口本身还可以用来组合

case2: 基本类型
>  Java中，有一些相伴的类型，比如int和Integer—前者是基本类型，后者是装箱类型。麻烦的是，由于装箱类型是对象，因此在内存中存在额外开销,  
为了减小这些性能开销，Stream 类的某些方法对基本类型和装箱类型做了区分。 mapToLong 和其他类似函数即为该方面的  
一个尝试。在 Java 8 中，仅对整型、长整型和双浮点型做了特殊处理，因为它们在数值计算中用得最多，特殊处理后的系统性能提升效果最明显。 
对基本类型做特殊处理的方法在命名上有明确的规范。如果方法返回类型为基本类型，则在基本类型前加 To， ToLongFunction。如果参数是基本
类型，则不加前缀只需类型名即可， LongFunction。如果高阶函数使用基本类型，则在操作后加后缀 To 再加基本类型，如 mapToLong。 

case8: compose/andThen
case9: BinaryOperator 
case10: 方法引用
> 我们可以将方法引用看作一个函数指针function pointer,方法引用共分为四类：  
> + 类名::静态方法名  integers.forEach(System.out::println);   students.sort(Student::compareByAge);  
> + 引用名(对象名)::实例方法名  
> + `类名::实例方法名`    
> + 构造方法引用: 类名::new