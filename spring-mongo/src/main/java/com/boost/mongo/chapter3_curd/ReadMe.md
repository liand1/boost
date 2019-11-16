### 创建，更新和删除文档
+ 向集合添加新文档
+ 从集合里删除文档
+ 更新现在文档
+ 为这些操作选择合适的安全级别和速度

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


  
  


