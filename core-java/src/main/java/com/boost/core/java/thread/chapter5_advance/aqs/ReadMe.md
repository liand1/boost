case1: 
case2: 实现一把锁(使用synchronized)    
case3: 实现一把锁(使用AQS)    
case4: ConditionObject  
case5: 基于AQS实现的不可重入的独占锁 
 
### 抽象同步队列 AQS 概述
#### AQS一一锁的底层支持  
+ >AbstractQueuedSynchronizer 抽象同步队列简称 AQS ，它是实现同步器的基础组件，
并发包中锁的底层就是使用AQS 实现的 另外，大多数开发者可能永远不会直接使用
AQS ，但是知道其原理对于架构设计还是很有帮助的。
  
    `AQS是一个FIFO的双向队列，其内部通过节点head和tail记录队首和队尾元素，队列元素的类型为Node。其中Node中的thread变量用来存
放进入AQS队列里面的线程；Node节点内部的SHARED用来标记该线程是获取共享资源时被阻塞挂起后放入AQS队列的，EXCLUSIVE用来标记
线程是获取独占资源时被挂起后放入AQS队列的；waitStatus记录当前线程等待状态，可以为CANCELLED（线程被取消了）、
SIGNAL（线程需要被唤醒）、CONDITION（线程在条件队列里面等待）、PROPAGATE（释放共享资源时需要通知其他节点）; 
prev记录当前节点的前驱节点，next记录当前节点的后继节点。`

#### 源码
> 单一的状态信息，可以通过 getState()、setState()、compareAndSetState() 函数修改其值，对于不同的实现有不同的意义，如
+ ReentrantLock： 当前线程获取锁的可重入次数
+ ReentrantReadWriteLock：高 16 位表示获取读锁的次数，低 16 位表示获取写锁的次数
+ semaphore： 当前可用信号的个数
+ CountDownLatch： 计数器当前的值

对state操作是AQS线程同步的关键。根据state是否属一个线程，操作state的方式分为独占方式和共享方式。

#### 独占方式
>如果一个线程获取到了资源，就会标记这个线程获取到了，其他线程尝试操作 state 获取资源时会发现当前该资源不是自己持有的，
就会在获取失败后被阻塞, 比如独占锁ReentrantLock的实现，当一个线程获取了ReentrantLock的锁后，在AQS内部会首先使用CAS操作
把state状态值从0变为1，然后设置当前锁的持有者为当前线程，当该线程再次获取锁时发现它就是锁的持有者，则会把状态值从1变
为2，也就是设置可重入次数，而当另外一个线程获取锁时发现自己并不是该锁的持有者就会被放入AQS阻塞队列后挂起。          
在独占方式下获取和释放资源使用的方法为： 
void acquire（int arg）void acquireInterruptibly（int arg）boolean release（int arg）
```
/**
*当一个线程调用acquire（int arg）方法获取独占资源时，会首先使用tryAcquire方法尝试获取资源，具体是设置状态变量state的值，
*成功则直接返回，失败则将当前线程封装为类型为Node.EXCLUSIVE的Node节点后插入到AQS阻塞队列的尾部，并调用
*LockSupport.park（this）方法挂起自己
*/
 public final void acquire(int arg) {
           if (!tryAcquire(arg) &&
               acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
               selfInterrupt();
 }
```
```
/**
*当一个线程调用release（int arg）方法时会尝试使用tryRelease操作释放资源，这里是设置状态变量state的值，然后调用
*LockSupport.unpark（thread）方法激活AQS队列里面被阻塞的一个线程（thread）。被激活的线程则使用tryAcquire尝试，看当前状态
*变量state的值是否能满足自己的需要，满足则该线程被激活，然后继续向下运行，否则还是会被放入AQS队列并被挂起。
*/
 public final boolean release(int arg) {
         if (tryRelease(arg)) {
             Node h = head;
             if (h ! = null && h.waitStatus ! = 0)
                 unparkSuccessor(h);
             return true;
         }
         return false;
     }
```


#### 共享方式
> 当多个线程去请求资源时通过 CAS 方式竞争获取资源，当一个线程获取到了资源后，另一个线程再次去获取时，如果当前资源还能满
足它的需要，则当前线程需要使用 CAS 方式进行获取即可，如果不满足，则把当前线程放入阻塞队列，比如资源不足以满足需求
 在共享方式下获取和释放资源的方法为： 
>void acquireShared（int arg）void acquireSharedInterruptibly（int arg）boolean releaseShared（int arg）

#### 入队操作
>当一个线程获取锁失败后该线程会被转换为Node节点，然后就会使用enq（final Node node）方法将该节点插入到AQS的阻塞队列。
```
private Node enq(final Node node) {
        for (;;) {
            Node t = tail;// （1）
            if (t == null) { // Must initialize
                if (compareAndSetHead(new Node()))// （2）
                    tail = head;
            } else {
                node.prev = t;// （3）
                if (compareAndSetTail(t, node)) {// （4）
                    t.next = node;
                    return t;
                }
            }
        }
    }
```

#### Condition condition = lock.newCondition();
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


    


