### 用Optional取代null
+  >本章内容
 null引用引发的问题，以及为什么要避免null引用
 从null到Optional：以null安全的方式重写你的域模型
 让Optional发光发热： 去除代码中对null的检查
 读取Optional中可能值的几种方法
 对可能缺失值的再思考
 重要的一点是 Optional 不是 Serializable。因此，它不应该用作类的字段。
   
case1: 创建 Optional 对象  
case2: 使用 map 从 Optional 对象中提取和转换值

