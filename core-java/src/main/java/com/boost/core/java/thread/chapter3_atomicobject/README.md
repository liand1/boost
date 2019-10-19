### Java 并发包中原子操作类原理剖析  
+ >JUC包提供了一系列的原子性操作类，这些类都是使用非阻塞算法CAS实现的，相比使用锁实现原子性操作这在性能上有很大提高。由
  >于原子性操作类的原理都大致相同，所以本章只讲解最简单的AtomicLong类的实现原理以及JDK8中新增的LongAdder和
  >LongAccumulator类的理。有了这些基础，再去理解其他原子性操作类的实现就不会感到困难了。

JUC并发包中包含有AtomicInteger、AtomicLong和AtomicBoolean等原子性操作类，它们的原理类似，本章讲解AtomicLong类。
AtomicLong原子性递增或者递减类，其内部使用Unsafe来实现  

### AtomicLong
+ > 高并发情况下AtomicLong还会存在性能问题,通过CAS操作去修改原子变量，在高并发的情况下只会有一个线程CAS操作成功，其它的
  线程只能自旋重新尝试，会占用CPU资源。
### LongAdder
+ > JDK8新增了一个原子性递增或者递减类LongAdder,LongAdder则是内部维护多个Cell变量，多个线程尝试操作变量的
  时候，如果一个线程操作成功了，其它的不会在一棵树上吊死，而是去尝试操作其它的Cell变量，这个成功率显然会大于AtomicLong中
  多个线程对一个原子变量的操作。
  
  1.LongAdder的结构 
    > LongAdder类继承自Striped64类，在Striped64内部维护着三个变量.LongAdder的真实值其实是base的值与Cell数组里
      面所有Cell元素中的value值的累加，base是个基础值，默认为0.cellsBusy用来实现自旋锁，状态值只有0和1当创建
      Cell元素，扩容Cell数组或者初始化Cel数组时，使用CAS操作该变量来保证同时只有一个线程可以进行其中之一的操作. 
                                                                                                                                                                                                                                                                                                                                                                    
  ```
  public void add(long x) {
       Cell[] as; long b, v; int m; Cell a;
       if ((as = cells) ! = null || ! casBase(b = base, b + x)) {//(1)
            boolean uncontended = true;
            if (as == null || (m = as.length -1) < 0 ||//(2)
                (a = as[getProbe() & m]) == null ||//(3)
                !(uncontended = a.cas(v = a.value, v + x)))//(4)
                longAccumulate(x, null, uncontended); //(5)
       }
  }
   
  final boolean casBase(long cmp, long val) {
        return UNSAFE.compareAndSwapLong(this, BASE, cmp, val);
  }

  ```
  
  2.当前线程应该访问Cell数组的哪一个Cell元素  
     > 查看LongAdder的add方法源码:
     代码（1）首先看cells是否为null，如果为null则当前在基础变量base上进行累加，这时候就类似AtomicLong的操作。
             如果cells不为null或者线程执行代码（1）的CAS操作失败了，则会去执行代码（2）。代码（2）（3）决定当前线程应该
     访问cells数组里面的哪一个Cell元素，如果当前线程映射的元素存在则执行代码（4），使用CAS操作去更新分配的Cell元素的
     value值，如果当前线程映射的元素不存在或者存在但是CAS操作失败则执行代码（5）。其实将代码（2）（3）（4）合起来看就
     是获取当前线程应该访问的cells数组的Cell元素，然后进行CAS更新操作，只是在获取期间如果有些条件不满足则会跳转到代
     码（5）执行。另外当前线程应该访问cells数组的哪一个Cell元素是通过getProbe（）& m进行计算的，其中m是当前cells数组元
     素个数-1, getProbe（）则用于获取当前线程中变量threadLocalRandomProbe的值，这个值一开始为0，在代码（5）里面会对其进
     行初始化。并且当前线程通过分配的Cell元素的cas函数来保证对Cell元素value值更新的原子性，到这里我们回答了问题2和问题6。
     下面重点研究longAccumulate的代码逻辑，这是cells数组被初始化和扩容的地方。
     ```
        final void longAccumulate(long x, LongBinaryOperator fn,
                                      boolean wasUncontended) {
                //(6) 初始化当前线程的变量threadLocalRandomProbe的值
                int h;
                if ((h = getProbe()) == 0) {
                    ThreadLocalRandom.current(); //
                    h = getProbe();
                    wasUncontended = true;
                }
                boolean collide = false;
                for (; ; ) {
                    Cell[] as; Cell a; int n; long v;
                    if ((as = cells) ! = null && (n = as.length) > 0) {//(7)
                        if ((a = as[(n -1) & h]) == null) {//(8)
                            if (cellsBusy == 0) {       // Try to attach new Cell
                                Cell r = new Cell(x);   // Optimistically create
                                if (cellsBusy == 0 && casCellsBusy()) {
                                    boolean created = false;
                                    try {               // Recheck under lock
                                        Cell[] rs; int m, j;
                                        if ((rs = cells) ! = null &&
                                            (m = rs.length) > 0 &&
                                            rs[j = (m -1) & h] == null) {
                                            rs[j] = r;
                                            created = true;
                                        }
                                    } finally {
                                        cellsBusy = 0;
                                    }
                                    if (created)
                                        break;
                                    continue;           // Slot is now non-empty
                                }
                            }
                            collide = false;
                        }
                        else if (! wasUncontended)       // CAS already known to fail
                            wasUncontended = true;
                        //当前Cell存在，则执行CAS设置（9）
                        else if (a.cas(v = a.value, ((fn == null) ? v + x :
                                                      fn.applyAsLong(v, x))))
                            break;
                        //当前Cell数组元素个数大于CPU个数（10)
                        else if (n >= NCPU || cells ! = as)
                            collide = false;            // At max size or stale
                        //是否有冲突（11）
                        else if (! collide)
                            collide = true;
                        //如果当前元素个数没有达到CPU个数并且有冲突则扩容（12）
                        else if (cellsBusy == 0 && casCellsBusy()) {
                            try {
                                if (cells == as) {      // Expand table unless stale
                                    //12.1
                                    Cell[] rs = new Cell[n << 1];
                                    for (int i = 0; i < n; ++i)
                                        rs[i] = as[i];
                                    cells = rs;
                                }
                            } finally {
                                //12.2
                                cellsBusy = 0;
         
                            }
                            //12.3
                            collide = false;
                            continue;                   // Retry with expanded table
                        }
                        //（13）为了能够找到一个空闲的Cell，重新计算hash值，xorshift算法生成随机数
                        h = advanceProbe(h);
                    }
                    //初始化Cell数组（14）
                    else if (cellsBusy == 0 && cells == as && casCellsBusy()) {
                        boolean init = false;
                        try {
                            if (cells == as) {
                                //14.1
                                Cell[] rs = new Cell[2];
                                //14.2
                                rs[h & 1] = new Cell(x);
                                cells = rs;
                                init = true;
                            }
                        } finally {
                            //14.3
                            cellsBusy = 0;
                        }
                        if (init)
                            break;
                    }
                    else if (casBase(v = base, ((fn == null) ? v + x :
                                                fn.applyAsLong(v, x))))
                        break;                          // Fall back on using base
                }
            }
     ```
     >上面代码比较复杂，这里我们主要关注问题3、问题4和问题5。
        当每个线程第一次执行到代码（6）时，会初始化当前线程变量threadLocalRandomProbe的值，上面也说了，这个变量在
     计算当前线程应该被分配到cells数组的哪一个Cell元素时会用到。
        cells数组的初始化是在代码（14）中进行的，其中cellsBusy是一个标示，为0说明当前cells数组没有在被初始化或者扩容，
     也没有在新建Cell元素，为1则说明cells数组在被初始化或者扩容，或者当前在创建新的Cell元素、通过CAS操作来进行0或1状态的
     切换，这里使用casCellsBusy函数。假设当前线程通过CAS设置cellsBusy为1，则当前线程开始初始化操作，那么这时候其他线程就
     不能进行扩容了。如代码（14.1）初始化cells数组元素个数为2，然后使用h&1计算当前线程应该访问celll数组的哪个位置，也就
     是使用当前线程的threadLocalRandomProbe变量值&（cells数组元素个数-1），然后标示cells数组已经被初始化，最后代码
     （14.3）重置了cellsBusy标记。显然这里没有使用CAS操作，却是线程安全的，原因是cellsBusy是volatile类型的，这保证了变量
     的内存可见性，另外此时其他地方的代码没有机会修改cellsBusy的值。在这里初始化的cells数组里面的两个元素的值目前还是
     null。这里回答了问题3，知道了cells数组如何被初始化。
     cells数组的扩容是在代码（12）中进行的，对cells扩容是有条件的，也就是代码（10）（11）的条件都不满足的时候。具体就是
     当前cells的元素个数小于当前机器CPU个数并且当前多个线程访问了cells中同一个元素，从而导致冲突使其中一个线程CAS失败时
     才会进行扩容操作。这里为何要涉及CPU个数呢？其实在基础篇中已经讲过，只有当每个CPU都运行一个线程时才会使多线程的效果
     最佳，也就是当cells数组元素个数与CPU个数一致时，每个Cell都使用一个CPU进行处理，这时性能才是最佳的。代码（12）中的
     扩容操作也是先通过CAS设置cellsBusy为1，然后才能进行扩容。假设CAS成功则执行代码（12.1）将容量扩充为之前的2倍，并复
     制Cell元素到扩容后数组。另外，扩容后cells数组里面除了包含复制过来的元素外，还包含其他新元素，这些元素的值目前还是
     null。这里回答了问题4。    
     在代码（7）（8）中，当前线程调用add方法并根据当前线程的随机数threadLocalRandomProbe和cells元素个数计算要访问的Cell
     元素下标，然后如果发现对应下标元素的值为null，则新增一个Cell元素到cells数组，并且在将其添加到cells数组之前要竞争设
     置cellsBusy为1。
     代码（13）对CAS失败的线程重新计算当前线程的随机值threadLocalRandomProbe，以减少下次访问cells元素时的冲突机会。这里
     回答了问题5。

     
  3.如何初始化Cell数组  
  4.Cell数组如何扩容  
  5.线程访问分配的Cell元素有冲突后如何处理  
  6.如何保证线程操作被分配的Cell元素的原子性  
   > 查看Cell类的源码；可以看到，Cell的构造很简单，其内部维护一个被声明为volatile的变量，这里声明为volatile是因为线程操作
     value变量时没有使用锁，为了保证变量的内存可见性这里将其声明为volatile的。另外cas函数通过CAS操作，保证了当前线程更
     新时被分配的Cell元素中value值的原子性。另外，Cell类使用@sun.misc.Contended修饰是为了避免伪共享
     
               
   
### LongAccumulator


case2: JMH分析AtomicLong&LongAdder（进一步了解JMH插件去(chapter5_advance)[https://github.com/liand1/boost/tree/master/core-java/src/main/java/com/boost/core/java/thread/chapter5_advance]）
case3-case4: 对比两者之间的性能