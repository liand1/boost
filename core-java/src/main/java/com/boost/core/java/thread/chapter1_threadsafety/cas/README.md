### Java中的CAS操作  优点，无锁，性能好(CAS都是硬件级别的操作，因此效率会高一些)，缺点，自旋。
 >在Java中，锁在并发处理中占据了一席之地，但是使用锁有一个不好的地方，就是当一个线程没有获取到锁时会被阻塞挂起，这会导致
 线程上下文的切换和重新调度开销。Java提供了非阻塞的volatile关键字来解决共享变量的可见性问题，这在一定程度上弥补了锁带来
 的开销问题，但是volatile只能保证共享变量的可见性，不能解决读—改—写等的原子性问题。CAS即Compare and Swap，其是JDK提供
 的非阻塞原子性操作，它通过硬件保证了比较—更新操作的原子性。JDK里面的Unsafe类提供了一系列的compareAndSwap*方法，下面
 以compareAndSwapLong方法为例进行简单介绍。
+ boolean compareAndSwapLong（Object obj, long valueOffset, long expect, long update）方法：其中compareAndSwap的意思是比
较并交换。CAS有四个操作数，分别为：对象内存位置、对象中的变量的偏移量(我们可以简单地把valueOffset理解为变量的内存地址[内存偏移百度百科](https://baike.baidu.com/item/%E5%81%8F%E7%A7%BB%E5%9C%B0%E5%9D%80/3108819?fr=aladdin))、
变量预期值和新的值。其操作含义是，如果对象obj中内存偏移量为valueOffset的变量值为expect，则使用新的值update替换旧的值
expect。这是处理器提供的一个原子性指令。
>关于CAS操作有个经典的ABA问题，具体如下：假如线程I使用CAS修改初始值为A的变量X，那么线程I会首先去获取当前变量X的值
（为A），然后使用CAS操作尝试修改X的值为B，如果使用CAS操作成功了，那么程序运行一定是正确的吗？其实未必，这是因为有可能在
线程I获取变量X的值A后，在执行CAS前，线程II使用CAS修改了变量X的值为B，然后又使用CAS修改了变量X的值为A。所以虽然线程I执行
CAS时X的值是A，但是这个A已经不是线程I获取时的A了。这就是ABA问题。
       ABA问题的产生是因为变量的状态值产生了环形转换，就是变量的值可以从A到B，然后再从B到A。如果变量的值只能朝着一个方
向转换，比如A到B, B到C，不构成环形，就不会存在问题。JDK中的AtomicStampedReference类给每个变量的状态值都配备了一个时间戳
，从而避免了ABA问题的产生。

CAS 有三个参数：V，A，B。内存值 V、旧的预期值 A、要修改的值 B
CAS操作大概有如下几步:

>读取旧值为一个临时变量， 将V = A
对旧值的临时变量进行操作或者依赖旧值临时变量进行一些操作
判断旧值临时变量是不是等于旧值，等于则没被修改，那么新值写入；不等于则被修改，此时放弃或者从步骤1重试。`这时候线程里面的
内除值是刷新了的，就是V已经是之前的内存修改过后的值，线程二会重新读取主内存中的V值，所以再重试的时候会发现已经相等了， 
这时候执行CAS就成功了。`

#### 举例
挪用银行1000万，赚取了60万利息之后又放回去了。

理解ABA， 查看AtomicInteger源码，里面有涉及CAS, 因为java.util.concurrent.atomic 包下的原子操作类都是基于 CAS 实现的

#### 接下来看unsafe模块->Unsafe是CAS的核心类  


#### CAS和synchronized区别
synchronized在高并发的情况下使用，wait方法不占用cpu资源
CAS在锁竞争不激烈的情况下使用，因为可以很轻易拿到锁，否则如果长时间拿不到锁，自旋是会耗CPU性能的



