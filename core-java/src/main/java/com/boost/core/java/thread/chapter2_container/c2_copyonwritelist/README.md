### 主要方法源码解析


添加元素:
```
public boolean add(E e) {

        //加独占锁（1）
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            //获取array(2)
            Object[] elements = getArray();

            //拷贝array到新数组，添加元素到新数组(3)
            int len = elements.length;
            Object[] newElements = Arrays.copyOf(elements, len + 1);
            newElements[len] = e;

            //使用新数组替换添加前的数组(4)
            setArray(newElements);
            return true;
        } finally {
            //释放独占锁(5)
            lock.unlock();
        }
 }
```
> 如上代码，调用add方法的线程会首先执行代码（1）去获取独占锁，如果多个线程都调用add则只有一个线程会获取该锁，其他线程会被
阻塞挂起知道锁被释放。所以一个线程获取到锁后，就保证了在该线程添加元素过程中，其他线程不会对array进行修改。
线程获取锁后执行代码（2）获取array，然后执行代码（3）拷贝array到一个新数组（从这里可以知道新数组的大小是原来数组大小加
增加1，所以CopyOnWriteArrayList是无界List），并把要新增的元素添加到新数组。
然后执行到代码（4）把新数组替换原数组，然后返回前要释放锁，由于加了锁，所以整个add过程是个原子操作，需要注意的是添加元
素时候首先是拷贝了一个快照，然后在快照上进行的添加，而不是直接在源来的数组上进行。

修改，删除类似就不一一介绍了。

### 接下来要讲解一下弱一致性的迭代器
遍历列表元素可以使用迭代器进行迭代操作，讲解什么是迭代器的弱一致性前先上一个例子说明下迭代器的使用。代码如下:
```
public class CopyOnWriteArrayListTest {
    public static void main( String[] args ) {
        CopyOnWriteArrayList<String> arrayList = new CopyOnWriteArrayList<>();
        arrayList.add("hello");
        arrayList.add("java");

        Iterator<String> itr = arrayList.iterator();
        while(itr.hasNext()){
            System.out.println(itr.next());// print: hello \n java
        }

    }
}
```

> 其中迭代器的hasNext方法用来判断是否还有元素，next方法则是具体返回元素。那么接下来看CopyOnWriteArrayList中迭代器是弱一致
性，所谓弱一致性是指返回迭代器后，其他线程对list的增删改对迭代器不可见，无感知的
```
public Iterator<E> iterator() {
    return new COWIterator<E>(getArray(), 0);
}

static final class COWIterator<E> implements ListIterator<E> {
    //array的快照版本
    private final Object[] snapshot;

    //数组下标
    private int cursor;

    //构造函数
    private COWIterator(Object[] elements, int initialCursor) {
        cursor = initialCursor;
        snapshot = elements;
    }

    //是否遍历结束
    public boolean hasNext() {
        return cursor < snapshot.length;
    }

    //获取元素
    public E next() {
        if (! hasNext())
            throw new NoSuchElementException();
        return (E) snapshot[cursor++];
    }

}
```
   如上代码当调用iterator()方法获取迭代器时候实际是返回一个COWIterator对象，COWIterator的snapshot变量保存了当前list的内容，
cursor是遍历list数据的下标。
   这里为什么说snapshot是list的快照呢？明明是指针传递的引用，而不是拷贝。如果在该线程使用返回的迭代器遍历元素的过程中，
   其他线程没有对list进行增删改，那么snapshot本身就是list的array,因为它们是引用关系。

   但是如果遍历期间，有其他线程对该list进行了增删改，那么snapshot就是快照了，因为增删改后list里面的数组被新数组替换了，
   这时候老数组只有被snapshot索引用，所以这也就说明获取迭代器后，使用改迭代器进行变量元素时候，其它线程对该list进行的增
   删改是不可见的，

   因为它们操作的是两个不同的数组，这也就是弱一致性的达成。