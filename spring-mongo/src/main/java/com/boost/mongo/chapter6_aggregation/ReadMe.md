## 聚合
+ 聚合框架
+ MapReduce
+ 几个简单聚合命令：count,distinct,group

### 6.1 聚合框架 
使用集合框架可以对集合中的文档进行变换和组合。基本上，可以用多个构建创建一个管道，用于对一连串的文档进行处理。这些构件
包括筛选(filtering)，投射(projecting)，分组(grouping)，排序(sorting)，限制(limiting)和跳过(skipping)

比如有一个保存杂志文章的集合。你可能希望找出发表文章最多哪个作者、 假设每篇文章中被保存为MongoDB的一个文档，可以按照如下步骤创建管道。
1. 将每个文章文档中的作者投射出来  
2. 将作者按照名字排序，统计每个名字出现的次数  
3. 将作者按照名字出现的次数降序排列  
4. 将返回结果限制为前五个  

对于操作符号实例：
1. ｛‘$project’:{‘auther‘:1}｝ 这样可以把‘author’从每个文档中投射出来；执行结果后该文档会存在｛‘id’:id,‘author‘:author｝
2.  {‘$group‘:{‘Id:‘‘$author‘,‘count‘:{‘$sum‘:1}}}  这样就会将作者按照名字排序，某个作者名字每出现一次，就会对
每个作者的count加一。执行该步骤后，结果集合中的每个文档就是这样的结构：｛"_id": "authorName","count": articleCount｝
3. {"$sort": {"count":-1}}  这个操作会对结果集合中的文档根据“count”字段进行降序排列
4. ｛"$limit":5｝ 这个结果的显示为当前结果的前五个文档。

在mongo中实际运行时，要将这些操作分别传给aggregate()函数
```
db.article.aggregate(
{"peoject":{"author":1}}, 
{"group":{"_id":"$author", "count":{"$sum":1}}}. 
{"sort":{"count":-1}},
{"limit":5}})
```

### 6.2 管道操作符
每个操作符都会接受一连串的文档，对这些文档做一些类型转换，最后将转换后的文档作为结果传递给下一个操作符(对于最后一个管道
操作符，是将结果返回给客户端)。

不同的管道操作符可以按任意顺序组合在一起使用，而且可以被重复任意多次，例如，可以先做$match,再做$group，再做$match

#### 6.2.1 $match
通常，在实际使用中应该尽可能将它放在管道的前面位置，一是可以快速将不需要的文档过滤掉，以减少管道的工作量，二是在投射和
分组之前执行$match，查询可以使用索引。
```
 db.users.aggregate({"$match":{"age":20}}, {"$group":{"_id":"$age", "count":{"$sum":1}}},{"$sort":{"age":1}},{"$limit":5})
```

#### 6.2.2 $project
+ 1可以从文档中提取字段，可以重命名字段。
```
db.users.aggregate({"project":{"userId":"$_id", "_id":0}})// 给_id重命名
```
+ 2数学表达式,将多个字段的值相加
case1: 将salary, bouns相加
```
db.users.aggregate({
    "$project":{
        "totalPay" : {"$add" : ["$salary", "$bonus"]}
    }
})
```
case2: 从总金额中扣除一些值
```
db.users.aggregate({
    "$project":{
        "totalPay" : 
            "$subtract":[{"$add" : ["$salary", "$bonus"]}, "$tax"]
    }
})
```
与此同时还有其它的操作符
$multiply: [expr1[,expr2,expr3...exprN]]相乘
$divide: [expr1,expr2]相除
$mod: [expr1,expr2]取模

+ 3日期表达式
+ 4字符串表达式
$substr
$concat
$toLower
$toUper

+ 5逻辑表达式

#### 6.2.3 $group
如果有一个用户集合，希望知道每个城市有多少用户，可以根据state和city分组，每个state和city对应一个分组，并且可以将它们传递给
$group函数的"_id"字段
```
{"$group":{"_id":{"state":"$state", "city":"$city"}}}
```
case1: 得到每个国家的总收入
```
db.sales.aggregate({"$group":{"_id":"$country", "totalRevenue" : {"$sum":"$revenue"}}})
```
case2: 得到每个国家的平均收入, 以及每个国家的销量
```
db.sales.aggregate({"$group":{"_id":"$country", "totalRevenue" : {"$avg":"$revenue"}，"numSales":{"$num":1}}})
```
case3: 找出考试成绩学生的最高分和最低分
```
db.scores.aggregate({"$group":{"_id":"$grade", "lowerScore" : {"$min":"$score"}，"highScore":{"$max":"$score"}}})
```
case4: 如果是排序好的，可以通过$first,$last实现
```
db.scores.aggregate(
{"$sort":{"score":1}},
{"$group":{"_id":"$grade", "lowerScore" : {"$first":"$score"}，"highScore":{"$last":"$score"}}}
)
```
case5: $push  
case6: $addToSet[用法参考,case5同理](https://www.jianshu.com/p/a5c70cfbc9af)  

#### 6.2.4 $unwind
将数组中的每一个值拆分为单独的文档
case1: 如果希望在所有子文档中，找到Mark评论的数据:
```
db.blog.aggragate({"$project":{"comments":"$comments"}}
                  ,{"$unwind":"$comments"},
                  ,{"$match" : {"comments.author" : "Mark"}})
```
#### 6.2.5 $sort
可以根据任何字段或多个字段进行排序，与在普通查询中的语法相同。如果要对大量的文档进行排序，强烈建议在管道的第一阶段进行排序
，这时的排序操作可以使用索引。否则，排序过程就会比较慢。而且会占用大量内存。
$sort也是一个无法使用流式工作的操作符，必须要先接收到所有的文档才能进行排序。在分片环境下，先在各个分片上进行排序，然后
将各个分片的排序结果发送到mongos做进一步处理

case1: 对员工排序，按薪水从高到低，姓名从A到Z
```
db.users.employees.aggregate({
"$project":{
    "compensation": {
        "$add" :["$salary","$bonus"]
    },
    "name" :1
 }
},
{
"$sort":{"compensation":-1, "name":1}    
})
```

#### 6.2.6 $limit(ignore..)
#### 6.2.7 $skip(ignore..)

#### 6.2.8 使用管道
应该尽量在管道的开始阶段就将尽可能多的文档和字段过滤掉。如果不是直接从原先的集合中使用数据，那就无法在筛选和排序中使用
索引。如果可能，聚合管道会尝试对操作进行排序，以便能够有效使用索引。

mongoDB不允许单一的聚合操作占用过多的系统内存，如果mongo发现某个聚合操作占用了20%以上的内存，这个操作就会直接输出错误。

### 6.3 MapReduce
聚合工具中的明星，`有时候无法使用聚合框架的查询语言来表达，这时可以使用MapReduce`。MapReduce使用js作为查询语言，因此
它能够表达任意复杂的逻辑。`然而，它非常慢，不应该用在实时的数据分析中`。

MapReduce可以在多台服务器中并行执行。它会将一个大问题拆分为多个小问题，将各个小问题发送到不同的机器上，每台机器只负责完成
一部分工作。所有机器都完成时，再将这些零碎的解决方案合并为一个完整的解决方案。

MapReduce需要这么几个步骤。最开始是映射"map"，







