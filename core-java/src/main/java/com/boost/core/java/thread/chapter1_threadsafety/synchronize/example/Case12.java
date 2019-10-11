package com.boost.core.java.thread.chapter1_threadsafety.synchronize.example;

/**
 * 所以，使用synchronized(非this对象)同步代码块格式进行同步操作时，对象监视器必须是同一个对象，如果不是同一个对象监视器，
 * 运行的结果就是异步调用了，就会交叉运行。
 */
public class Case12 {

    public static void main(String[] args) {
        Case12Service service = new Case12Service();
        Thread threadA = new Case12ThreadA(service);
        threadA.setName("A");
        threadA.start();
        Thread threadB = new Case12ThreadB(service);
        threadB.setName("B");
        threadB.start();
    }
}

class Case12Service {

    private String usernameParam;
    private String passwordParam;



    public void setUsernamePassword(String username, String password) {
        try {
            Object object = new Object();
            // 锁其对象不同
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

class Case12ThreadA extends Thread {
    private Case12Service service;

    public Case12ThreadA(Case12Service service) {
        super();
        this.service = service;
    }

    public void run() {
        service.setUsernamePassword("a", "aa");
    }
}

class Case12ThreadB extends Thread {
    private Case12Service service;

    public Case12ThreadB(Case12Service service) {
        super();
        this.service = service;
    }

    public void run() {
        service.setUsernamePassword("b", "bb");
    }

}
