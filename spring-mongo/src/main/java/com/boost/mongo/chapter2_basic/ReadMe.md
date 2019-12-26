#### 2.1    文档
>文档是Mongodb的核心概念，文档就是键值对的一个有序集，文档的键是字符串，除了少数例外情况，键可以是任意UTF-8字符。
键不能含有\0(空字符），这个字符表示键的结尾
.和$具有特殊意义，只能在特定环境下使用
文档中的值可以是多种不同的数据类型（甚至是一个完整的内嵌文档）。Mongodb不但区分类型，而且区分大小写，还有Mongodb的文档不
能有重复的键。文档中的键值对是有序的：{ “x”:1, “y”:2}与{ “y”:2, “x”:1}是不同的，通常字段顺序并不重要，无须让数据
库模型依赖特定的字段顺序（Mongodb会对字段重新排序），只有在某些特殊情况下，字段顺序才变得非常重要。

#### 2.2    集合
>集合就是一组文档，如果将Mongodb中的一个文档比喻为关系数据库中的行，那么一个集合就相当于一张表。集合是动态模式，里面可以
放任何文档。
集合名可以是满足下列条件的任意UTF-8字符串
+> 集合名不能是空字符串
+> 集合名不能包含\0字符，这个字符表示集合名的结束
+> 集合名不能以“system.”开头，这是系统集合保留的前缀，例如system.users这个集合保存这数据库的用户信息，而system.namespace
  集合保存这所有数据库集合的信息。
+> 用户创建的集合名最好不要包含$符号，因为某些系统生成的集合中包含$。

#### 创建.mongorc.js文件
如果某些脚本会被频繁加载，可以将他们添加到.mongorc.js文件中，这个文件会在启动shell时自动运行。最常见的用途之一时移除
哪些比较危险的shell辅助函数。可以在这里集中重写这些方法
```
//禁止删除数据库
db.dropDatabase = DB.prototype.dropDatabase = no;
//禁止删除集合
DBCollection.propotype.drop=no
//禁止删除索引
DBCollection.propotype.dropIndex=no
```
如果在启动shell指定 --norc参数，就可以禁止加载.mongorc.js

