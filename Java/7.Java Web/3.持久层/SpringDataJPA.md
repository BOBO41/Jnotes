## 简介

**什么是Spring Data JPA？**

由于市面上有多种ORM框架，为了方便使用Java制定了一份规范，叫Java Persistence API（JPA），它定义了一系列的接口，用来实现数据存储功能。而Spring Data JPA是对JPA的再一次封装。

**什么是ORM框架？**

基于JDBC的方式操作数据库需要编写大量的模板代码，特别是把结果装入到对象的时候，这个过程更加烦人。ORM框架就是对JDBC的封装，方便我们实现数据操作，例如执行SQL语句、执行结果的封装等等，都由框架为我们完成。

## 配置

```java
@Configuration
@ComponentScan("com.hdr.learn.jpa")
@EnableJpaRepositories(basePackages = {"com.hdr.learn.jpa.repository"})
public class ApplicationConfig {
   
   // 配置数据源
   @Bean
   public DataSource dataSource() {
      ComboPooledDataSource dataSource = new ComboPooledDataSource();
      dataSource.setUser("root");
      dataSource.setPassword("qwer");
      try {
         dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
      } catch (PropertyVetoException e) {
         e.printStackTrace();
      }
      dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/jpa?useUnicodeEncoding=true&characterSet=utf8mb4");
      return dataSource;
   }

   // 配置EntityManagerFactory
   @Bean
   public EntityManagerFactory entityManagerFactory() {
      HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
      vendorAdapter.setGenerateDdl(false);
      vendorAdapter.setDatabase(Database.MYSQL);
      vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
      vendorAdapter.setShowSql(true);

      LocalContainerEntityManagerFactoryBean emFactoryBean = new LocalContainerEntityManagerFactoryBean();
      emFactoryBean.setPackagesToScan("com.hdr.learn.jpa.entity");
      emFactoryBean.setDataSource(dataSource());
      emFactoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
      emFactoryBean.afterPropertiesSet();
      return emFactoryBean.getObject();
   }
   
}
```

## 核心概念

#### Repository

**Repository**是Spring Data Repositories中最核心的接口。仓库就是用来存储实体对象。

```java
@Indexed
public interface Repository<T, ID> {}
// T 实体对象的类型  ID 该实体对象用作标记的类型
```

#### CrudRepository

Repository是没有定义任何方法的，Spring Data Repositories提供了一个**CrudRepository**，它是Repository的子接口，它定义了一些常用操作数据库的方法。

```java
public interface CrudRepository<T, ID extends Serializable>
  extends Repository<T, ID> {

  <S extends T> S save(S entity);	// 存储或更新实体

  Optional<T> findById(ID primaryKey);  // 根据ID查找实体

  Iterable<T> findAll();               // 查找素有实体

  long count();                        // 统计实体数量

  void delete(T entity);               // 删除实体

  boolean existsById(ID primaryKey);   // 判断实体是否存在

  // ......
}
```

#### PagingAndSortingRepository

CrudRepository之后还有一个**PagingAndSortingRepository**，它添加了分页以及排序的功能。

```java
public interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {

   Iterable<T> findAll(Sort sort);

   Page<T> findAll(Pageable pageable);
}
```

## 定义仓库

一般来说，你可以定义一个与实体对象对应的仓库，这个仓库一般会继承Repository或CrudRepository或PagingAndSortingRepository。如果不想继承上述接口，你可以使用`@RepositoryDefinition`修饰仓库，该注解等同于继承Repository。

#### 对仓库的行为进行微调

如果你只想要接口的部分内容，你可以继承Repository然后把想要的内容直接复制即可。

```java
@NoRepositoryBean
interface MyBaseRepository<T, ID extends Serializable> extends Repository<T, ID> {

  Optional<T> findById(ID id);

  <S extends T> S save(S entity);
}

interface UserRepository extends MyBaseRepository<User, Long> {
  User findByEmailAddress(EmailAddress emailAddress);
}
```

#### 可以处理null值的仓库

- [`@NonNullApi`](https://docs.spring.io/spring/docs/5.1.7.RELEASE/javadoc-api/org/springframework/lang/NonNullApi.html): Used on the package level to declare that the default behavior for parameters and return values is to not accept or produce `null` values.
- [`@NonNull`](https://docs.spring.io/spring/docs/5.1.7.RELEASE/javadoc-api/org/springframework/lang/NonNull.html): Used on a parameter or return value that must not be `null` (not needed on a parameter and return value where `@NonNullApi` applies).
- [`@Nullable`](https://docs.spring.io/spring/docs/5.1.7.RELEASE/javadoc-api/org/springframework/lang/Nullable.html): Used on a parameter or return value that can be `null`.

```java
@org.springframework.lang.NonNullApi
package com.hdr;
//----------------------------------------------------------------------------------------
package com.acme;                                                       

import org.springframework.lang.Nullable;

interface UserRepository extends Repository<User, Long> {
              
  @Nullable
  User findByEmailAddress(@Nullable EmailAddress emailAdress);          
}
```

#### 多个模块使用仓库

有些时候我们需要使用多个Spring Data模块，例如Spring Data Redis、Spring Data JPA。当Spring Data检测到存在多个仓库工厂时，Spring Data会进入仓库严格配置模式。也就是Spring Data会根据仓库信息或者实体对象来判断究竟使用哪个Spring Data模块。

**根据仓库信息判断Spring Data模块**

```java
// 这里用的是与Spring Data JPA模块相关的接口，在多Spring Data 模块存在下，一样能使用
interface MyRepository extends JpaRepository<User, Long> { }

@NoRepositoryBean
interface MyBaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
  …
}

interface UserRepository extends MyBaseRepository<User, Long> {
  …
}

// 这里用的是通用接口，在多Spring Data 模块存在下，就无法判断究竟用的是哪个模块
interface AmbiguousRepository extends Repository<User, Long> {
 …
}

@NoRepositoryBean
interface MyBaseRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
  …
}

interface AmbiguousUserRepository extends MyBaseRepository<User, Long> {
  …
}
```

**根据实体对象判断Spring Data模块**

```java
// Person用的是Entity修饰，所以它应该使用Spring Data JPA
interface PersonRepository extends Repository<Person, Long> {
 …
}

@Entity
class Person {
  …
}

// User用Document注解修饰，所以它应该使用Spring Data MongoDB
interface UserRepository extends Repository<User, Long> {
 …
}

@Document
class User {
  …
}

// 使用多种技术的实体对象
interface JpaPersonRepository extends Repository<Person, Long> {
 …
}

interface MongoDBPersonRepository extends Repository<Person, Long> {
 …
}

@Entity
@Document
class Person {
  …
}
```

**设置仓库位置判断Spring Data模块**

```java
@EnableJpaRepositories(basePackages = "com.acme.repositories.jpa")
@EnableMongoRepositories(basePackages = "com.acme.repositories.mongo")
interface Configuration { }
```

## 持久化和更新实体对象

我们可以调用`CrudRepository.save(…)`方法实现实体的持久化和更新。如果该实体还没被持久化，那么底层会调用`entityManager.persist(…)`或者底层会调用`entityManager.merge(…)`。

Spring Data JPA遵循以下策略判断一个实体是否是新的：

- 判断实体Id的值，如果为null，则这是一个新的实体
- 如果实体对象实现了`Persistable`，则会调用它的`isNew`方法判断是否是新对象
- Implementing `EntityInformation`: You can customize the `EntityInformation` abstraction used in the `SimpleJpaRepository` implementation by creating a subclass of `JpaRepositoryFactory` and overriding the `getEntityInformation(…)` method accordingly. You then have to register the custom implementation of `JpaRepositoryFactory` as a Spring bean. Note that this should be rarely necessary. See the [JavaDoc](https://docs.spring.io/spring-data/data-jpa/docs/current/api/index.html?org/springframework/data/jpa/repository/support/JpaRepositoryFactory.html) for details.

## 查询

Spring Data JPA提供了两种查询方式，第一种是根据方法名生成对应查询，另一种是使用`@Query`注解。

### 查询步骤

使用Spring Data JPA查询数据主要分为四个步骤。

1. 配置Spring Data JPA
2. 定义一个仓库（继续Repository或其子接口）
3. 在该仓库中声明方法
4. 获取仓库实例，调用仓库方法

```java
// 使用方法名
public interface UserRepository extends Repository<User, Long> {
  List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);
}
// @NamedQuery 或 @NamedNativeQuery
@Entity
@NamedQuery(name = "User.findByEmailAddress",
  query = "select u from User u where u.emailAddress = ?1")
public class User {
}
public interface UserRepository extends JpaRepository<User, Long> {
  List<User> findByLastname(String lastname);
  User findByEmailAddress(String emailAddress);
}
// @Query
public interface UserRepository extends JpaRepository<User, Long> {
  @Query("select u from User u where u.emailAddress = ?1")
  User findByEmailAddress(String emailAddress);
}
// nativeQuery
public interface UserRepository extends JpaRepository<User, Long> {
  @Query(value = "SELECT * FROM USERS WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)
  User findByEmailAddress(String emailAddress);
}
// 更新和删除
@Modifying
@Query("update User u set u.firstname = ?1 where u.lastname = ?2")
int setFixedFirstnameFor(String firstname, String lastname);

@Modifying
@Query("delete from User u where user.role.id = ?1")
void deleteInBulkByRoleId(long roleId);
```

### 排序与分页



### 动态查询

### 一对多



### 多对多



## 用聚合根发布事件

仓库所管理的实体叫做聚合根。在领域驱动设计的应用中，聚合根通常都会发布事件。Spring Data 提供了一个注解`@DomainEvents`，方便我们使用聚合根发布事件。

```java
class AnAggregateRoot {

    @DomainEvents 
    Collection<Object> domainEvents() {
        // … return events you want to get published here
    }

    @AfterDomainEventPublication 
    void callbackMethod() {
       // … potentially clean up domain events list
    }
}
```

The method using `@DomainEvents` can return either a single event instance or a collection of events. It must not take any arguments.

After all events have been published, we have a method annotated with `@AfterDomainEventPublication`. It can be used to potentially clean the list of events to be published (among other uses).

