### 用流收集数据
> 你可以把Java 8的流
  看作花哨又懒惰的数据集迭代器。它们支持两种类型的操作：中间操作（如filter或map）和
  终端操作（如count、findFirst、forEach和reduce）。中间操作可以链接起来，将一个流
  转换为另一个流。这些操作不会消耗流，其目的是建立一个流水线。与此相反，终端操作会消
  耗流，以产生一个最终结果，例如返回流中的最大元素。它们通常可以通过优化流水线来缩短
  计算时间。

case1: minBy/summingInt/joining
case2: groupingBy collectingAndThen 分组
case3: 分区