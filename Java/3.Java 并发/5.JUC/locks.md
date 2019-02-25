# locks



我们知道，synchronized 是Java的关键字，是Java的内置特性，在JVM层面实现了对临界资源的同步互斥访问，但 synchronized 在处理实际问题时存在诸多局限性，比如响应中断等。Lock 提供了比 synchronized更广泛的锁操作，它能以更优雅的方式处理线程同步问题。

lock是控制多线程获取共享资源的工具，只有在获取到锁的情况下，才能对共享资源进行操作，而在同一个时间只能有一个线程获取到锁。当然也有些锁是运行并发获取共享资源的，例如ReadWriteLock中的读锁。

每个对象都藏有一个监视器锁（monitor lock），synchronized关键字就是用来获取这个锁的，synchronized要求对锁的占用和释放都必须在代码块或方法块中进行。这就导致synchronized用起来简单，但是它不够灵活，在某些情景下，我们需要更加灵活的锁。例如用于遍历并发访问的数据结构的一些算法需要使用“手动”或“链锁”：获取节点A的锁定，然后获取节点B，然后释放A并获取C，然后释放B并获取D等等。Lock接口的实现类允许在不同的作用域中获取和释放锁，还允许以任何的顺序获取或释放多个锁。

由于它允许你在不同的作用域使用锁，因此你需要手动的对锁进行释放。

## Lock

```java
public interface Lock {
    void lock();
    void lockInterruptibly() throws InterruptedException;
    boolean tryLock();
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    void unlock();
    Condition newCondition();
}
```

在Lock中声明了四个方法来获取锁，下面来讲讲这四个方法有何区别。

-   lock()

    用来获取锁。如果锁已被其他线程获取，则一直等待。

-   tryLock()tryLock()

    尝试获取锁，如果获取成功，则返回true，如果获取失败（即锁已被其他线程获取），则返回false，也就说这个方法无论如何都会立即返回。在拿不到锁时不会一直在那等待。

-   tryLock(long time, TimeUnit unit)

    和tryLock()方法是类似的，只不过区别在于这个方法在拿不到锁时会等待一定的时间，在时间期限之内如果还拿不到锁，就返回false。如果如果一开始拿到锁或者在等待期间内拿到了锁，则返回true。

-   lockInterruptibly()

    获取锁，unless the current thread is [interrupted](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#interrupt--).

### ReentrantLock

ReentrantLock是目前JUC中唯一实现了Lock接口的类，并且ReentrantLock提供了更多的方法。下面通过一些实例看具体看一下如何使用ReentrantLock。

### ReentrantReadWriteLock.ReadLock

The lock returned by method ReentrantReadWriteLock.readLock().

### ReentrantReadWriteLock.WriteLock



## Condition

Condition接口可以让线程暂停执行，直到给定的条件（Condition）为真。

Lock用来代替synchronized，Condition用来代替Object的监控方法（wait, notify and notifyAll）。

|           | 方法和描述                                                   |
| --------- | ------------------------------------------------------------ |
| `void`    | `await()`线程进入等待状态直到接收到信号或者被打断            |
| `boolean` | `await(long time, TimeUnit unit)`Causes the current thread to wait until it is signalled or interrupted, or the specified waiting time elapses. |
| `long`    | `awaitNanos(long nanosTimeout)`Causes the current thread to wait until it is signalled or interrupted, or the specified waiting time elapses. |
| `void`    | `awaitUninterruptibly()`线程进入等待状态直到接收到信号       |
| `boolean` | `awaitUntil(Date deadline)`Causes the current thread to wait until it is signalled or interrupted, or the specified deadline elapses. |
| `void`    | `signal()`Wakes up one waiting thread.                       |
| `void`    | `signalAll()`Wakes up all waiting threads.                   |

例子

```java
 class BoundedBuffer {
   final Lock lock = new ReentrantLock();
   final Condition notFull  = lock.newCondition(); 
   final Condition notEmpty = lock.newCondition(); 

   final Object[] items = new Object[100];
   int putptr, takeptr, count;

   public void put(Object x) throws InterruptedException {
     lock.lock();
     try {
       while (count == items.length)
         notFull.await();
       items[putptr] = x;
       if (++putptr == items.length) putptr = 0;
       ++count;
       notEmpty.signal();
     } finally {
       lock.unlock();
     }
   }

   public Object take() throws InterruptedException {
     lock.lock();
     try {
       while (count == 0)
         notEmpty.await();
       Object x = items[takeptr];
       if (++takeptr == items.length) takeptr = 0;
       --count;
       notFull.signal();
       return x;
     } finally {
       lock.unlock();
     }
   }
 }
```



## ReadWriteLock

ReadWriteLock维护一对关联的锁，一个用于只读操作，另一个用于写入。只要没有写入，读锁定可以由多个读取器线程同时保持。写锁是独占的。

|        | 方法和描述                                      |
| ------ | ----------------------------------------------- |
| `Lock` | `readLock()`Returns the lock used for reading.  |
| `Lock` | `writeLock()`Returns the lock used for writing. |

### ReentrantReadWriteLock

ReadWriteLock的实现，支持与ReentrantLock类似的语义。

```java
 class RWDictionary {
   private final Map<String, Data> m = new TreeMap<String, Data>();
   private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
   private final Lock r = rwl.readLock();
   private final Lock w = rwl.writeLock();

   public Data get(String key) {
     r.lock();
     try { return m.get(key); }
     finally { r.unlock(); }
   }
   public String[] allKeys() {
     r.lock();
     try { return m.keySet().toArray(); }
     finally { r.unlock(); }
   }
   public Data put(String key, Data value) {
     w.lock();
     try { return m.put(key, value); }
     finally { w.unlock(); }
   }
   public void clear() {
     w.lock();
     try { m.clear(); }
     finally { w.unlock(); }
   }
 }
```

## LockSupport

### concurrent包的基础

Doug Lea 的神作concurrent包是基于AQS (AbstractQueuedSynchronizer)框架，AQS框架借助于两个类：Unsafe(提供CAS操作)和LockSupport(提供park/unpark操作)。因此，LockSupport可谓构建concurrent包的基础之一。理解concurrent包，就从这里开始。

### 两个重点

-   **操作对象**

归根结底，LockSupport调用的Unsafe中的native代码： 

```
public native void unpark(Thread jthread); 
public native void park(boolean isAbsolute, long time); 
```

两个函数声明清楚地说明了操作对象：park函数是将当前Thread阻塞，而unpark函数则是将另一个Thread唤醒。

与Object类的wait/notify机制相比，park/unpark有两个优点：1. 以thread为操作对象更符合阻塞线程的直观定义；2. 操作更精准，可以准确地唤醒某一个线程（notify随机唤醒一个线程，notifyAll唤醒所有等待的线程），增加了灵活性。

-   **关于许可**

在上面的文字中，我使用了阻塞和唤醒，是为了和wait/notify做对比。其实park/unpark的设计原理核心是“许可”。park是等待一个许可。unpark是为某线程提供一个许可。如果某线程A调用park，那么除非另外一个线程调用unpark(A)给A一个许可，否则线程A将阻塞在park操作上。

有一点比较难理解的，是unpark操作可以再park操作之前。也就是说，先提供许可。当某线程调用park时，已经有许可了，它就消费这个许可，然后可以继续运行。这其实是必须的。考虑最简单的生产者(Producer)消费者(Consumer)模型：Consumer需要消费一个资源，于是调用park操作等待；Producer则生产资源，然后调用unpark给予Consumer使用的许可。非常有可能的一种情况是，Producer先生产，这时候Consumer可能还没有构造好（比如线程还没启动，或者还没切换到该线程）。那么等Consumer准备好要消费时，显然这时候资源已经生产好了，可以直接用，那么park操作当然可以直接运行下去。如果没有这个语义，那将非常难以操作。

-   **其它细节** 
    理解了以上两点，我觉得应该把握了关键，其它细节就不是那么关键，也容易理解了，不作分析。



## Lock和synchronized的选择

总结来说，Lock和synchronized有以下几点不同：

　　1）Lock是一个接口，而synchronized是Java中的关键字，synchronized是内置的语言实现；

　　2）synchronized在发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象发生；而Lock在发生异常时，如果没有主动通过unLock()去释放锁，则很可能造成死锁现象，因此使用Lock时需要在finally块中释放锁；

　　3）Lock可以让等待锁的线程响应中断，而synchronized却不行，使用synchronized时，等待的线程会一直等待下去，不能够响应中断；

　　4）通过Lock可以知道有没有成功获取锁，而synchronized却无法办到。

　　5）Lock可以提高多个线程进行读操作的效率。

　　在性能上来说，如果竞争资源不激烈，两者的性能是差不多的，而当竞争资源非常激烈时（即有大量线程同时竞争），此时Lock的性能要远远优于synchronized。所以说，在具体使用时要根据适当情况选择。

