|                  简介                  |           配置           |                         映射器                         |               运行原理               |           实用场景           |
| :------------------------------------: | :----------------------: | :----------------------------------------------------: | :----------------------------------: | :--------------------------: |
| [:sun_with_face:](#sun_with_face-简介) | [:scroll:](#scroll-配置) | [:globe_with_meridians:](#globe_with_meridians-映射器) | [:microscope:](#microscope-运行原理) | [:surfer:](#​surfer-实用场景) |

## :sun_with_face:简介

[返回头部](#sun_with_face-简介)

## :scroll:配置

MyBatis的配置文件对整个MyBatis系统具有深远的影响,接下来我们详细了解MyBatis的配置.

下面是MyBatis XML配置文件的层次结构

**注意：元素的定义的顺序不能颠倒**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<properties></properties> <!--属性-->
    <settings></settings> <!--设置-->
    <typeAliases></typeAliases> <!--命名类型-->
    <typeHandler></typeHandler> <!--类型处理器-->
    <objectFactory></objectFactory> <!--对象工厂-->
    <plugins></plugins> <!--插件-->
    <environments> 
        <environment>
            <transactionManager></transactionManager>
            <dataSource></dataSource>
        </environment>
    </environments>
    <databaseIdProvider></databaseIdProvider> <!--数据库厂商标识-->
    <mappers></mappers> <!--映射器-->
</configuration>
```



## Properties元素

Properties元素有两个作用：获取外部属性文件的内容、定义属性配置

配置属性后，就可以在配置文件中替换需要动态配置的属性值。 

### property子元素

```xml
<properties>
  <property name="username" value="hdr"/>     <!--自定义属性配置-->
  <property name="password" value="admin"/>     <!--自定义属性配置-->
</properties>
```

### properties配置文件

```xml
<properties resource="config.properties">     <!---通过resource属性引用外部文件-->
</properties>
```

### 程序传递参数



### 优先级

propertie子元素的内容先背读取,然后再读取resource属性引入的properties文件,最后才是读取作为方法参数传递的属性.

如果由相同的属性,后者会覆盖前者.

## Settings元素

Settings的配置内容如下表所示,配置不需要修改太多,未来我们还会再接触,这里有点印象即可.

| 设置参数                  | 描述                                           | 有效值            | 默认值  |
| ------------------------- | ---------------------------------------------- | ----------------- | ------- |
| cacheEnabled              | 映射器中缓存配置的全局开关                     | true / false      | true    |
| lazyLoadingEnabled        | 对象延迟加载的全局开关,可通过fetchType属性覆盖 | true / false      | false   |
| aggressiveLazyLoding      |                                                | true / false      | true    |
| multipleResultSetsEnabled | 是否允许单一语句返回多结果集(需要兼容驱动)     | true / false      | true    |
| useColumnLabel            | 使用标签代替列名                               | true / false      | true    |
| useGeneratedKeys          | 允许JDBC支持自动生成主键                       | true / false      | true    |
| autoMappingBehavior       | 指定MyBatis如何自动映射列到字段(属性)          | NONE PRATIAL FULL | PARTIAL |
| defaultExecutorType       |                                                |                   |         |
| defaultStatementTimeout   |                                                |                   |         |
| safeRowBoundsEnabled      | 允许在嵌套语句中使用分页                       |                   |         |
| mapUnderscoreToCamelCase  | 开启驼峰命名规则映射                           | true / false      | false   |
| localCacheScope           |                                                |                   |         |
| jdbcTypeForNull           |                                                |                   |         |
| lazyLoadTriggerMethods    |                                                |                   |         |
| defaultScriptingLanguage  |                                                |                   |         |
| callSettersOnNulls        |                                                |                   |         |
| logPrefix                 |                                                |                   |         |
| logImpl                   |                                                |                   |         |
| proxyFactory              |                                                |                   |         |

## typeAliases

类型别名是为 Java 类型设置一个短的名字。它只和 XML 配置有关，存在的意义仅在于用来减少类完全限定名的冗余。 

### 定义typeAliases

```xml
<typeAliases>
  <typeAlias alias="User" type="com.hdr.mybatisDemo.domain.User"/> 
  <!---这时候User代表com.hdr.mybatisDemo.domain.User-->
  <package name="com.hdr.mybatisDemo.domain"/>
  <!--指定一个包名，MyBatis 会在包名下面搜索需要的 Java Bean-->
</typeAliases>
```

**注意：**

指定包中Java Bean，如果没有注解则使用 Bean 的首字母小写的非限定类名来作为它的别名。 

 比如 com.hdr.mybatisDemo.beans.User 的别名为 `user`；

若有注解，则别名为其注解值。看下面的例子： 

```java
@Alias("User")
public class Author {
    ...
}
```

MyBatis默认实现了一些类型别名，具体请看 [别名](http://www.mybatis.org/mybatis-3/zh/configuration.html#typeAliases)。

### 使用typeAliases

```xml
<update id="update" parameterType="User">
    update tb_user
    set
    username = #{userName} ,
    password = #{password},
    age=#{age}
    where username=#{userName}
</update>
```

------

MyBatis通过对JDBC进行抽象，从而大大简化我们对持久层的操作。

下面我们通过一个例子来看看这个过程是怎么实现的。

```xml
<insert id="insertStudent" parameterType="Student">
	INSERT INTO STUDENTS(STUD_ID,NAME,EMAIL,DOB)
	VALUES(#{id},#{name},#{email},#{dob})
</insert>
```

1.  MyBatis会创建一个PreparedStatement接口如下

    ```java
    PreparedStatement pstmt = connection.prepareStatement
    ("INSERT INTO STUDENTS(STUD_ID,NAME,EMAIL,DOB) VALUES(?,?,?,?)");
    ```

2.  检查传递过来的属性(studentId name email)的数据类型，然后调用相应的setXXX（）方法

    ```java
    pstmt.setInt(1,student.getId());
    pstmt.setString(2, student.getName());
    pstmt.setString(3, student.getEmail());
    pstmt.setTimestamp(4, new Timestamp((student.getDob()).getTime()));
    ```

不错吧~ MyBatis真的能让我们少写点代码呢~

看了上面的例子，你可能会有点疑问，所有的数据类型都能被处理吗？

不是的，MyBatis内部定义了一些类型处理器，可以处理byte[], java.util.Date, java.sql.Date, java.sql.Time, java.
sql.Timestamp, java enums,等等。如果要处理其它类型，那么就需要你自己实现一个Type handler。

### 重写或自定义type handler

重写或自定义类型处理器有两种方法：

-   实现 `org.apache.ibatis.type.TypeHandler` 接口
-   继承 `org.apache.ibatis.type.BaseTypeHandler`类， 然后可以选择性地将它映射到一个 JDBC 类型。 

**假设我们现在处于这种状态**

```java
public class Student
{
    private Integer id;
    private String name;
    private String email;
    private PhoneNumber phone;  
    // Setters and getters
}
```

```java
public class PhoneNumber
{
    private String countryCode;
    private String stateCode;
    private String number;
    
    public PhoneNumber() {}
    
    public PhoneNumber(String countryCode, String stateCode, String number) {
        this.countryCode = countryCode;
        this.stateCode = stateCode;
        this.number = number;
    }
    public PhoneNumber(String string) {
        if(string != null){
        	String[] parts = string.split("-");
            if(parts.length>0) this.countryCode=parts[0];
            if(parts.length>1) this.stateCode=parts[1];
            if(parts.length>2) this.number=parts[2];
        }
	}
    public String getAsString() {
		return countryCode+"-"+stateCode+"-"+number;
    }
    // setters and getters
}
```

很明显，Student类型中的phone字段是一种自定义的类型，要存储该类型，我们就要为她实现type handler

```java
// ExampleTypeHandler.java
/*
  MyBatis提供了两种途径，让我们指定被关联的 JDBC 类型
  ① 在配置文件typeHandler元素上增加一个jdbcType属性（比如：jdbcType="VARCHAR"）；
  ② 给TypeHandler类增加一个 @MappedJdbcTypes 注解来指定与其关联的JDBC类型列表。
    如果同时使用，前者会覆盖后者。
*/
@MappedJdbcTypes(JdbcType.VARCHAR)
/*
 根据泛型，MyBatis 可以得知该类型处理器处理的 Java 类型。
 MyBatis还另外提供了两种做法，可以让我们实现相同的效果。
 ① 在配置文件typeHandler元素上增加一个 javaType 属性（比如：javaType="String"）；
 ② 给TypeHandler类增加一个 @MappedTypes 注解来指定与其关联的 Java 类型列表。
   如果同时使用，前者会覆盖后者。
*/
public class PhoneTypeHandler extends BaseTypeHandler<PhoneNumber>{
    
    @Override
    public void setNonNullParameter(PreparedStatement ps , int i , 
                                    PhoneNumber parameter ,JdbcType jdbcType) 
    throws SQLException{
        ps.setString(i,parameter.getAsString());
    }
    
    @Override
    public PhoneNumber getNullableResult(Result rs, String columnName) 
    throws SQLException{
        return new PhoneNumber(rs.getString(columnName));
    }
    @Override
	public PhoneNumber getNullableResult(ResultSet rs, int columnIndex)
    throws SQLException {
		return new PhoneNumber(rs.getString(columnIndex));
	}
    @Override
    public PhoneNumber getNullableResult(CallableStatement cs, int columnIndex)
    throws SQLException{
    	return new PhoneNumber(cs.getString(columnIndex));
    }
    
}
```

**注册type handler**

```xml
<!-- mybatis-config.xml -->
<typeHandlers>
  <typeHandler handler="com.hdr.mybatisDemo.typeHandler.PhoneTypeHandler"/>
  <package name="com.hdr.mybatisDemo.typeHandler"/>
  <!--指定一个包名，MyBatis 会在包名下面搜索需要的类型处理器-->
</typeHandlers>
```

**补充：使用一个type handler 处理多个自定义java类**

```java
//GenericTypeHandler.java
public class GenericTypeHandler<E extends MyObject> extends BaseTypeHandler<E> {
/*
  为了使用泛型类型处理器， 需要增加一个接受该类的class作为参数的构造器，
  这样在构造一个类型处理器的时候 MyBatis 就会传入一个具体的类。
*/
  private Class<E> type;

  public GenericTypeHandler(Class<E> type) {
    if (type == null) throw new IllegalArgumentException("Type argument cannot be null");
    this.type = type;
  }
```

------

### 枚举类型typeHandler

MyBatis内部提供了两个typeHandler给我们处理枚举类

-   org.apache.ibatis.EnumTypeHandler            使用枚举名称作为参数传递
-   org.apache.ibatis.EnumOrinalTypeHandler   使用整数下标作为参数传递

如果上面两个typeHandler不能满足我们的需求，我们还能自定义typeHandler

```java
public enum Gender {
   MALE(1, "男"), FEMALE(2, "女");
   private int id;
   private String string;

   private Gender(int id, String string) {
      this.id = id;
      this.string = string;
   }

   public static Gender getGender(int id) {
      if (id == 1) {return MALE;} 
      else if (id == 2) {return FEMALE;} 
      else {return null;}
   }
    
    // getter setter
}
```

#### EnumOrinalTypeHandler

**注册typeHandler**

```xml
<typeHandler handler="org.apache.ibatis.type.EnumOrdinalTypeHandler" 
             javaType="com.hdr.learn.mybatis.enums.Gender"/>
```

**使用typeHandler**

```xml
<result column="gender" property="gender" 
        typeHandler="org.apache.ibatis.type.EnumOrdinalTypeHandler" />
```

#### EnumTypeHandler 

```xml
<result column="gender" property="gender" 
        typeHandler="org.apache.ibatis.type.EnumTypeHandler" />
```

#### 自定义TypeHandler

参考上面的自定义typeHandler

## ObejctFactory

当MyBatis在构建一个结果返回的时候，会使用ObjectFactory去构建POJO。

一般来说，我们使用MyBatis提供的ObjectFactory即可，但是我们也能自定义ObjectFactory

## plugins

插件很强大，但是也很复杂，在没有很好了解MyBatis的运行原理之前，我们还无法讨论它。

## environments

environments元素下可以编写一到多个environment元素。（注意有s和没有s）

一个environment元素可以粗略看做一个数据库。

```xml
<environments default="development">
	<environment id="development">
     	<transactionManager type="JDBC"/>
     	<dataSource type="POOLED">
        	<property name="driver" value="com.mysql.jdbc.Driver"/>
            <property name="url" value="jdbc:mysql://127.0.0.1:3306/mybatis3"/>
            <property name="username" value="root"/>
            <property name="password" value="admin"/>
        </dataSource>
    </environment>
    
</environments>
```

### default="shoppingcart"

你可以看到environments元素有个default的属性，这是干嘛用的呢？

其实，在我们build一个SqlSessionFactory的时候，是要指定使用哪一个environment元素中的配置信息。

```java
SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader,"development");

```

但是我们之前例子并没有传递这个值的呀，不也是可以吗？

```java
SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
```

这是因为default属性在暗中帮助呢

default属性的作用就是，设置默认使用哪一个environment元素的配置信息建立SqlSessionFactory对象。

### transactionManager 与 dataSource

我们可以看到，environment元素具有两个子元素，分别是transactionManager 与 dataSource。

#### transactionManager元素 

MyBatis支持两种类型的事务管理器，分别是JDBC和MANAGED。

**什么是事务管理?**

事务管理是对于一系列数据库操作进行管理，一个事务包含一个或多个[SQL](https://baike.baidu.com/item/SQL)语句，是逻辑管理的工作单元 。事务中的SQL语句，如果有任何一条出现异常，那么回滚之前的所有操作。这样可以防止出现脏数据，防止数据库数据出现问题。

-   JDBC – 这个配置就是直接使用了 JDBC 的提交和回滚设置，它依赖于从数据源得到的连接来管理事务作用域。
-   MANAGED – 这个配置几乎没做什么。它从来不提交或回滚一个连接，而是让容器来管理事务的整个生命周期。     默认情况下它会关闭连接，然而一些容器并不希望这样，因此需要将 closeConnection 属性设置为 false 来阻止它默认的关闭行为。

**补充**：

1.  在JDBC中是通过Connection对象进行事务管理的，默认是自动提交事务，可以手工将自动提交关闭，关闭后需要通过commit方法进行提交，rollback方法进行回滚，如果不提交，则数据不会真正的插入到数据库中。
2.  如果你正在使用 Spring + MyBatis，则没有必要配置事务管理器， 因为 Spring 模块会使用自带的管理器来覆盖前面的配置。 

#### dataSource

dataSource元素是用来配置连接数据库的属性的。

```xml
<dataSource type="JNDI">
	<property name="data_source" value="java:comp/jdbc/ShoppingcartDS"/>
</dataSource>
```

dataSource元素的type属性具有三个值，分别是UNPOOLED, POOLED, JNDI。

UNPOOLED- 对于每次数据库操作，MyBatis会打开一个新的session执行数据库操作，操作完成后关闭session。

POOLED- MyBatis会创建一个数据库连接池，要对数据库进行操作就从这个池里获取连接对象。（一般用于研发和测试）

JNDI- MyBatis通过JDNI获取连接，生产环境中最好使用它。

补充：JNDI API 允许java软件客户端，通过名称发现和查找数据和资源。

## Mappers元素

Mappers元素是用来对Mapper XML文件进行定位的。

```xml
<mappers>
    <!-- 使用相对于类路径的资源引用 -->
  <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
    <!-- 使用完全限定资源定位符（URL） -->
  <mapper url="file:///var/mappers/BlogMapper.xml"/>
    <!-- 使用映射器接口实现类的完全限定类名 -->
  <mapper class="org.mybatis.builder.PostMapper"/>
    <!-- 将包内的映射器接口实现全部注册为映射器 -->
  <package name="org.mybatis.builder"/>
</mappers>
```

[返回头部](#sun_with_face-简介)

## :globe_with_meridians:映射器

## 3.1映射器的主要元素

-   select
-   insert
-   update
-   delete
-   sql  （允许自定义一部分的SQL然后在各个地方引用）
-   resultMap
-   cache
-   cache-ref

## 3.2CRUD

### 1.Select元素

#### Select元素的属性

| 属性          | 说明                                            | 备注       |
| ------------- | ----------------------------------------------- | ---------- |
| id            | 对应的方法名称                                  |            |
| parameterType | 参数类型，可以使类的全命令，也可以使用别名      |            |
| resultType    | 结果类型                                        |            |
| resultMap     | 结果映射                                        |            |
| flushCache    | 调用SQL后，是否清空之前查询的本地缓存和二级缓存 | 默认 false |
| useCache      | 是否启动二级缓存，存储该结果                    | 默认 true  |
| timeout       |                                                 |            |
| fetchSize     | 设定获取记录的总条数                            |            |
| statementType |                                                 |            |
| resultSetType |                                                 |            |
| databaseId    |                                                 |            |
| resultOrdered |                                                 |            |
| resultSets    |                                                 |            |

#### 传递多个参数

#### 使用Map传递参数

可读性差

#### 使用注解的方式传递参数

```java
List<Role> findRoleByAnnotation(@Param("roleName")String roleName,
                                @Param("note")String note);
```

当参数个数一多，写起来也是难受

#### 使用JavaBean传递参数

### 2.insert元素

#### insert元素的属性

| 属性名称         | 描述 | 备注 |
| ---------------- | ---- | ---- |
| id               |      |      |
| parameterType    |      |      |
| flushCache       |      |      |
| timeout          |      |      |
| statementType    |      |      |
| useGeneratedKeys |      |      |
| keyProperty      |      |      |
| keyColumn        |      |      |
| databaseI        |      |      |
| lang             |      |      |

## 3.sql元素

通过sql元素，我们可以写sql语句，然后在其他地方使用。

```xml
<sql id="role_columns">
	id,role_name,note
</sql>

<select id="getRole" parameterType="long" resultMap="roleMap">
	select <include refid="role_columns"> from t_role where id = #{id};
</select>
```



## 4.ResultType

ResultType不是元素,它只是select元素中的一个属性.

其作用就是说明,查询结果应该映射到哪种Java类型上.

### 使用集合类型的resultType

#### 单个集合

```xml
<select id="selectById" parameterType="int" resultType="map">
    <!--resultType中的map是java.util.HashMap的别名-->
	select * from tb_user where id = #{id};
</select>
```

```java
public interface UserRepository {
    public Map<String,Object> selectById(int id);
}
```

```java
UserRepository userRepository = sqlSession.getMapper("UserRepository.xml");

Map<String,Object> userMap = userRepository.selectById(id);
for(String key:userMap.keySet()){
     System.out.println(key+": "+userMap.get(key));
}
```

#### 嵌套集合

```xml
<select id="selectAllUser" resultType="map">
	select * from tb_user;
</select>
<!--最后的返回结果类型List<Map<String,Object>>-->
```

```java
public interface UserRepository {
    public List<Map<String,Object>> selectAllUser();
}
```

------

## 5.Result Maps元素

ResultMaps其实就是ResultType的升级版,都是用来把select语句返回的结果映射到JavaBean的属性上。

 ResultMap 的设计思想是，简单的语句不需要明确的结果映射，而复杂一点的语句只需要描述它们的关系就行了。 

当JavaBean属性名与数据表中列明不相同时,使用resultType就会有点麻烦,如下

```xml
<typeAlias type="com.someapp.model.User" alias="User"/>
<select id="selectUsers" resultType="User">
  select
  user_id             as "id",
  user_name           as "userName",
  hashed_password     as "hashedPassword"
  from tb_user
  where id = #{id}
</select>
```

**使用Result Map**

```xml
<!--结果映射 将数据库返回结果的数据与java数据对应-->
<resultMap id="userResultMap" type="User">
  <!--id元素跟result元素相似,但它是用来映射标识符属性(标识符属性:用来比较对象是否相等的属性)-->
  <id property="id" column="user_id" />
  <!--result元素用来把结果中列的值映射到JavaBean的属性上-->
  <result property="username" column="user_name"/>
  <result property="password" column="hashed_password"/>
</resultMap>

<select id="selectUsers" resultMap="userResultMap">
  select user_id, user_name, hashed_password
  from some_table
  where id = #{id}
</select>
```

resultMap是非常强大的,下面让我们看看它其它作用.



### ResultMap的继承

```xml
<resultMap type="Student" id="StudentResult">
    <id property="studId" column="stud_id"/>
    <result property="name" column="name"/>
    <result property="email" column="email"/>
    <result property="phone" column="phone"/>
</resultMap>

<resultMap type="Student" id="StudentWithAddressResult" extends="StudentResult">
    <result property="address.addrId" column="addr_id"/>
    <result property="address.street" column="street"/>
    <result property="address.city" column="city"/>
    <result property="address.state" column="state"/>
    <result property="address.zip" column="zip"/>
    <result property="address.country" column="country"/>
</resultMap>
```

**最佳实践：使用框架的缺点是有时候它们看上去像黑盒子(无论源代码是否可见)。 为了确保你实现的行为和想要的一致，最好的选择是编写单元测试。** 

### 一对一关系的映射处理

在我们的领域对象中,每个学生都具有一个地址,着就是一对一关系.

```java
public class Address
{
    private Integer addrId;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;
    // setters & getters
}
```

```java
public class Student
{
    private Integer studId;
    private String name;
    private String email;
    private PhoneNumber phone;
    private Address address;
    //setters & getters
}
```

有三种方法来处理一对一关系.

#### 直接处理法

```xml
<resultMap type="Student" id="StudentWithAddressResult">
    <id property="studId" column="stud_id"/>
    <result property="name" column="name"/>
    <result property="email" column="email"/>
    <result property="phone" column="phone"/>
    <result property="address.addrId" column="addr_id"/>
    <result property="address.street" column="street"/>
    <result property="address.city" column="city"/>
    <result property="address.state" column="state"/>
    <result property="address.zip" column="zip"/>
    <result property="address.country" column="country"/>
</resultMap>
```

```xml
<select id="selectStudentWithAddress" parameterType="int" resultMap="StudentWithAddressResult">
    SELECT STUD_ID, NAME, EMAIL, A.ADDR_ID, STREET, CITY, STATE,ZIP, COUNTRY
    FROM STUDENTS S LEFT OUTER JOIN ADDRESSES A ON <!--这里其实是SQL语句知识-->
    S.ADDR_ID=A.ADDR_ID
    WHERE STUD_ID=#{studId}
</select>
```

这种方法的缺点是,如果我们只需要用到address的信息,则需要另外写一个address的resultMap.

#### 使用嵌套ResultMap

上面的例子向我们展示一种处理一对一表的方法,接下来我们介绍另一种实现方法.

```xml
<resultMap type="Address" id="AddressResult">
    <id property="addrId" column="addr_id"/>
    <result property="street" column="street"/>
    <result property="city" column="city"/>
    <result property="state" column="state"/>
    <result property="zip" column="zip"/>
    <result property="country" column="country"/>
</resultMap>

<resultMap type="Student" id="StudentWithAddressResult">
    <id property="studId" column="stud_id"/>
    <result property="name" column="name"/>
    <result property="email" column="email"/>
    <association property="address" resultMap="AddressResult"/>
    <!--association元素可以引用在同一个XML文件中定义的resultMap元素-->
</resultMap>

```

```xml
<select id="findStudentWithAddress" parameterType="int" 
        resultMap="StudentWithAddressResult">
    SELECT STUD_ID, NAME, EMAIL, A.ADDR_ID, STREET, CITY, STATE,ZIP, COUNTRY
    FROM STUDENTS S LEFT OUTER JOIN ADDRESSES A ON
    S.ADDR_ID=A.ADDR_ID
    WHERE STUD_ID=#{studId}
</select>
```

你也可以直接resultMap元素中,使用association元素配置结果的映射

```xml
<resultMap type="Student" id="StudentWithAddressResult">
    <id property="studId" column="stud_id"/>
    <result property="name" column="name"/>
    <result property="email" column="email"/>
    <association property="address" javaType="Address">
        <id property="addrId" column="addr_id"/>
        <result property="street" column="street"/>
        <result property="city" column="city"/>
        <result property="state" column="state"/>
        <result property="zip" column="zip"/>
        <result property="country" column="country"/>
    </association>
</resultMap>
```

#### 使用嵌套的select语句

通过使用嵌套的select语句,我们可以获取student以及address的内容.

```xml
<select id="findAddressById" parameterType="int"
    resultMap="AddressResult">
    SELECT * FROM ADDRESSES WHERE ADDR_ID=#{id}
</select>

<resultMap type="Student" id="StudentWithAddressResult">
    <id property="studId" column="stud_id"/>
    <result property="name" column="name"/>
    <result property="email" column="email"/>
    <association property="address" column="addr_id" select="findAddressById"/>
    <!--其实也是使用association元素而已-->
</resultMap>

<select id="findStudentWithAddress" parameterType="int" resultMap="StudentWithAddressResult">
    SELECT * FROM STUDENTS WHERE STUD_ID=#{Id}
</select>

```

在本例子中,该嵌套select语句,会在数据库中执行两次查询操作.

### 一对多关系的映射处理

在我们的领域对象中,每个导师可以教一到多门课程,这就是一对多关系.

我们可以使用collection元素,处理一对多关系的结果.

```java
public class Course
{
    private Integer courseId;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private Integer tutorId;
    //setters & getters
}
```

```java
public class Tutor
{
    private Integer tutorId;
    private String name;
    private String email;
    private Address address;
    private List<Course> courses;
    /setters & getters
}
```

有两种方法处理一对多关系

#### 使用嵌套ResultMap

```xml
<resultMap type="Course" id="CourseResult">
    <id column="course_id" property="courseId"/>
    <result column="name" property="name"/>
    <result column="description" property="description"/>
    <result column="start_date" property="startDate"/>
    <result column="end_date" property="endDate"/>
</resultMap>

<resultMap type="Tutor" id="TutorResult">
    <id column="tutor_id" property="tutorId"/>
    <result column="tutor_name" property="name"/>
    <result column="email" property="email"/>
    <collection property="courses" resultMap="CourseResult"/>
</resultMap>

<select id="findTutorById" parameterType="int" resultMap="TutorResult">
    SELECT T.TUTOR_ID, T.NAME AS TUTOR_NAME, EMAIL, C.COURSE_ID,
    C.NAME, DESCRIPTION, START_DATE, END_DATE
    FROM TUTORS T LEFT OUTER JOIN ADDRESSES A ON T.ADDR_ID=A.ADDR_ID
    LEFT OUTER JOIN COURSES C ON T.TUTOR_ID=C.TUTOR_ID
    WHERE T.TUTOR_ID=#{tutorId}
</select>
```

#### 使用嵌套select语句

```xml
<resultMap type="Course" id="CourseResult">
    <id column="course_id" property="courseId"/>
    <result column="name" property="name"/>
    <result column="description" property="description"/>
    <result column="start_date" property="startDate"/>
    <result column="end_date" property="endDate"/>
</resultMap>

<select id="findCoursesByTutor" parameterType="int" resultMap="CourseResult">
    SELECT * FROM COURSES WHERE TUTOR_ID=#{tutorId}
</select>

<resultMap type="Tutor" id="TutorResult">
    <id column="tutor_id" property="tutorId"/>
    <result column="tutor_name" property="name"/>
    <result column="email" property="email"/>
    <collection property="courses" column="tutor_id" select="findCoursesByTutor"/>
</resultMap>

<select id="findTutorById" parameterType="int" resultMap="TutorResult">
    SELECT T.TUTOR_ID, T.NAME AS TUTOR_NAME, EMAIL
    FROM TUTORS T WHERE T.TUTOR_ID=#{tutorId}
</select>
```



------

## 6.缓存

**一级缓存**

MyBatis支持缓存功能，在默认情况下，它只开启一级缓存。（一级缓存只是相对于同一个SqlSession而言）

在第一次查询的后，MyBatis会把查询的结果缓存起来，如果之后并没有发生更新、插入、删除等操作，而且缓存也没有过期，对于接下来相同的查询，MyBatis会返回缓存中的结果。

**二级缓存**

由于一级缓存SqlSession层面的，而且SqlSession又是相互隔离的，为了解决这个问题，我们就需要开启二级缓存。

开启二级缓存,只需要在你的 SQL 映射文件中添加一行:

```xml
<cache/>
```

这个简单语句的效果如下:

-   映射语句文件中的所有 select 语句将会被缓存。
-   映射语句文件中的所有 insert,update 和 delete 语句会刷新缓存。
-   缓存会使用 Least Recently Used(LRU,最近最少使用的)算法来收回。
-   根据时间表(比如 no Flush Interval,没有刷新间隔), 缓存不会以任何时间顺序 来刷新。
-   缓存会存储列表集合或对象(无论查询方法返回什么)的 1024 个引用。
-   缓存会被视为是 read/write(可读/可写)的缓存,意味着对象检索不是共享的,而 且可以安全地被调用者修改,而不干扰其他调用者或线程所做的潜在修改。

上面的这些属性都可以通过缓存元素的属性来修改，例如:

```xml
<cache
  eviction="FIFO"
  flushInterval="60000"
  size="512"
  readOnly="true"/>
```

这里定义了一个 FIFO,并每隔 60 秒刷新,存数结果对象或列表的 512 个引用,而且返回的对象被认为是只读的缓存配置。

## 7.动态SQL

MyBatis的动态SQL包括以下几种元素

| 元素                      | 作用                        | 备注                    |
| ------------------------- | --------------------------- | ----------------------- |
| if                        | 判断语句                    |                         |
| choose（when、otherwise） | 相当于Java中的case when语句 |                         |
| trim（where、set）        | 辅助元素                    | 用于处理一些SQL拼接问题 |
| foreach                   | 循环语句                    |                         |

### 7.1if

```xml
<select id="searchCourses" parameterType="hashmap" resultMap="CourseResult">
    <![CDATA[
        SELECT * FROM COURSES
        WHERE TUTOR_ID= #{tutorId}
        <if test="courseName != null">
        	AND NAME LIKE #{courseName}
        </if>
        <if test="startDate != null">
        	AND START_DATE >= #{startDate}
        </if>
        <if test="endDate != null">
        	AND END_DATE <= #{endDate}
        </if>
    ]]>
</select>
<!--
	所有 XML 文档中的文本均会被解析器解析。
	只有 CDATA 区段（CDATA section）中的文本会被解析器忽略。
	主要是解决特殊字符的问题，例如 < & 等等
	< &lt;
	> &gt;
	&amp; &
	&apos; '
	&quot; "
-->
```

### 7.2choose  when  otherwise 

```xml
<select id="searchCourses" parameterType="hashmap" resultMap="CourseResult">
    SELECT * FROM COURSES
    <choose>
        <when test="searchBy == 'Tutor'">WHERE TUTOR_ID= #{tutorId}</when>
        <when test="searchBy == 'CourseName'">WHERE name like #{courseName}</when>
        <otherwise>WHERE TUTOR start_date &gt;= now()</otherwise>
    </choose>
</select>
<!--类似与switch语句-->
```

### 7.3where元素

```xml
<select id="searchCourses" parameterType="hashmap" resultMap="CourseResult">
    SELECT * FROM COURSES
    <where>
        <if test=" tutorId != null ">
            TUTOR_ID= #{tutorId}
        </if>
        <if test="courseName != null">
            AND name like #{courseName} <!--如果where后面是AND或者OR,则会除去AND/OR-->
        </if>
        <if test="startDate != null">
            AND start_date &gt;= #{startDate}
        </if>
        <if test="endDate != null">
            AND end_date &lt;= #{endDate}
        </if>
    </where>
</select>
```

### 7.4trim元素

trim元素类似于where元素,它更加灵活.

```xml
<select id="searchCourses" parameterType="hashmap" resultMap="CourseResult">
    SELECT * FROM COURSES
    <trim prefix="WHERE" prefixOverrides="AND | OR">
        <!--
			1.在语句开头添加WHERE关键字
			2.如果语句的开头是AND 或者 OR 则改写成WHERE
		-->
        <if test=" tutorId != null ">
            TUTOR_ID= #{tutorId}
        </if>
        <if test="courseName != null">
            AND name like #{courseName}
        </if>
    </trim>
</select>
```

### 7.5set元素

```xml
<update id="updateStudent" parameterType="Student">
    update students
    <set>
        <if test="name != null">name=#{name},</if>
        <if test="email != null">email=#{email},</if>
        <if test="phone != null">phone=#{phone},</if>
    </set>
    where stud_id=#{id}
</update>
```

### 7.6foreach元素

```xml
<select id="searchCoursesByTutors" parameterType="map" resultMap="CourseResult">
    SELECT * FROM COURSES
    <if test="tutorIds != null">
        <where>
            <foreach item="tutorId" collection="tutorIds">
                OR tutor_id=#{tutorId}
            </foreach>
        </where>
    </if>
</select>
```

[返回头部](#sun_with_face-简介)

# :microscope:运行原理



[返回头部](#sun_with_face-简介)

# :surfer:实用场景