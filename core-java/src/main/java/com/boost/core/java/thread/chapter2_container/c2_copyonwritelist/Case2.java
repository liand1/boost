package com.boost.core.java.thread.chapter2_container.c2_copyonwritelist;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 弱一致性, 所谓弱一致性是指返回迭代器后，其他线程对list的增删改对迭代器不可见, 因为他们操作的是不同数组，这就是弱一
 * 致性
 */
public class Case2 {

    private static volatile CopyOnWriteArrayList<String> arrayList = new CopyOnWriteArrayList<>();

    /**
     * 运行结果证明，修改没有生效，弱一致性的原因， 因为父线程获取的是旧的数组
     * @param args
     * @throws InterruptedException
     */
    public static void main( String[] args ) throws InterruptedException
    {
        arrayList.add("hello");
        arrayList.add("Java");
        arrayList.add("welcome");
        arrayList.add("to");
        arrayList.add("hangzhou");

        Thread threadOne = new Thread(new Runnable() {

            @Override
            public void run() {

                //修改list中下标为1的元素为JavaSe
                arrayList.set(1, "JavaSe");
                //删除元素
                arrayList.remove(2);
                arrayList.remove(3);

            }
        });

        //保证在修改线程启动前获取迭代器
        Iterator<String> itr = arrayList.iterator();

        //启动线程
        threadOne.start();

        //等在子线程执行完毕
        threadOne.join();

        //迭代元素
        while(itr.hasNext()){
            System.out.println(itr.next());
        }

    }

}
