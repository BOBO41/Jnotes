# 事务管理

## 事务

数据库事务(Database Transaction) ，是指作为单个逻辑工作单元执行的一系列操作，要么完全地执行，要么完全地不执行。

事务：用于完成某项任务的一组操作。

事务管理：在执行事务的过程中出错，则撤销该事务所做过的操作，避免脏数据的产生。

Spring全面支持事务管理。

## Spring的事务管理的优点

传统上，Java EE开发人员有两种事务管理的方法：全局或本地，这两种方式都有很大的局限性。在接下来的两节中将对全局和本地事务管理进行了解，然后讨论Spring Framework的事务管理支持如何解决全局和本地事务模型的局限性。

### Global Transactions

全局事务使您可以使用多个事务资源，通常是关系数据库和消息队列。应用程序服务器通过JTA管理全局事务，这是一个繁琐的API。此外，JTA UserTransaction通常需要从JNDI获取，这意味着您还需要使用JNDI才能使用JTA。全局事务的使用限制了应用程序代码的任何潜在重用，因为JTA通常仅在应用程序服务器环境中可用。 

以前，使用全局事务的首选方法是通过EJB CMT（容器管理事务）。 CMT是一种声明式事务管理（与程序化事务管理不同）。 EJB CMT消除了与事务相关的JNDI查找的需要，尽管使用EJB本身需要使用JNDI。它消除了编写Java代码以控制事务的大部分但不是全部的需要。重要的缺点是CMT与JTA和应用服务器环境相关联。此外，仅当选择在EJB中（或至少在事务EJB外观之后）实现业务逻辑时，它才可用。一般来说，EJB的负面影响是如此之大，以至于这不是一个有吸引力的主张，特别是在面对声明式事务管理的令人信服的替代方案时。

### Local Transactions

本地事务是特定于资源的，例如与JDBC连接关联的事务。本地事务可能更容易使用，但具有明显的缺点：它们无法跨多个事务资源工作。例如，使用JDBC连接管理事务的代码无法在全局JTA事务中运行。由于应用程序服务器不参与事务管理，因此无法确保跨多个资源的正确性。 （值得注意的是，大多数应用程序使用单个事务资源。）另一个缺点是本地事务对编程模型是侵入性的。

### Spring的事务编程模型

Spring解决了全局和本地事务的缺点。它允许应用程序开发人员在任何环境中使用一致的编程模型，只需编写一次代码，它可以从不同环境中的不同事务管理策略中受益。 Spring Framework提供了声明式和编程式事务管理。大多数用户更喜欢声明式事务管理。

## Spring的事务抽象

Spring事务抽象的关键是事务策略的概念。事务策略定义在`PlatformTransactionManager`接口

```java
public interface PlatformTransactionManager {

    TransactionStatus getTransaction(
        TransactionDefinition definition) throws TransactionException;

    void commit(TransactionStatus status) throws TransactionException;

    void rollback(TransactionStatus status) throws TransactionException;
}
```

TransactionStatus接口为事务代码提供了一种控制事务执行和查询事务状态的简单方法。

```java
public interface TransactionStatus extends SavepointManager {
    boolean isNewTransaction();
    boolean hasSavepoint();
    void setRollbackOnly();
    boolean isRollbackOnly();
    void flush();
    boolean isCompleted();
}
```

以下示例显示了如何定义本地PlatformTransactionManager实现

```xml
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" 
      destroy-method="close">
    <property name="driverClassName" value="${jdbc.driverClassName}" />
    <property name="url" value="${jdbc.url}" />
    <property name="username" value="${jdbc.username}" />
    <property name="password" value="${jdbc.password}" />
</bean>
```

```xml
<bean id="txManager" 
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>
```

您还可以轻松使用Hibernate本地事务，如以下示例所示。在这种情况下，您需要定义一个Hibernate的LocalSessionFactoryBean，您的应用程序代码可以使用它来获取Hibernate Session实例。

```java
<bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="mappingResources">
        <list>
            <value>org/springframework/samples/petclinic/hibernate/petclinic.hbm.xml</value>
        </list>
    </property>
    <property name="hibernateProperties">
        <value>
            hibernate.dialect=${hibernate.dialect}
        </value>
    </property>
</bean>

<bean id="txManager" class="org.springframework.orm.hibernate5.HibernateTransactionManager">
    <property name="sessionFactory" ref="sessionFactory"/>
</bean>
```

**JTA EJB 都是些什么鬼**

## 同步资源与事务

如何创建不同的事务管理器以及它们如何链接到需要与事务同步的相关资源（例如DataSourceTransactionManager到JDBC DataSource，HibernateTransactionManager到Hibernate SessionFactory等等）现在应该清楚了。

本节描述应用程序代码（直接或间接使用诸如JDBC，Hibernate或JPA之类的持久性API）如何确保正确创建，重用和清理这些资源。本节还讨论了如何（可选）通过相关的PlatformTransactionManager触发事务同步。

### 高级同步方法

### 低级同步方法

### TransactionAwareDataSourceProxy

你应该几乎不需要或想要使用这个类

## 声明式事务管理

Sprng的声明式事务管理与EJB CMT类似，它们之间的区别有

- EJB CMT与JTA耦合，而Spring的声明式事务管理可以在任何环境下运行
- 任何类都可以使用Spring的声明式事务管理
- Spring框架提供声明式的回滚规则
- 可以使用APO自定义事务的行为
- Spring Framework不支持跨远程调用传播事务上下文

### 声明式事务管理的实现

跟AOP密切相关

![](https://docs.spring.io/spring/docs/5.1.2.RELEASE/spring-framework-reference/images/tx.png)

### 例子

# 事实证明，我对于事务、数据库还很差，这里的很多东西都无法理解