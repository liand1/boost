### Java 并发包中原子操作类原理剖析  
+ >JUC包提供了一系列的原子性操作类，这些类都是使用非阻塞算法CAS实现的，相比使用锁实现原子性操作这在性能上有很大提高。由
  >于原子性操作类的原理都大致相同，所以本章只讲解最简单的AtomicLong类的实现原理以及JDK8中新增的LongAdder和
  >LongAccumulator类的理。有了这些基础，再去理解其他原子性操作类的实现就不会感到困难了。

JUC并发包中包含有Atomiclnteger、AtomicLong和AtomicBoolean等原子性操作类，它们的原理类似，本章讲解AtomicLong类。
AtomicLong原子性递增或者递减类，其内部使用Unsafe来实现  
