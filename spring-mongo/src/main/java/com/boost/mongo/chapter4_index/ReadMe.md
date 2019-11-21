### 索引
索引可以用来优化查询，而且在某些特定类型的查询中，索引是必不可少的。
+ 什么是索引，为什么需要索引  
+ 如何选择需要建立索引的字段  
+ 如何强制使用索引，如果评估索引的效率  
+ 创建索引和删除索引  
为集合选择合适的索引是提高性能的关键  

不使用索引的查询称为全表扫描，通常来说，我们应该尽量避免全表扫描，因为对于大集合来说，全表扫描的效率非常低。
//插入1kw条数据
```
for( i=0; i <10000000; i++){ 
    db.user.insert( 
        { "i":i, "username":"user"+i, "age":Math.floor(Math.random()*120),"create":new Date() } 
    ); 
}
```
然后执行查询，使用explain()函数查看MongoDB在执行查询过程中所做的事情。
```
>db.user.find({"username":"user100"}).explain("executionStats");
>
```
目前用的mongo的版本是4.2.1，explain的打印参数如下
```
{
	"queryPlanner" : {
		"plannerVersion" : 1,
		"namespace" : "test.users",
		"indexFilterSet" : false,
		"parsedQuery" : {
			"username" : {
				"$eq" : "user100"
			}
		},
		"queryHash" : "379E82C5",
		"planCacheKey" : "379E82C5",
		"winningPlan" : {
			"stage" : "COLLSCAN",// 表示全表扫描
			"filter" : {
				"username" : {
					"$eq" : "user100"
				}
			},
			"direction" : "forward"
		},
		"rejectedPlans" : [ ]
	},
	"serverInfo" : {
		"host" : "d72953d37125",
		"port" : 27017,
		"version" : "4.2.1",
		"gitVersion" : "edf6d45851c0b9ee15548f0f847df141764a317e"
	},
	"ok" : 1
}
```
为了优化查询，我们可以使用limit(1)，这样查到一个文档就停止了
db.users.find({"username":"user100"}).limit(1).explain()
但是我们在看explain信息的时候发现其实还是全表扫描，现在有100w条数据，如果要查询最后几条，几乎也是会遍历整个集合，对此我们
便可以使用索引,然而使用索引是有代价的：对于添加的每一个索引，每次cud操作都将耗费更多的时间。这是因为，当数据发生变化时，mongo
不仅要更新文档，还要更新集合上的所有索引。因此mongo限制每个集合上最多只能有64个索引。通常，在一个特定的集合上，不应该
拥有两个以上的索引。
```
db.users.ensureIndex({"username": 1})
db.users.getIndexes()//获取该集合的索引信息
```
现在我们再执行explain查询信息
```
{
	"queryPlanner" : {
		"plannerVersion" : 1,
		"namespace" : "test.users",
		"indexFilterSet" : false,
		"parsedQuery" : {
			"username" : {
				"$eq" : "user100"
			}
		},
		"queryHash" : "379E82C5",
		"planCacheKey" : "965E0A67",
		"winningPlan" : {
			"stage" : "LIMIT",
			"limitAmount" : 1,
			"inputStage" : {
				"stage" : "FETCH",
				"inputStage" : {
					"stage" : "IXSCAN",
					"keyPattern" : {
						"username" : 1
					},
					"indexName" : "username_1",
					"isMultiKey" : false,
					"multiKeyPaths" : {
						"username" : [ ]
					},
					"isUnique" : false,
					"isSparse" : false,
					"isPartial" : false,
					"indexVersion" : 2,
					"direction" : "forward",
					"indexBounds" : {
						"username" : [
							"[\"user100\", \"user100\"]"
						]
					}
				}
			}
		},
		"rejectedPlans" : [ ]
	},
	"serverInfo" : {
		"host" : "d72953d37125",
		"port" : 27017,
		"version" : "4.2.1",
		"gitVersion" : "edf6d45851c0b9ee15548f0f847df141764a317e"
	},
	"ok" : 1
}

```

#### 4.1.1 复合索引
索引的值是按一定顺序排列的。因此，使用索引键对文档进行排序非常快。然而，只有在首先使用索引键进行排序时，索引才会用，例如在
下面的排序里，username上的索引没什么作用
```
db.users.find().sort({ "age":1， "username":1})
```
这里先根据age拍个再根据username排序，所以username在这里发挥的作用不大。为了优化这个排序，可能需要在age和username上建立索引
```
db.users.ensureIndex({"username":1, "age":1})
```

建完后的索引大致是这个样子：
```
[0, "user0"] -> 0X0C966589
[0, "user2"] -> 0X0C965148
[0, "user66"] -> 0XFC964657
...
[5, "user991"] -> 0X2C965148
[5, "user992"] -> 0XcC965148
[5, "user993"] -> 0X3C964517
...
[22, "user9996"] -> 0X0C675148
[22, "user9997"] -> 0XfC963428
[22, "user9998"] -> 0X9C132148

```
每一个索引条目都包含一个age字段和username字段，并且指向文档在磁盘上的存储位置。注意，这里的age字段时严格升序
排列的，age相同的条目按照username升序排列。

mongoDB对这个索引的使用方式取决于查询类型。下面是三种主要的方式。
+ 点查询（point query），用于查找单个值(尽管包含这个值的文档可能有多个)。，由于索引中的第二个字段，查询结果已经是有序的
了。mongo可以从{"age":21}匹配的最后一个索引开始，逆序依次遍历索引，这种类型的查询是非常高效的。mongo能够直接定位到正确
的年龄，而且不需要对结果排序(只需要逆序)。
```
dd.users.find({"age":21}).sort({"username":-1})
```
---
+ 多值查询(multi-value query) ,查询到多个值相匹配的文档。mongo会使用索引中的第一个键"age"得到匹配的文档，通常来说，如果
mongo使用索引进行查询，那么查询结果文档通常是按照索引顺序排列的。
```
db.users.find({"age":{"$gte":21, "lte":30}})
```
```
[21, "user0"] -> 0X0C966589
[21, "user888"] -> 0X0C965148
[21, "user12586"] -> 0XFC964657
...
[30, "user2"] -> 0X0C675148
[30, "user55"] -> 0XfC963428
[30, "user888"] -> 0X9C132148

```
---
+ 多值查询(multi-value query)，也是一个多值索引，只是这次需要对查询结果进行排序。然而，使用这个索引得到的结果集中的username
是无序的，所以mongo需要现在内存中对结果进行排序，然后才能返回。因此这个查询通常不如上一个高效。
```
db.users.find({"age":{"$gte":21, "lte":30}}).sort({"username":1})
```

如果结果集的大小超过32MB，Mongo就会出错，拒绝对如此多的数据进行排序：

#### 4.1.2 使用复合索引
+ 1. `选择键的方向`  
假设我们要根据年龄从小到大，用户名从Z到A对上面的集合排序。对于这个问题，之前的索引变得不再高效：每一个年龄分组都是按照username
升序排列的，是A到Z，而不是Z到A。对于按age升序排列按username降序排列这样的需求来说，用上面的索引得到的数据的顺序没什么用。
```
db.users.find().sort({"age":1, "username":-1}).explain()
```
`为了在不同方向上优化这个复合排序，需要使用与方向相匹配的索引`。
```
db.users.ensureIndex({"age":1, "username":-1})
```
现在再去执行explain查询发现已经不是collscan了。
---
+ 2. `使用覆盖索引(covered index)`  
上面的例子，查询只是用来查找正确的文档，然后按照指示获取实际的文档。然后如果你的查询只需要查找索引中包含的字段，那就根本
没必要获取实际的文档。当一个索引包含用户请求的所有字段，可以认为这个索引覆盖了本次查询。在实际中，应该优先使用覆盖索引，
而不是去获取实际的文档。这样可以保证工作集比较小，尤其与右平衡索引一起使用时。为了确保查询只使用索引就可以完成，应该使用
投射来指定不要返回"_id"字段（除非它是索引的一部分）。可能还需要对不需要查询的字段做索引，因此需要在编写时
就在所需的查询速度和这种方式带来的开销之间做好权衡。如果在覆盖索引上执行explain()，"indexOnly"字段的值要为true。
`如果在一个含有数组的字段上做索引，这个索引永远也无法覆盖查询（因为数组是被保存在索引中的）。即便将数组字段从需要返回的
字段中剔除，这样的索引仍然无法覆盖查询`。
```
db.users.find({"username":"user99"}, {"username":1, "_id":0}).explain()// 只查询username字段。
```
---
+ 3. 隐式索引
复合索引具有双重功能，而且对不同的查询可以表现为不同的索引。如果有一个{"age" : 1， "username" : 1}索引，"age"字段会被自
动排序，就好像有一个{"age" : 1}索引一样。因此，这个复合索引可以当作{"age" : 1}索引一样使用。
这个可以根据需要推广到尽可能多的键：如果有一个拥有N个键的索引，那么你同时“免费”得到了所有这N个键的前缀组成的索引。举例
来说，如果有一个{"a": 1， "b": 1， "c": 1， ...， "z": 1}索引，那么，实际上我们也可以使用 {"a": 1}、{"a": 1， "b" : 1}、
{"a": 1， "b": 1， "c": 1}等一系列索引。
`注意，这些键的任意子集所组成的索引并不一定可用。例如，使用{"b": 1}或者{"a": 1， "c": 1}作为索引的查询是不会被优化的：
只有能够使用索引前缀的查询才能从中受益`.