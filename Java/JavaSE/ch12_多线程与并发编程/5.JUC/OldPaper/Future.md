# Future

在前面的文章中我们讲述了创建线程的2种方式，一种是直接继承Thread，另外一种就是实现Runnable接口。

这2种方式都有一个缺陷就是：在执行完任务之后无法获取执行结果。

如果需要获取执行结果，就必须通过共享变量或者使用线程通信的方式来达到效果，这样使用起来就比较麻烦。

而自从Java 1.5开始，就提供了Callable和Future，通过它们可以在任务执行完毕之后得到任务执行结果。

**Interface**

Future代表一次异步计算的结果，它提供方法来查看计算是否完成、获取计算结果、取消计算。

| 方法与描述                                                |
| --------------------------------------------------------- |
| `cancel(boolean mayInterruptIfRunning)`尝试取消执行该任务 |
| `get()`一直等待直到任务完成                               |
| `get(long timeout, TimeUnit unit)`最多等待指定的时间      |
| `isCancelled()`是否成功取消该任务                         |
| `isDone()`任务是否已完成                                  |

## ScheduledFuture

**Interface**

A delayed result-bearing action that can be cancelled. Usually a scheduled future is the result of scheduling a task with a [`ScheduledExecutorService`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html).

## RunnableFuture

**Interface**

一个Runnable的Future，在成功执行玩run方法后，可以获取其结果。

### FutureTask

Future接口的基本实现。

### RunnableScheduledFuture

A [`ScheduledFuture`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledFuture.html) that is [`Runnable`](https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html). Successful execution of the `run` method causes completion of the `Future` and allows access to its results.