### 创建，更新和删除文档
+ 向集合添加新文档  
+ 从集合里删除文档  
+ 更新现在文档  
+ 为这些操作选择合适的安全级别和速度  
+ 查询  
    + 使用find或者findOne函数和查询文档对数据库进行查询
    + 使用$条件查询实现范围查询，数据集包含查询，不等式查询，以及其它一些查询
    + 差选将会返回一个数据库游标，游标只会在你需要时才将需要的文档批量返回
    + 还有很多指针对游标执行的元操作，包括忽略一定数量的结果，或者限定返回结果的数量，以及对结果排序。
    

#### 3.1 插入并保存
```
db.foo.insert({"bar":"baz"})
WriteResult({ "nInserted" : 1 })
```

##### 3.1.1 批量插入
如果要向集合中插入多个文档，使用批量插入会快一些。
```
db.foo.insert([{"name":"yeezy"},{"name":"scott"},{"name":"jordan"}])
```
如果在插入的时候有一个文档插入失败，那么在这个文档之前的所有文档会成功插入，但是这个文档之后的都会插入失败,如下
因为第三个文档的id和第二个相同，主键又冲突所以不会插入成功，导致第四个也不会插入成功，最终成功插入前两条数据
```
db.foo.insert([{"_id":1},{"_id":2},{"_id":2},{"_id":3}])
```
如果想失败文档后面的也插入到集合中，可以使用continueOnError选项。shell不支持这个选项，但是所有驱动程序都支持。

#### 3.2 删除文档
```
db.foo.remove({"_id":2})
```
remove删除数据时永久性的，不能撤销，不能恢复。

>如果要清空整个集合，那么使用db.foo.drop()会更快

#### 3.3 更新文档

##### 3.3.1文档替换
最简单的更新就是用一个新文档完全替换匹配的文档，这是用于进行大规模迁移的情况。
如下：我们希望将friends和enemies两个字段移到relationships子文档中。
```
> var people = {"name":"joe","friends":32,"enemies":2}
> db.users.insert(people)
WriteResult({ "nInserted" : 1 })
> db.users.find()
{ "_id" : ObjectId("5dcf459d5268ce524747362d"), "name" : "joe", "friends" : 32, "enemies" : 2 }
> var joe=.db.users.findOne({"name":"joe"})
2019-11-16T00:42:23.585+0000 E  QUERY    [js] uncaught exception: SyntaxError: expected expression, got '.' :
@(shell):1:8
> var joe=db.users.findOne({"name":"joe"})
> joe.relationships={"friends":joe.friends, "enemies":joe.enemies}
{ "friends" : 32, "enemies" : 2 }
> joe.username=joe.name;
joe
> delete joe.friends
true
> delete joe.enemies
true
> delete joe.name
true
> db.users.update({"name":"joe"}, joe);
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
> db.users.find()
{ "_id" : ObjectId("5dcf459d5268ce524747362d"), "relationships" : { "friends" : 32, "enemies" : 2 }, "username" : "joe" }
> 

```

##### 使用修改器
通常文档只会有一部分要更新，可以使用原子性的更新修改器(update modifier)，指定对文档中的某些字段进行更新。指定对文档中的
某些字段进行更新。更新修改器时种特殊的键，用来指定复杂的更新操作，比如修改，增加或者删除键，还可能时操作数组或者内嵌文档。

假设对一个网站的访问次数设置一个计数器
```
> db.users.update({"username":"joe"},{"$set":{"pv":52}})
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
> db.users.find()
{ "_id" : ObjectId("5dcf459d5268ce524747362d"), "relationships" : { "friends" : 32, "enemies" : 2 }, "username" : "joe", "url" : "www.baidu.com", "pv" : 52 }
> db.users.update({"username":"joe"},{"$inc":{"pv":1})
... db.users.find()
... ^C

> db.users.update({"username":"joe"},{"$inc":{"pv":1}})
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
> db.users.find()
{ "_id" : ObjectId("5dcf459d5268ce524747362d"), "relationships" : { "friends" : 32, "enemies" : 2 }, "username" : "joe", "url" : "www.baidu.com", "pv" : 53 }
> db.users.update({"username":"joe"},{"$inc":{"pv":1}})
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
> db.users.find()
{ "_id" : ObjectId("5dcf459d5268ce524747362d"), "relationships" : { "friends" : 32, "enemies" : 2 }, "username" : "joe", "url" : "www.baidu.com", "pv" : 54 }
> 

```
1.$set 用来指定一个字段的值，如果这个字段不存在，则创建它
2 $unset 将指定的键移除
```
db.users.update({"username":"joe"},{"$unset":{"pv":1}})
```
3 $inc 专门来增加和减少数字的。只能用于整型，长整型或双精度浮点型的值。
4 $push 会向已有数组末尾加入一个元素，要是没有就创建一个新的数组。可以使用$each子操作符，可以通过以一次$push操作添加多个
  值，同时可以使用$slice保证数组不会超出设定好的最大长度,$slice必须是负整数。`下面的命令，插入了两个元素，后面两个`
```
> db.users.update({"username":"joe"},{"$push":{"interest":{"$each":["basketball", "stamp", "fish"], "$slice":-2}}})
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
> db.users.find()
{ "_id" : ObjectId("5dcf459d5268ce524747362d"), "relationships" : { "friends" : 32, "enemies" : 2 }, "username" : "joe", "url" : "www.baidu.com", "interest" : [ "stamp", "fish" ] }
> 

```
5 $pop 删除元素 {"$pop";{"key":1}}从数组末尾开始删除一个元素，{"$pop";{"key":-2}}从数组开头删除两个元素
```
> db.lists.find()
{ "_id" : ObjectId("5dcff5ade3c061a1ee56d198"), "todo" : [ "dishes", "laundry", "dry cleaning" ], "name" : "depu" }
> db.lists.update({"name":"depu"}, {"$pop":{"todo":-1}})
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
> db.lists.find()
{ "_id" : ObjectId("5dcff5ade3c061a1ee56d198"), "todo" : [ "laundry", "dry cleaning" ], "name" : "depu" }
```
6 $pull 删除数组中指定元素
```
> db.lists.find()
{ "_id" : ObjectId("5dcff5ade3c061a1ee56d198"), "todo" : [ "dishes", "laundry", "dry cleaning" ], "name" : "depu" }
> db.lists.update({"name":"depu"}, {"$pull":{"todo":"laundry"}})
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
> db.lists.find()
{ "_id" : ObjectId("5dcff5ade3c061a1ee56d198"), "todo" : [ "dishes", "dry cleaning" ], "name" : "depu" }
```

7 基于位置的数组修改器，若是数组有多个值，而我们只想对其中的一部分进行操作，就需要一些技巧。有两种方法操作数组中的值：
  通过位置或者定位操作符("$"), 如下数据，如果想增加第一条评论的投票数量
```
> db.blog.insert({"content":"...", "comments":[{"comment":"good post", "author":"John", "votes":0}, {"comment":"i thought it was too short", "author":"Claire", "votes":3},{"comment":"free watches", "author":"Alita", "votes":-1}]})
WriteResult({ "nInserted" : 1 })
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
> db.blog.update({"content":"..."}, {"$inc":{"comments.0.votes":1}})
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
> db.blog.find()
{ "_id" : ObjectId("5dcffc07e3c061a1ee56d199"), "content" : "...", "comments" : [ { "comment" : "good post", "author" : "John", "votes" : 1 }, { "comment" : "i thought it was too short", "author" : "Claire", "votes" : 3 }, { "comment" : "free watches", "author" : "Alita", "votes" : -1 } ] }

```
但是很多情况下一般不知道要修改的数组的下标。为了克服这个困难，MongoDB提供了定位操作符"$",用来定位查询文档已经匹配的数组元素
，并进行更新。例如，要是用户John把名字改成了Jim，就可以用定位符替换他在评论中的名字：
```
> db.blog.update({"comments.author":"John"}, {"$set":{"comments.$.author":"Jim"}})
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
> db.blog.find()
{ "_id" : ObjectId("5dcffc07e3c061a1ee56d199"), "content" : "...", "comments" : [ { "comment" : "good post", "author" : "Jim", "votes" : 2 }, { "comment" : "i thought it was too short", "author" : "Claire", "votes" : 3 }, { "comment" : "free watches", "author" : "Alita", "votes" : -1 } ] }
```  

#### 返回被更新的文档 findAndModify
可以通过findAndModify命令得到被更新的文档。这对于操作队列以及执行其它需要进行原子性取值和赋值的操作来说，十分方便。


#### 3.4 写入安全机制
写入安全(Write Concern)是一种客户端设置，用于控制写入的安全级别。默认情况下，插入，删除和更新都会一直等待数据库响应(写入是否成功)
然后才会继续执行。通常，遇到错误时，客户端会抛出一个异常
`两种最基本的写入安全机制是应答写入和非应答写入。应答写入是默认的方式：数据库会给出响应，告诉你写入操作是否成功执行。
非应答式写入不返回任何响应`.
通常来说，应用程序应该使用应答式写入。但是，对于一些不是特别重要的数据（比如日志或者是批量加载数据），你可能不愿意为了
自己不关心的数据而等待数据库的响应。这种情况下，可以使用非应答式写入。 
  

### 3.5 查询
#### 3.5.1 find
MongoDB中使用find来进行查询。查询就是返回一个集合中文档的子集。find的第一个参数决定了要返回哪些文档，也就是用来指定查询条件
空的查询条件就会匹配集合中的全部内容。
```
db.users.find({}) == db.users.find()
```
#### 3.5.1.1 指定需要返回的键
如果只想返回集合中的某些字段，可以使用如下查询,默认情况下"_id"这个键总是被返回，即便是没有指定要返回这个键。
```
db.users.find({}, {"username":1, "email":1})
```
也可以通过以下查询剔除掉集合中的某些字段,可以把"_id"剔除出去
```
db.users.find({}, {"password":0, "_id":0})
```

#### 3.5.2 限制
传递给数据库的查询文档的值必须是常量。也就是不能引用文档中其它键的值。
```
db.stock.find({"in_stock":"this.num_sold"})//比较数据库中两个字段的值，这种写法是行不通的
```

### 3.6 查询条件  
+ $lt -----> '<'  
+ $lte -----> '<='   
+ $gt -----> '>'    
+ $gte -----> '>='   
查询18~30年龄段的人  
```
db.table.find({"age":{"gte":18, "$lte":30}})
```
+ $ne -----> '!='  
+ $in   
```
db.table.find({"age":{"$in":[12, 25]}})
```
+ $nin -----> 'not in'  
+ $and
```
db.users.find({"$and":[{"age":{"$lt":20}},{"age": 30}]}).pretty()
```
+ $or  
>  '$in'能对单个键做or查询，但是想要对多个键多or查询就应该使用'$or'
查询年龄是12的或者是男性的文档 
```
db.table.find({"$or":[{"age":12}, {"sex","male"}]})
```
+ $not  
+ $mod 会将查询的值除以第一个给定值，若余数等于第二个给定值则匹配成功  
```
db.users.find({"id_num":{"$mod":[5, 1]}})// 查询id_num除以5余1的文档，如1,6,11,16
```

+ $exists  
如果想查询某个字段为null的文档，不能使用如下方式，因为它会查询出字段为null的文档，同时也匹配出了不包含这个字段的文档
```
> db.users.find({"name":null})
{ "_id" : ObjectId("5dd09406e7620d7d8a8b7e35"), "name" : null }
{ "_id" : ObjectId("5dd09458e7620d7d8a8b7e36"), "username" : "wang" }
```
应该使用这种方式进行匹配(目前没有$eq操作符，所以只能使用$in)
```
> db.users.find({"name":{"$in":[null], "$exists":true}})
{ "_id" : ObjectId("5dd09406e7620d7d8a8b7e35"), "name" : null }
```
+ 正则表达式  
```
> db.users.find({"name":/joe/i})// 查询名字为joe但是不区分大小写
```

+ 查询数组($all)   
查询数据里面即包含A又包含B元素的  
```
> db.food.find()
{ "_id" : ObjectId("5dd1d7dba648b6f91b48f0f3"), "fruit" : [ "apple", "banana", "peach" ] }
{ "_id" : ObjectId("5dd1d82fa648b6f91b48f0f4"), "fruit" : [ "apple", "kumquat", "orange" ] }
{ "_id" : ObjectId("5dd1d841a648b6f91b48f0f5"), "fruit" : [ "cherry", "banana", "apple" ] }
> db.food.find({"fruit":{"$all":["apple", "banana"]}})// 既包含apple又包含banana的fruit
{ "_id" : ObjectId("5dd1d7dba648b6f91b48f0f3"), "fruit" : [ "apple", "banana", "peach" ] }
{ "_id" : ObjectId("5dd1d841a648b6f91b48f0f5"), "fruit" : [ "cherry", "banana", "apple" ] }
``` 

+ 查询数组($size) 
可以用来查询特定长度的数组
```
db.food.find({"fruit":{"$size":3}})
```
+ 查询数组($slice) 
```
db.food.findOne({"fruit":{"$slice":5}})//返回前5条
db.food.findOne({"fruit":{"$slice":-5}})//返回后5条
db.food.findOne({"fruit":{"$slice":[23, 10]}})//跳过前23个元素，返回第24~33个
```

+ 查询数组($elemMatch）
如果要查询x大于10 ,小于20的文档，并且存在x是个数字数组的情况，`这时候使用如下的查询是错误的。因为会导致查询结果不准确`
因为在 [ 5, 25 ]里面，25是大于10的，5是小于20的(查询条件中的每条语句可以匹配不同的数组元素)
```
 db.num.find({"x":{"$gt":10, "$lt":20}})
{ "_id" : ObjectId("5dd2a1a4c6ac2d3222c0bfcf"), "x" : 15 }
{ "_id" : ObjectId("5dd2a1a4c6ac2d3222c0bfd0"), "x" : [ 5, 25 ] }
```
这时候可以使用$elemMatch，要求Mongo同时使用查询条件中的两个语句与一个数组元素进行比较，但是，这里有一个问题，它不会去匹
配非数组元素。
```
db.num.find({"x":{"$elemMatch":{"$gt":10, "$lt":20}}})
```

+ 查询内嵌文档
```
{ "_id" : ObjectId("5dd2a445c6ac2d3222c0bfd1"), "name" : { "first" : "li", "last" : "cheng" }, "age" : 99 }
> db.users.find({"name" : { "first" : "li", "last" : "cheng" }})
{ "_id" : ObjectId("5dd2a445c6ac2d3222c0bfd1"), "name" : { "first" : "li", "last" : "cheng" }, "age" : 99 }
> db.users.find({"name" : { "first" : "li"}})
> empty result // 因为如果要查询一个完整的子文档，那么子文档必须精确匹配。所以我们应该用以下方式
 db.users.find({"name.first" :  "li"})

```
场景1： 查询出joe发表的5分以上的评论
```
db.blog.find({"comments":{"$elemMatch":{"author":"joe", "score" :{"$gte": 5}}}})
{ "_id" : ObjectId("5dd2a6eec6ac2d3222c0bfd2"), "content" : "...", "comments" : [ { "author" : "joe", "comment" : "nice post", "score" : 3 }, { "author" : "joe", "comment" : "bad post", "score" : 6 } ] }
```
切记不能使用db.blog.find({"comments.author":"joe", "comments.score" :{"$gte": 5}}}}),因为comments是个数组，很有可能
"comments.author":"joe"在第一个元素中匹配了，"comments.score" :{"$gte": 5}这个在第二个元素中匹配了，也就是元素没有同时
满足这两个条件，而是多个元素分别满足了其中一个条件。

### 3.6.2 $where查询
`不是非常必要时，一定要避免使用$where查询，因为它们在速度上要比常规查询慢很多。每个文档从BSON转换成javascript对象，然后
通过$where表达式来运行。而且它不能使用索引`。进行复杂查询的另一种方法是使用聚合工具。

#### 服务器端脚本
在服务器上执行javascript时必须注意安全性，如果使用不当，服务器端javascript很容易受到攻击。与关系型数据库中的注入攻击类似。
不过只要在输入时遵循一些规则，就可以安全地使用JS。也可以在运行mongod时指定--noscripting选项，完全关闭javascript的执行。
比如func =  "function(){print('hello, "+name+"')}",name可能是"'); db.dropDatabase();print('"这样的字符串。
为了避免这种情况，应该使用作用域来传递name的值。

### 3.6.3 游标
调用find时，shell并不立即查询数据库，而是等待真正开始要求获得结果时才发送差选，这样在执行之前可以给查询附加额外的选项。
几乎所以游标对象的方法都返回游标本身，这样就可以按任意顺序组成方法链。例如，下面几种表达是等价的:
```
>var cusor = db.foo.find().sort({“x” : 1}).limit(1).skip(10);
>var cursor = db.foo.find().limit(1).sort({“x” : 1}).skip(10);
>var cursor = db.foo.find().skip(10).limit(1).sort({“x”: 1});
```
此时，查询还没有执行，所有这些函数只是构造查询，现在，假设我们执行如下操作
```
cursor.hasNext()
```
这时，查询被发往服务器。shell立刻获取前100个结果或者前4MB数据（两者这中较小者），这样下次调用next或者hasNext时就不必兴师
动众跑到服务器上去了。客户端用光了第一组结果，shell会再一次联系数据库，并要求更多的结果。这个过程一直会持续到游标耗尽或
者结果全部返回。
`sort、limit和skip组合使用可以实现分页的效果，但是skip过多的结果会导致性能问题，所以建议尽量避免`。
+ limit db.users.find().limit(3)// 只返回3个结果。  
+ limit db.users.find().skip(3)// 略过前3个匹配的文档。  
+ limit db.users.find().sort({username:1, age:-1})// 按照username升序，按照age降序排列
下面例子，搜索mp3，想每页返回50个结果，而且按照价格从高到低排序，可以这样写：
```
db.stock.find({"desc":"mp3"}).limit(50).sort({"price":-1})
```
如果要查询下一页，通过skip可以实现,然后略过过多的结果会导致性能问题。
```
db.stock.find({"desc":"mp3"}).limit(50).skip(50).sort({"price":-1})
```
避免使用skip略过大量结果
用skip略过少量的文档还是不错的。但是要是数量非常多的话，skip就会变得很慢，因为要先找到需要被略过的数据，然后再抛弃这些
数据。大多数据库都会再索引中保存更多的元数据，用于处理skip，但是mongo目前还不支持，所以要尽量避免略过太多的数据。`通常
可以利用上次的结果来计算下一次查询条件`。
比如可以根据时间升序来排列，或者第一页最后一个元素的时间，然后找出比这个时间大的前100条，这就是第二页的数据，可以不用
skip操作了
```
>var page1 = db.foo.find().sort({"date":-1}).limit(100)
>var lastest = null;
// 显示第一页
>while(page1.hasNext()) {
    lastest = page1.next();
    display(latest);
}
// 获取下一页
var page2 = db.foo.find({"date":{"$gt"：latest.date}}}).sort({"date":-1}).limit(100)
```
#### 高级查询
$maxscan 指定本次查询中扫描文档的上限，如果不希望查询耗时太多，也不确定集合中到底有多少文档需要扫描，那么可以使用这个选
项。这样就会将查询结果限定为与被扫描的集合部分相匹配的文档。这种方式的一个坏处是，某些你希望得到的文档没有扫描到。
待补充....