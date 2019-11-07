###ReentrantLock和synchronized的区别  



case4: tryLock  
case5: ReentrantLock 公平锁和非公平锁 
case6: 使用Condition  
case7: 使用多个Condition通知部分线程 (错误用法)  
case8: 使用多个Condition通知部分线程 (正确用法)  
case9: 使用ReentrantLock实现生产者/消费者模式 (一对一)  
case10: 使用ReentrantLock实现生产者/消费者模式 (多对多)  
case11: ReentrantLock常用方法  
case12: 使用Condition实现顺序执行
case13: park+自旋实现一把锁(TODO)

lock.newCondition（）的作用其实是new了一个在AQS内部声明的ConditionObject对象，ConditionObject是AQS的内部类，可以访问AQS
内部的变量（例如状态变量state）和方法。在每个条件变量内部都维护了一个条件队列，用来存放调用条件变量的await（）方法时被
阻塞的线程。注意这个条件队列和AQS队列不是一回事。
在如下代码中，当线程调用条件变量的await（）方法时（必须先调用锁的lock（）方法获取锁），在内部会构造一个类型为
Node.CONDITION的node节点，然后将该节点插入条件队列末尾，之后当前线程会释放获取的锁（也就是会操作锁对应的state变量的值）
，并被阻塞挂起。这时候如果有其他线程调用lock.lock（）尝试获取锁，就会有一个线程获取到锁，如果获取到锁的线程调用了条件变
量的await（）方法，则该线程也会被放入条件变量的阻塞队列，然后释放获取到的锁，在await（）方法处阻塞。
```
public final void await() throws InterruptedException {
          if (Thread.interrupted())
              throw new InterruptedException();
            //创建新的node节点，并插入到条件队列末尾（9）
          Node node = addConditionWaiter();
 
          //释放当前线程获取的锁（10）
          int savedState = fullyRelease(node);
          int interruptMode = 0;
          //调用park方法阻塞挂起当前线程（11）
          while (! isOnSyncQueue(node)) {
              LockSupport.park(this);
              if ((interruptMode = checkInterruptWhileWaiting(node)) ! = 0)
                  break;
          }
          ...
    }
```
在如下代码中，当另外一个线程调用条件变量的signal方法时（必须先调用锁的lock（）方法获取锁），在内部会把条件队列里面队头的
一个线程节点从条件队列里面移除并放入AQS的阻塞队列里面，然后激活这个线程。
```
public final void signal() {
      if (! isHeldExclusively())
          throw new IllegalMonitorStateException();
      Node first = firstWaiter;
      if (first ! = null)
        //将条件队列头元素移动到AQS队列
          doSignal(first);
  }

```

