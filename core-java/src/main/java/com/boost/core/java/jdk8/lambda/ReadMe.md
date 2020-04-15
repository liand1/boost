布尔表达式 (List<String> list) -> list.isEmpty()    Predicate<List<String>>  
创建对象 () -> new Apple(10)                  Supplier<Apple>  
消费一个对象 (Apple a) ->
System.out.println(a.getWeight())       Consumer<Apple>  
从一个对象中
选择/提取
(String s) -> s.length() Function<String, Integer>或ToIntFunction<String>  
合并两个值 (int a, int b) -> a * b IntBinaryOperator    
比较两个对象 (Apple a1, Apple a2) ->  
a1.getWeight().compareTo(a2.getWeight()) Comparator<Apple>或
BiFunction<Apple, Apple, Integer>
或 ToIntBiFunction<Apple, Apple>   

### 类型推断
List<String> list = new ArrayList<>();// diamond
### 