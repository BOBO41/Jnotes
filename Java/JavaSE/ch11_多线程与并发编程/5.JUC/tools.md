# CountDownLatch

一个同步辅助工具，允许一个或多个线程处于等待直到其它线程中执行的一组操作完成。

构造CountDownLatch的时候需要给定一个计数量（count），每次调用countDown()方法就会减少一次，当计量数为0的时候，唤醒所有等待中的线程。

|           | 方法和描述                                                   |
| --------- | ------------------------------------------------------------ |
| `void`    | `await()`Causes the current thread to wait until the latch has counted down to zero, unless the thread is [interrupted](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#interrupt--). |
| `boolean` | `await(long timeout, TimeUnit unit)`Causes the current thread to wait until the latch has counted down to zero, unless the thread is [interrupted](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#interrupt--), or the specified waiting time elapses. |
| `void`    | `countDown()`Decrements the count of the latch, releasing all waiting threads if the count reaches zero. |
| `long`    | `getCount()`Returns the current count.                       |
| `String`  | `toString()`Returns a string identifying this latch, as well as its state. |

例子

```java
class Driver { // ...
   void main() throws InterruptedException {
     CountDownLatch doneSignal = new CountDownLatch(N);
     Executor e = ...

     for (int i = 0; i < N; ++i) // create and start threads
       e.execute(new WorkerRunnable(doneSignal, i));

     doneSignal.await();           // wait for all to finish
   }
 }

 class WorkerRunnable implements Runnable {
   private final CountDownLatch doneSignal;
   private final int i;
   WorkerRunnable(CountDownLatch doneSignal, int i) {
     this.doneSignal = doneSignal;
     this.i = i;
   }
   public void run() {
     try {
       doWork(i);
       doneSignal.countDown();
     } catch (InterruptedException ex) {} // return;
   }

   void doWork() { ... }
 }
```

# CyclicBarrier

允许一组线程全部等待彼此到达共同的障碍点。

CyclicBarrier支持可选的Runnable命令，该命令在每个障碍点运行一次，在聚会中的最后一个线程到达之后，但在释放任何线程之前。

|           | 方法和描述                                                   |
| --------- | ------------------------------------------------------------ |
| `int`     | `await()`Waits until all [parties](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CyclicBarrier.html#getParties--) have invoked `await` on this barrier. |
| `int`     | `await(long timeout, TimeUnit unit)`Waits until all [parties](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CyclicBarrier.html#getParties--) have invoked `await` on this barrier, or the specified waiting time elapses. |
| `int`     | `getNumberWaiting()`Returns the number of parties currently waiting at the barrier. |
| `int`     | `getParties()`Returns the number of parties required to trip this barrier. |
| `boolean` | `isBroken()`Queries if this barrier is in a broken state.    |
| `void`    | `reset()`Resets the barrier to its initial state.            |

# Semaphore

Semaphore是一种基于计数的信号量。它可以设定一个阈值，基于此，多个线程竞争获取许可信号，做完自己的申请后归还，超过阈值后，线程申请许可信号将会被阻塞。Semaphore可以用来构建一些对象池，资源池之类的，比如数据库连接池，我们也可以创建计数为1的Semaphore，将其作为一种类似互斥锁的机制，这也叫二元信号量，表示两种互斥状态。

```java
 class Pool {
   private static final int MAX_AVAILABLE = 100;
   private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

   public Object getItem() throws InterruptedException {
     available.acquire();
     return getNextAvailableItem();
   }

   public void putItem(Object x) {
     if (markAsUnused(x))
       available.release();
   }

   // Not a particularly efficient data structure; just for demo

   protected Object[] items = ... whatever kinds of items being managed
   protected boolean[] used = new boolean[MAX_AVAILABLE];

   protected synchronized Object getNextAvailableItem() {
     for (int i = 0; i < MAX_AVAILABLE; ++i) {
       if (!used[i]) {
          used[i] = true;
          return items[i];
       }
     }
     return null; // not reached
   }

   protected synchronized boolean markAsUnused(Object item) {
     for (int i = 0; i < MAX_AVAILABLE; ++i) {
       if (item == items[i]) {
          if (used[i]) {
            used[i] = false;
            return true;
          } else
            return false;
       }
     }
     return false;
   }
 }
```

# Executors

此包中定义的Executor，ExecutorService，ScheduledExecutorService，ThreadFactory和Callable类的工厂和实用方法。

该类支持以下几种方法：

-   Methods that create and return an [`ExecutorService`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html) set up with commonly useful configuration settings.
-   Methods that create and return a [`ScheduledExecutorService`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html) set up with commonly useful configuration settings.
-   Methods that create and return a "wrapped" ExecutorService, that disables reconfiguration by making implementation-specific methods inaccessible.
-   Methods that create and return a [`ThreadFactory`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadFactory.html) that sets newly created threads to a known state.
-   Methods that create and return a [`Callable`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Callable.html) out of other closure-like forms, so they can be used in execution methods requiring `Callable`.

# Exchanger

