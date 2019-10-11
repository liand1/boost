package com.boost.core.java.thread.chapter1_threadsafety.notify;

/**
 * 生产者消费者
 */
public class Case1 {
    public static void main(String[] args) {
        Object lock = new Object();
        Producer producer = new Producer(lock);
        Consumer consumer = new Consumer(lock);

        new Thread(new ThreadP(producer), "producer").start();
        new Thread(new ThreadC(consumer), "consumer").start();
    }

}

class ValueObject {
    public static String value = "";
}

class Producer {
    private Object lock;

    public Producer(Object lock) {
        this.lock = lock;
    }

    public void setValue() {
        try {
            synchronized (lock) {
                if (!ValueObject.value.equals("")) {
                    lock.wait();
                }
                String value = System.currentTimeMillis() + "_" + System.nanoTime();
                System.out.println("set value is " + value);
                ValueObject.value = value;
                lock.notify();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer {
    private Object lock;

    public Consumer(Object lock) {
        this.lock = lock;
    }

    public void getValue() {
        try {
            synchronized (lock) {
                if (ValueObject.value.equals("")) {
                    lock.wait();
                }
                System.out.println("get value is" + ValueObject.value);
                ValueObject.value = "";
                lock.notify();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ThreadP implements Runnable {

    private Producer p;

    public ThreadP(Producer p) {
        this.p = p;
    }

    @Override
    public void run() {
        while (true) {
            p.setValue();
        }
    }
}

class ThreadC implements Runnable {
    private Consumer c;

    public ThreadC(Consumer c) {
        this.c = c;
    }

    @Override
    public void run() {
        while (true) {
            c.getValue();
        }
    }
}
