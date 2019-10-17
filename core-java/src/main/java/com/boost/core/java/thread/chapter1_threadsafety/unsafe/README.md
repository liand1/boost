### Unsafe类
>JDK中的rt.jar保重Unsafe类中提供了硬件级别的原子性操作，Unsafe类中的方法都是native方法，他们使用JNI的方式访问C++实现库，
下面我们来了解一下Unsafe提供的几个主要方法以及编程时如何使用Unsafe类做一些事情。
--- 
我们来看一下它的重要的方法：  
+ ```public native void park(boolean isAbsolute, long time);```
  >阻塞当前线程（这个方法在AQS中的使对象等待时会使用到）。isAbsoulte的为是否是绝对时间，如果isAbsolute为false，
  其中time为大于0，那么当前线程会在等待time后唤醒。如果isAbsolute为false，其中time为0，那么线程为无限等待。如果
  isAbsolute为true，那么time就是指的是绝对时间也就是换算为ms后的绝对时间。另外，当其他线程调用了当前阻塞线程的interrupt
  方法而中断了当前线程时，当前线程也会返回，而其他线程调用unPark方法并且把当前线程作为参数时也会返回
+ ```void unpark(Object thread);```  
  >唤醒调用park线程的线程 
+ ```long getAndSetLong(Object obj,long offset,long update);```  
  >查看源码可以知先通过getLongVolatile获取当前变量的值，然后使用CAS原子操作设置新值。这里使用while循环时考虑到，在多个线
  程同时调用的情况下CAS失败时需要重试。  

