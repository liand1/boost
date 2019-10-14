类ReentrantLock具有完全互斥排他的效果，即同一时间只有一个线程在执行ReentrantLock.lock()方法后面的任务。这样做虽然保证了实例变量的线程安全性，但效率却是非常低下的。所以在JDK中提供了一种读写锁ReentrantReadWriteLock类，使用它可以加快运行效率，在某些不需要操作实例变量的方法中，完全可以使用读写锁ReentrantReadWriteLock来提升该方法的代码运行速度。
读写锁表示也有两个锁，一个是读操作相关的锁，也称为共享锁;另一个是写操作相关的锁，也叫排他锁。也就是多个读锁之间不互斥，读锁与写锁互斥，写锁与写锁互斥。在没有线程Thread进行写人操作时，进行读取操作的多个Thread都可以获取读锁，而进行写人操作的Thread只有在获取写锁后才能进行写人操作。即多个Thread可以同时进行读取操作，但是同一时刻只允许一个Thread进行写人操作。


ReentrantReadWriteLock是Lock的另一种实现方式，我们已经知道了ReentrantLock是一个排他锁，同一时间只允许一个线程访问，而ReentrantReadWriteLock允许多个读线程同时访问，但不允许写线程和读线程、写线程和写线程同时访问。相对于排他锁，提高了并发性。在实际应用中，大部分情况下对共享数据（如缓存）的访问都是读操作远多于写操作，这时ReentrantReadWriteLock能够提供比排他锁更好的并发性和吞吐量。

　　读写锁内部维护了两个锁，一个用于读操作，一个用于写操作。所有 ReadWriteLock实现都必须保证 writeLock操作的内存同步效果也要保持与相关 readLock的联系。也就是说，成功获取读锁的线程会看到写入锁之前版本所做的所有更新。

　　ReentrantReadWriteLock支持以下功能:  

　　　　1）支持公平和非公平的获取锁的方式;  
　　　　2）支持可重入。读线程在获取了读锁后还可以获取读锁；写线程在获取了写锁之后既可以再次获取写锁又可以获取读锁；
　　　　3）还允许从写入锁降级为读取锁，其实现方式是：先获取写入锁，然后获取读取锁，最后释放写入锁。但是，从读取锁升级到写入锁是不允许的;  
　　　　4）读取锁和写入锁都支持锁获取期间的中断;  
　　　　5）Condition支持。仅写入锁提供了一个 Condition 实现；读取锁不支持 Condition ，readLock().newCondition() 会抛出 UnsupportedOperationException.  