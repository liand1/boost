### Java 并发包中并发队列原理剖析
JDK中提供了一系列场景的并发安全队列。总的来说，按照实现方式的不同可分为阻
塞队列和非阻塞队列，前者使用锁实现，而后者则使用CAS非阻塞算法实现.

### ConcurrentLinkedQueue
>线程安全的无界非阻塞队列，其底层数据结构使用单向链表
实现，对于入队和出队操作使用CAS来实现线程安全。下面我们来看具体实现，1 非阻塞队列–ConcurrentLinkedQueue
ConcurrentLinkedQueue是无界非阻塞队列，内部使用单项链表实现；其中有两个volatile 类型的Node 节点分别用来存放队列的首、尾节
点。


总结：
1：对于map/set的选择使用
HashMap
TreeMap
LinkedHashMap

Hashtable
Collections.sychronizedXXX 并发不高的情况下

ConcurrentHashMap
ConcurrentSkipListMap  并发高的情况下

2：队列
ArrayList
LinkedList
Collections.synchronizedXXX  并发不高的情况下
CopyOnWriteList 性能很低  在监听器队列里面用的多
Queue
	ConcurrentLinkedQueue //concurrentArrayQueue 内部加锁
	BlockingQueue 阻塞式队列
		LinkedBQ
		ArrayBQ
		TransferQueue  在游戏服务器接受转化消息用的多
		SynchronusQueue
	    DelayQueue执行定时任务


