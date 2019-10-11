package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;

/**
 * 由于对象监视器不同，所以运行结果就是异步的
 *
 * 同步代码块放在非同步synchronized方法中进行声明，并不能保证调用方法的线程的执行同步/顺序性，也就是线程调用方法的顺序
 * 是无序的，虽然在同步块中执行的顺序是同步的，这样就容易出现脏读问题。
 */
public class Case13 {

    public static void main(String[] args) {
        Case13Service service = new Case13Service();
        Thread threadA = new Case13ThreadA(service);
        threadA.setName("A");
        threadA.start();
        Thread threadB = new Case13ThreadB(service);
        threadB.setName("B");
        threadB.start();
    }
}

class Case13Service {

    private String anyThing = new String();
    public void a(){
        try {
            synchronized (anyThing){
                System.out.println("a begin");
                Thread.sleep(2000);
                System.out.println("a end");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    synchronized  public void b(){
        System.out.println(" b begin");
        System.out.println(" b end");
    }
}

class Case13ThreadA extends Thread {
    private Case13Service service;

    public Case13ThreadA(Case13Service service) {
        super();
        this.service = service;
    }

    public void run() {
        service.a();
    }
}

class Case13ThreadB extends Thread {
    private Case13Service service;

    public Case13ThreadB(Case13Service service) {
        super();
        this.service = service;
    }

    public void run() {
        service.b();
    }

}
