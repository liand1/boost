case1: park  
case2: unpark  
case3: park+unpark  
case4: park(Object blocker)  
case5: 实现一个先进先出锁  

### LockSupport类探究
+ > JDK 中的 rt.jar 里面的 LockSupport 是个工具类，主要作用是挂起和唤醒线程，它是创建锁和其它同步类的基础。
LockSupport 类与每个使用它的线程都会关联一个许可证,默认调用 LockSupport 类的方法的线程是不持有许可证的，LockSupport 内部
使用 Unsafe 类实现

在Java多线程中，当需要阻塞或者唤醒一个线程时，都会使用LockSupport工具类来完成相应的工作。LockSupport定义了一组公共静态
方法，这些方法提供了最基本的线程阻塞和唤醒功能，而LockSupport也因此成为了构建同步组件的基础工具。

LockSupport定义了一组以park开头的方法用来阻塞当前线程，以及unpark(Thread)方法来唤醒一个被阻塞的线程，这些方法描述如下：

方法名称   |                    描  述
-------    | -------
park()     | 阻塞当前线程，如果掉用unpark(Thread)方法或被中断，才能从park()返回  
parkNanos(long nanos)| 阻塞当前线程，超时返回，阻塞时间最长不超过nanos纳秒
parkUntil(long deadline)| 阻塞当前线程，直到deadline时间点
unpark(Thread)          | 唤醒处于阻塞状态的线程