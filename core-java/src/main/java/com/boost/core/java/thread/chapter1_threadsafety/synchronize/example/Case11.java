package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;


/**
 * 多个线程调用同一个对象中的不同名称的synchronized同步方法或synchronized(this)同步代码块时，调用的效果就是按顺序执行，
 * 也就是同步的，阻塞的。
 * 这说明synchronized同步方法或synchronized(this)同步代码块分别有两种作用
 * （1）synchronized同步方法
 * 1）对其他synchronized同步方法或synchronized(this)同步代码块调用呈阻塞状态
 * 2）同一时间只有一个线程可以执行synchronized同步方法中的代码
 * （2）synchronized(this)同步代码块
 * 1）对其他synchronized同步方法或synchronized(this)同步代码块调用呈阻塞状态
 * 2）同一时间只有一个线程可以执行synchronized(this)同步代码块中的代码
 *
 * Java还支持对“任意对象”作为“对象监视器”来实现同步的功能。这个“任意对象”大多数是实例变量及方法的参数，
 * 使用synchronized(非this对象)
 * synchronized(非this对象)格式的作用只有1中：synchronized(非this对象x)同步代码块
 *
 * 1）在多个线程持有“对象监视器”为同一个对象的前提下，同一时间只有一个线程可以执行synchronized(非this对象x)同步代码块中的代码
 * 2）当持有“对象监视器”为同一个对象的前提下，同一时间只有一个线程可以执行synchronized(非this对象x)同步代码块中的代码。
 *
 * 锁非this对象的优点：如果在一个类中有很多个synchronized方法，这时虽然能实现同步，但会受到阻塞，所以 影响效率；
 *  但如果使用同步代码块锁非this对象，则synchronized(非this)代码块中的程序与同步方法是异步的，不与其他锁非this同步方法争
 *  抢this锁，则可以大大提高运行效率。
 *  由于对象监视器不同，所以运行结果就是异步的
 */
public class Case11 {

    public static void main(String[] args) {
        Service service = new Service();
        Thread threadA = new Case11ThreadA(service);
        threadA.setName("A");
        threadA.start();
        Thread threadB = new Case11ThreadB(service);
        threadB.setName("B");
        threadB.start();
    }
}

class Service {

    private String usernameParam;
    private String passwordParam;

    private Object object = new Object();

    public void setUsernamePassword(String username, String password) {
        try {

            // 锁其对象为同一个
            synchronized (object) {
                System.out.println("线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "进入同步");
                usernameParam = username;
                Thread.sleep(2000);
                passwordParam = password;
                System.out.println("线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "离开同步");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Case11ThreadA extends Thread {
    private Service service;

    public Case11ThreadA(Service service) {
        super();
        this.service = service;
    }

    public void run() {
        service.setUsernamePassword("a", "aa");
    }
}

class Case11ThreadB extends Thread {
    private Service service;

    public Case11ThreadB(Service service) {
        super();
        this.service = service;
    }

    public void run() {
        service.setUsernamePassword("b", "bb");
    }

}


