### LockSupport类探究
+ > JDK 中的 rt.jar 里面的 LockSupport 是个工具类，主要作用是挂起和唤醒线程，它是创建锁和其它同步类的基础。
LockSupport 类与每个使用它的线程都会关联一个许可证,默认调用 LockSupport 类的方法的线程是不持有许可证的，LockSupport 内部
使用 Unsafe 类实现

