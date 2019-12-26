### 线程的阻塞和唤醒会涉及到线程上下文的切换（需要了解）
比如从线程1切换到线程2，会消耗资源和时间：


### 概念

>  方法wait的作用是使当前执行代码的线程进行等待，该方法将当前线程置入"预执行队列"中
  。并且在wait所在的代码处停止执行，直到接到通过或者被中断为止。
  在调用wait之前，线程必须获得该对象的对象级别锁，即只能在同步方法或同步块中调用wait
  方法。在执行wait方法后，当前线程释放锁。在从wait返回前，线程与其他线程竞争重新获得锁。
  当一个线程调用一个共享变量的wait（）方法时，该调用线程会被阻塞挂起，直到发生下面几件事情之一才返回：
  1）其他线程调用了该共享对象的notify（）或者notifyAll（）方法；
  2）其他线程调用了该线程的interrupt（）方法，该线程抛出InterruptedException异常返回。
       另外需要注意的是，如果调用wait（）方法的线程没有事先获取该对象的监视器锁，则调用wait（）方法时调用线程会
       抛出IllegalMonitorStateException异常。
  另外需要注意的是，一个线程可以从挂起状态变为可以运行状态（ 就是被唤醒），
  即使该线程没有被其他线程调用 notify()、 notifyAll()进行通知，或者被中断，或者等
  待超时，这就是所谓的虚假唤醒(比如在一个队列里面，size已经满了，这时候调用了wait方法挂起，但是这时候被虚假唤醒了，
  如果不判断size是要出问题的。).虽然虚假唤醒在应用实践中很少发生，但要防患于未然，做法就是不停去测试该线
  程被唤醒的条件是否满足