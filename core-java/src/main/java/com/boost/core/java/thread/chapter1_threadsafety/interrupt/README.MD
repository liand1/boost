### 停止线程
    通常我们会有这样的需求，即停止一个线程。在java的api中有stop、suspend等方法可以达到目的，但由于这些方法在使用上存在不
    安全性，会带来不好的副作用，不建议被使用。[具体原因](https://docs.oracle.com/javase/6/docs/technotes/guides/concurrency/threadPrimitiveDeprecation.html)
    
### interrupt
    1.interrupt()
        其作用是中断此线程（此线程不一定是当前线程，而是指调用该方法的Thread实例所代表的线程），但实际上只是给线程设置一
        个中断标志，线程仍会继续运行。还需要加入一个判断才可以完成线程的停止。
        
    2.interrupted()
        作用是测试当前线程是否被中断（检查中断标志），返回一个boolean并清除中断状态，第二次再调用时中断状态已经被清除，
        将返回一个false。
        
    3.isInterrupted()
        作用是只测试此线程是否被中断 ，不清除中断状态。    
        
           
case1:调用interrupt方法不是真正的停止线程  
case2:判断线程是否是停止状态 interrupted  
case3:this.isInterrupted()测试线程Thread对象是否已经是中断状态，但不清除标志  
case4:要解决完全退出run方法的情况，这里我们采用了抛出InterruptedException  
case5:睡眠中中断线程  
case6:先中断线程，然后进入到sleep        