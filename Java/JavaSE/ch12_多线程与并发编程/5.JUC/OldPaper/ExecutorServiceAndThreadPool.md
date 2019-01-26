# Executors框架

前面我们已经学了如何通过继承Thread类或者实现Runnable接口来创建线程。

如果只是创建几个线程还好，如果要创建的线程数量比较多，那么这种不断创建和销毁线程的方法，其实是大大降低对系统资源的利用度的。

为了解决这个问题，JDK为我们提供了Executor框架，一个用来创建和管理线程的框架。

Executor框架具有以下的作用：

-   创建线程：它提供了多种方法来创建线程，具体来说就是使用线程池维护线程，从线程池调用线程执行任务。
-   线程管理：通过线程池来维护线程的生命周期。
-   任务的提交与执行：它不仅提供了方法让你提交任务，还能让你决定任务是马上执行，还是延迟执行，甚至定期执行。

JUC提供了以下三个接口，这三个接口定义所有创建和管理线程所需要的方法。

-   **Executor**：一个只包含`execute（）`方法的接口，通过接收一个`runnable`对象来执行任务。
-   **ExecutorService**：**Executor**的子接口，添加了管理任务生命周期的功能。同时还提供了`submit()`方法可以接收`Runnable`和`Callable`对象。`Callable`与`Runnable`类似，只不过前者可以返回执行后的结果。
-   **ScheduledExecutorService**：**ExecutorService**的子接口，It adds functionality to schedule the execution of the tasks.

除了上面三个接口，JUC还提供了一个`Executors`类，提供了一些工厂方法来创建不同类型的**ExecutorService**。

## ExecutorService Example

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsExample {
    public static void main(String[] args) {
        System.out.println("Inside : " + Thread.currentThread().getName());

        System.out.println("Creating Executor Service...");
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        System.out.println("Creating a Runnable...");
        Runnable runnable = () -> {
            System.out.println("Inside : " + Thread.currentThread().getName());
        };

        System.out.println("Submit the task.");
        executorService.submit(runnable);
      
        System.out.println("Shutting down the executor");
			  executorService.shutdown();
    }
}
```

```java
public class ExecutorsExample {
    public static void main(String[] args) {
        System.out.println("Inside : " + Thread.currentThread().getName());

        System.out.println("Creating Executor Service with a thread pool of Size 2");
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Runnable task1 = () -> {
            System.out.println("Executing Task1 inside : " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
        };

        Runnable task2 = () -> {
            System.out.println("Executing Task2 inside : " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
        };

        Runnable task3 = () -> {
            System.out.println("Executing Task3 inside : " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
        };


        System.out.println("Submitting the tasks for execution...");
        executorService.submit(task1);
        executorService.submit(task2);
        executorService.submit(task3);

        executorService.shutdown();
    }
}
```

## ScheduledExecutorService example

ScheduledExecutorService用来定期或者延期执行任务。

```JAVA
// 延迟执行
public static void main(String[] args) {
   ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
   Runnable task = () -> System.out.println("老婆听电话！");
   executorService.schedule(task, 2, TimeUnit.MINUTES);
}
```

```JAVA
// 定时执行任务
public static void main(String[] args) {
   ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
   Runnable task = () -> System.out.println("老婆听电话！");
   executorService.scheduleAtFixedRate(task, 2, 2, TimeUnit.MINUTES);
}
```

# Callable And Future

在上面的例子，我们都是使用`Runnable`对象来定义任务，这种方法很方便，但是有个缺陷，那就是它不能返回任务执行后的结果。

JDK为我们提供了`Callable`接口用它来定义的任务，可以在执行完毕后返回结果。

```java
Callable<String> task = new Callable<String>() {
   @Override
   public String call() throws Exception {
      return "hello world";
   }
};

Callable<String> task = () -> {
   return "hello world";
};
```

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {

   Callable<String> task = () -> {
      // Perform some computation
      System.out.println("Entered Callable");
      Thread.sleep(2000);
      return "Hello from Callable";
   };

   ExecutorService executorService = Executors.newSingleThreadExecutor();
   Future<String> result = executorService.submit(task);
   // Future.get() blocks until the result is available
   String s = result.get();
   System.out.println(s);
   executorService.shutdown();
}
```

