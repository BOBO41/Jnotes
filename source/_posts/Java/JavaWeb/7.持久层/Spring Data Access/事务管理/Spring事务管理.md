---
title : 1.事务管理
categories : 
- JavaWeb
- Spring
- Data Access
date : 2018-12-5

---

# 

# Spring Transaction Management

## 事务

事务：为完成某件事情所做的一组操作。

事务管理：在执行事务的过程中出错，则撤销该事务所做过的操作，避免脏数据的产生。

事务的四种特性：

- 原子性

    事务是一组操作组成的基本单元，事务中操作的执行要么全部成功，要么全部失败，不会结束在中间某个环节。事务在执行过程中发生错误，会被回滚到事务开始前的状态，就像这个事务从来没有执行过一样。

- 一致性

    一个事务执行之前和执行之后数据库都必须处于一致性状态。如果事务成功地完成，那么系统中所有变化将正确地应用，系统处于有效状态。如果在事务中出现错误，那么系统中的所有变化将自动地回滚，系统返回到原始状态。

- 隔离性

    指的是在并发环境中，当不同的事务同时操纵相同的数据时，每个事务都有各自的完整数据空间。由并发事务所做的修改必须与任何其他并发事务所做的修改隔离。事务查看数据更新时，数据所处的状态要么是另一事务修改它之前的状态，要么是另一事务修改它之后的状态，事务不会查看到中间状态的数据。

- 持久性

    指的是只要事务成功结束，它对数据库所做的更新就必须永久保存下来。即使发生系统崩溃，重新启动数据库系统后，数据库还能恢复到事务成功结束时的状态。

## Spring事务管理的核心接口

Spring事务管理的三个重要接口：

- `PlatformTransactionManager`
- `TransactionDefinition`
- `TransactionStatus`

### PlatformTransactionManager

Spring并不直接管理事务，而是提供了多种事务管理器，他们将事务管理的职责委托给Hibernate或者JTA等持久化机制所提供的相关平台框架的事务来实现。 Spring事务管理器的接口是`PlatformTransactionManager`，通过这个接口，Spring为各个平台如JDBC、Hibernate等都提供了对应的事务管理器，但是具体的实现就是各个平台自己的事情了。

```java
public interface PlatformTransactionManager {
    TransactionStatus getTransaction(@Nullable TransactionDefinition definition) 
        throws TransactionException;
    void commit(TransactionStatus status) throws TransactionException;
    void rollback(TransactionStatus status) throws TransactionException;
}
```

### TransactionDefinition

`TransactionDefinition`类定义了一些基本的事务属性。

事务属性：

- 传播行为

    当事务方法被另一个事务方法调用时，必须指定事务应该如何传播。例如：方法可能继续在现有事务中运行，也可能开启一个新事务，并在自己的事务中运行。

    ```
    PROPAGATION_REQUIRED        支持当前事务，如果不存在 就新建一个
    PROPAGATION_SUPPORTS        支持当前事务，如果事务不存在，就不使用事务
    PROPAGATION_MANDATORY       支持当前事务，如果不存在，则抛出异常
    PROPAGATION_REQUIRES_NEW    如果有事务存在，挂起当前事务，创建一个新的事务
    PROPAGATION_NOT_SUPPORTED   以非事务的方式运行，如果有事务存在，挂起当前事务
    PROPAGATION_NEVER 			以非事务的方式运行，如果有事务存在，抛出异常
    PROPAGATION_NESTED          如果当前事务存在，则嵌套事务执行
    ```

- 隔离级别

    - 脏读、不可重复读、幻象读概念说明：
        - 脏读：指当一个事务正字访问数据，并且对数据进行了修改，而这种数据还没有提交到数据库中，这时，另外一个事务也访问这个数据，然后使用了这个数据。因为这个数据还没有提交那么另外一个事务读取到的这个数据我们称之为脏数据。依据脏数据所做的操作肯能是不正确的。
        - 不可重复读：指在一个事务内，多次读同一数据。在这个事务还没有执行结束的时候，另外一个事务也访问该同一数据，那么在第一个事务中的两次读取数据之间，由于第二个事务的修改第一个事务两次读到的数据可能是不一样的，这样就发生了在一个事物内两次连续读到的数据是不一样的，这种情况被称为是不可重复读。
        - 幻读：一个事务先后读取同一个范围的记录，但两次读取的记录数不同，我们称之为幻读（两次执行同一条 select 语句会出现不同的结果，第二次读会增加一数据行，并没有说这两次执行是在同一个事务中）
    - read uncommited：是最低的事务隔离级别，它允许另外一个事务可以看到这个事务未提交的数据。
    - read commited：保证一个事物提交后才能被另外一个事务读取。另外一个事务不能读取该事物未提交的数据。
    - repeatable read：这种事务隔离级别可以防止脏读，不可重复读。但是可能会出现幻象读。它除了保证一个事务不能被另外一个事务读取未提交的数据之外还避免了以下情况产生（不可重复读）。
    - serializable：这是花费最高代价但最可靠的事务隔离级别。事务被处理为顺序执行。除了防止脏读，不可重复读之外，还避免了幻象读
    - - 

- 回滚规则

- 事务超时

- 是否只读？

### TransactionStatus

## 编程式和声明式事务管理

Spring将事务管理分为了两类：

- 编程式事务管理（很少用）

- 声明式事务管理

    - 基于`TransactionProxyFactoryBean`的方式（很少用）

    - 基于注解的方式（常用）

    - 基于`AspectJ`的方式（常用）


### 基于注解的事务管理

1. 用`@EnableTransactionManagement`修饰`@Configuration`类
2. 用`@Transactional`修饰`DAO`类

#### @Transactional

只能用于接口、接口中的方法、类、类里面的公共方法。如果要对非公共方法进行事务管理，请考虑使用`AspectJ`。

Spring推荐修饰类、类里面的公共方法而不是接口和接口方法，因为当你使用基于类的代理方式，例如cglib，或者是使用AspectJ的时候，接口中的事务管理注解会失效。



