package com.boost.core.java.thread.chapter5_advance.locksupport;

import java.util.concurrent.locks.LockSupport;

/**
 * 与park 方法类似，如果调用 park 方法的线程已经 到了与 LockSupport 关联的许可证，
 * 则调用 LockSupport.parkNanos(long nanos）方法后会马上返回。该方法的不同在于 ，如果
 * 没有拿到许可证，则调用线程会被挂起 nanos 时间后修改为自动返回。
 * 另外 park 方法还支持带有 blocker 参数的方法 void park(Object blocker 方法，当钱程
 * 在没有持有许可证的情况下调用 park 方法而被阻塞挂起时 ，这个 blocker对象会被记录到
 * 该线程内部
 * 使用诊断工具可以观察线程被阻塞的原因，诊断工具是通过调用 getB ocker(T read)
 * 方法来获取 blocker对象的，所以 JDK 推荐我们使用带有bocker参数的 park 方法，并且
 * blocker 被设置为 this 这样当在打印线程堆横排查 问题时就能知道是哪个类被阻塞了。
 *
 * 先使用jsp命令查看当前线程，然后使用jstack pid命令查看线程堆栈可以看到如下结果
 * "main" #1 prio=5 os_prio=0 tid=0x0000000002bbe800 nid=0x2a34 waiting on condition [0x000000000303f000]
 *    java.lang.Thread.State: WAITING (parking)
 *         at sun.misc.Unsafe.park(Native Method)
 *         at java.util.concurrent.locks.LockSupport.park(LockSupport.java:304)
 *         at com.boost.core.java.thread.chapter5_advance.locksupport.Case4.testPark(Case4.java:19)
 *         at com.boost.core.java.thread.chapter5_advance.locksupport.Case4.main(Case4.java:23)
 *  修改代码（1）为 LockSupport. park( this 后运行代码， jstack pid 输出结果为
 *  "main" #1 prio=5 os_prio=0 tid=0x00000000021ee800 nid=0x3648 waiting on condition [0x000000000279f000]
 *    java.lang.Thread.State: WAITING (parking)
 *         at sun.misc.Unsafe.park(Native Method)
 *         - parking to wait for  <0x000000076ae301d0> (a com.boost.core.java.thread.chapter5_advance.locksupport.Case4)
 *         at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
 *         at com.boost.core.java.thread.chapter5_advance.locksupport.Case4.testPark(Case4.java:30)
 *         at com.boost.core.java.thread.chapter5_advance.locksupport.Case4.main(Case4.java:34)
 *  使用带 block 参数的 pa 方法，线程堆枝可 提供更多有关阻塞对象的信息。
 */
public class Case4 {

    public void testPark() {
//        LockSupport.park();
        LockSupport.park(this);// (1)
    }

    public static void main(String[] args) {
        new Case4().testPark();

    }
}
