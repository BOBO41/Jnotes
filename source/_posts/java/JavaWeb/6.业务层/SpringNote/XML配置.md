## 控制反转和依赖注入

IOC inversion of control  译作控制反转，是一种设计原则，本质就是，对象创建其依赖对象的操作，不再需要由对象本身来完成，而是由容器来完成。本来是对象自己控制生成其依赖对象的，现在由第三方来控制，因此得名控制反转。 

控制反转有两种实现方式,一种是依赖注入(dependence inject) 另一种是依赖查找(denpendence lookup).

我们无法准确得知为什么会有这两种方式的存在,控制反转更像是一个新旧思想的融合,依赖查找是比较传统的实现方式,而依赖注入,虽然一开始会让人有种难以理解,但事实证明它的灵活性更高,作用更好.

依赖查找方式的控制反转,组件需要具备对依赖的引用,而依赖注入直接由容器把依赖注入到组件中.



## Spring中的控制反转

控制反转是Spring框架很重要的一环,它主要是通过依赖注入实现,虽然依赖查找也有提供.



## Spring中的依赖注入

Spring实施依赖注入的容器的核心是BeanFactory接口.该接口负责管理组件,包括它们的依赖,以及生命周期.

在Spring中,容器管理的组件都叫Bean.

我们常用的ApplicationContext接口就是BeanFactory接口的子接口.

ApllicationContext具有两个实现类.

分别是FileSystemXmlApplicationContext和ClassPathXmlApplicationContext.

**浅显易懂的说法**

BeanFactory和ApplicationContext都是一个容器,用来装Bean的,后者继承前者,比前者更为强大.

一般我们会把Bean的配置,写在一个XML文件上.容器就是根据这XML文件上的内容生产Bean.

FileSystemXmlApplicationContext和ClassPathXmlApplicationContext的作用就是把XML文件传递给容器.

## ClassPathXmlApplication与FileSystemXmlApplicationContext

ClassPathXmlApplicationContext 默认会去 classPath 路径下找。classPath 路径指的就是编译后的 classes 目录。 

如果是Web开发,那classpath是指WEB-INF文件夹下的classes目录。 

```java
    // applicationContext.xml 在Resources
	// WEB-INF/classes/applicationContext.xml
    BeanFactory beanFactory=new ClassPathXmlApplicationContext("applicationContext.xml");
    

    BeanFactory beanFactory = new ClassPathXmlApplicationContext(
        "classpath*:applicationContext.xml");
    
    //多个配置文件
    BeanFactory beanFactory = new ClassPathXmlApplicationContext(
        new String[]{"applicationContext.xml"});
    
    //绝对路径需加“file:”前缀
    BeanFactory beanFactory = new ClassPathXmlApplicationContext(
        "file:E:\spring\src\main\resources\applicationContext.xml");
```

FileSystemXmlApplicationContext 默认是去项目的路径下加载，可以是相对路径，也可以是绝对路径，若是绝对路径，“file:” 前缀可以缺省。 

```java
    // applicationContext.xml在web目录下
	ApplicationContext ctx = new FileSystemXmlApplicationContext(
        "web/ApplicationContext.xml");
	// applicationContext.xml在WEB-INF目录下
	ApplicationContext ctx = new FileSystemXmlApplicationContext(
        "web/WEB-INF/ApplicationContext.xml");	

    //多配置文件
    BeanFactory beanFactory=new FileSystemXmlApplicationContext(
        new String[]{"src\\main\\resources\\applicationContext.xml"});

    //绝对目录
    BeanFactory beanFactory=new FileSystemXmlApplicationContext(
        new String[]{"E:\\spring\\src\\main\\resources\\applicationContext.xml"});
```

### classpath与classpath*

classpath：只会到你的class路径中查找找文件。

classpath*：不仅包含class路径，还包括jar文件中（class路径）进行查找。

同名资源存在时，classpath: 只从第一个符合条件的classpath中加载资源，而classpath*: 会从所有的classpath中加载符合条件的资源.

## Bean的实例化方式

Spring容器对Bean进行实例化,有三种方式.

- 使用类构造器
- 使用静态工厂
- 使用实例工厂

```xml
// 使用类构造器
<bean id="BeanId" class="com.packt.webstore.SpringDemo.Bean"/>
//使用静态工厂
<bean id="BeanId" class="com.packt.webstore.SpringDemo.BeanStaticFactory" 
      factory-method="createBean"></bean>
//使用实例工厂
	//实例化工厂Bean
<bean id="BeanFactory" class="com.packt.webstore.SpringDemo.BeanFactory"/>
	//创建Bean
<bean id="BeanId" factory-bean="BeanFactory" factory-method="createBean"></bean>
```

## Bean的作用域

- singleton  在Spring的IOC容器中,该Bean只能有一个.
- prototype 每次从容器中获取Bean时,都返回一个新的实例.
- request    每次HTTP请求都会创建一个新的Bean
- session    同一个HTTP Session共享一个Bean.
- globalSession 

```java
<bean id="BeanId" class="com.packt.webstore.SpringDemo.Bean" scope="singleton"/>
```



## Bean的属性注入

```xml
<!--常规方法-->
<bean id="BeanId" class="com.packt.webstore.SpringDemo.Bean" scope="singleton">
	<property name="" value""></property>
    <property name="">
        <value></value>
    </property>
    <!--Bean的属性值是另一个Bean-->
    <property name"" ref="beanId"></property>
</bean>
<!--P命令空间-->
<!--前提是在beans中添加xmlns:p="http://ww.springframework.org/schema/p"-->
<bean p:属性名="" P:属性名-ref="" ></bean>
<!--SpEL-->
<property name="" value="表达式"></property>
<!--
常用表达式:
常量: #{10} #{'string'}
Bean: #{BeanId}
Bean属性: #{BeanId.propertyName}
Bean方法: BeanId.methodName()
静态方法:  T(java.lang.Math).PI
运算符支持: #{3 lt 4== 4 gt 3}
正则表达式: #{user.name matches'[a-Z]{6,}'}
集合支持:  #{likes[3]}
-->
<!--
集合注入
<array>
<list>
<set>
<map>
<props>
-->
<bean id="" class="">
    
    <property name="">
    	<array>
            <value>大佬</value>
            <ref>BeanId</ref>
        </array>
    </property>
    
    <property name="">
    	<map>
            <entry key="" value=""></entry>
        </map>
    </property>
    
    <property name="">
    	<props>
            <prop key=""><prop>
        </props>
    </property>
            
</bean>
```

# 前言

基本上无论使用什么框架,配置都是非常重要并且必须掌握的知识。

Spring 框架提供了三种配置方式

- 基于XML的配置方式
- 基于注解的配置方式
- 基于Java代码的配置方式 

基于XML的配置方式,易于观看,但是当配置内容一多,就变得难以维护,而且也不好测试。

Spring官方推荐的是混合使用基于注解和Java代码的配置方式.

Spring配置的作用

- 告诉Spring容器,它需要创建和管理哪些Bean,以及应该为这些Bean设置什么属性和行为。
- 修改Spring容器的行为。

# 基于XML的配置方式

虽然说Spring官方推荐的是混合使用基于注解和Java代码的配置方式,但是个人觉得基于XML的配置方式对新手友好。

**配置文件模板**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

</beans>
```

## XML文档中的xmlns、xmlns:xsi和xsi:schemaLocation

其实他们的作用就是,让你可以引用外部xml文档,由于这些文档中可能会有相同的标签,为了不引发冲突,就为不同的文档添加一个标志,通过使用标志:标签名的方式,正确找到想要使用的标签.

**Spring xml文档样品**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context 
                           http://www.springframework.org/schema/context/spring-context.xsd
                           http://www.springframework.org/schema/mvc
                           http://www.springframework.org/schema/mvc/spring-mvc.xsd">
    
    <context:component-scan base-package="xxx.xxx.controller" />
     
    <context:annotation-config/>
    <mvc:default-servlet-handler/>
    <mvc:annotation-driven/>
     
    <mvc:resources mapping="/images/**" location="/images/" />
     
    <bean id="xxx" class="xxx.xxx.xxx.Xxx">
        <property name="xxx" value="xxxx"/>
    </bean>
</beans>
```

### **什么是XMLNS?**

xmlns其实是XML Namespace的缩写，可译为“XML命名空间” 

### 为什么需要XMLNS?

考虑这样两个XML文档：表示HTML表格元素的`<table/>`： 

```xml
<table>
  <tr>
    <td>Apples</td>
    <td>Bananas</td>
  </tr>
</table>
```

​    和描述一张桌子的`<table/>`：

```xml
<table>
  <name>African Coffee Table</name>
  <width>80</width>
  <length>120</length>
</table>
```

​    假如这两个 XML 文档被一起使用，由于两个文档都包含带有不同内容和定义的 <table> 元素，就会发生命名冲突。XML 解析器是无法确定如何处理这类冲突。为了解决上述问题，xmlns就产生了。

### 如何使用xmlns?

**定义xmlns**
语法： **xmlns:namespace-prefix="namespaceURI"**。
其中namespace-prefix为自定义前缀，只要在这个XML文档中保证前缀不重复即可；
namespaceURI是这个前缀对应的XML Namespace的定义。例如， 

```xml
xmlns="http://www.springframework.org/schema/beans" <!--没有前缀-->
xmlns:context="http://www.springframework.org/schema/context"
```

**使用xmlns**

```xml
<bean></bean> <!--使用的就是没有前缀的那个命名空间-->
<context:component-scan base-package="xxx.xxx.controller" />
```

### xmlns:xsi与 xsi:schemaLocation

xsi:schemaLocation属性其实是Namespace为http://www.w3.org/2001/XMLSchema-instance里的schemaLocation属性.

它定义了XML Namespace和对应的 XSD（Xml Schema Definition）文档的位置的关系。它的值由一个或多个URI引用对组成，两个URI之间以空白符分隔（空格和换行均可）。第一个URI是定义的 XML Namespace的值，第二个URI给出Schema文档的位置，Schema处理器将从这个位置读取Schema文档 

## 创建Bean

Spring容器对Bean进行实例化,有三种方式.

- 使用类构造器
- 使用静态工厂
- 使用实例工厂

```xml
// 使用类构造器
<bean id="BeanId" class="com.example.SpringDemo.Bean"/>
//使用静态工厂
<bean id="BeanId" class="com.example.SpringDemo.BeanStaticFactory" 
      factory-method="createBean"></bean>
//使用实例工厂
	//实例化工厂Bean
<bean id="BeanFactory" class="com.example.SpringDemo.BeanFactory"/>
	//创建Bean
<bean id="BeanId" factory-bean="BeanFactory" factory-method="createBean"/>
```

## Bean的作用域

- singleton  在Spring的IOC容器中,该Bean只能有一个.**(默认)**
- prototype 每次从容器中获取Bean时,都返回一个新的实例.
- request    每次HTTP请求都会创建一个新的Bean
- session    同一个HTTP Session共享一个Bean.
- globalSession 

```xml
<bean id="BeanId" class="com.packt.webstore.SpringDemo.Bean" scope="singleton"/>
```

## Bean的生命周期

### 初始化和销毁

```xml
<bean id="BeanId" class="com.packt.webstore.SpringDemo.Bean" 
      init-method="method" destroy-method="method"/>
```

### BeanPostProcessor

不懂,以后再说吧

## 为Bean注入属性

由于现在不怎么使用XML的配置方式,所以我也不打算详细讲0.0

```xml
<bean id="BeanId" class="com.packt.webstore.SpringDemo.Bean" scope="singleton">
	<property name="" value""></property>
    <property name="">
        <value></value>
    </property>
    <!--Bean的属性值是另一个Bean-->
    <property name"" ref="beanId"></property>
</bean>
```

## 从容器中获取Bean

知道了如何为容器注入Bean,我们就要知道如何从容器获取Bean.

### 获取容器

要从容器里获取Bean,那么先要获取到容器。使用基于XML配置的方式,我们有两种方法获取容器。

- ClassPathXmlApplicationContext
- FileSystemApplicationContext

ClassPathXmlApplicationContext 默认会去 classPath 路径下找。classPath 路径指的就是编译后的 classes 目录。 

如果是Web开发,那classpath是指WEB-INF文件夹下的classes目录。 

```java
    // applicationContext.xml 在Resources
	// WEB-INF/classes/applicationContext.xml
    ApplicationContext ctx =new ClassPathXmlApplicationContext("applicationContext.xml");
    

    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        "classpath*:applicationContext.xml");
    
    //多个配置文件
    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        new String[]{"applicationContext.xml"});
    
    //绝对路径需加“file:”前缀
    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        "file:E:\spring\src\main\resources\applicationContext.xml");
```

FileSystemXmlApplicationContext 默认是去项目的路径下加载，可以是相对路径，也可以是绝对路径，若是绝对路径，“file:” 前缀可以缺省。 

```java
    // applicationContext.xml在web目录下
	ApplicationContext ctx = new FileSystemXmlApplicationContext(
        "web/ApplicationContext.xml");
	// applicationContext.xml在WEB-INF目录下
	ApplicationContext ctx = new FileSystemXmlApplicationContext(
        "web/WEB-INF/ApplicationContext.xml");	

    //多配置文件
    ApplicationContext ctx = new FileSystemXmlApplicationContext(
        new String[]{"src\\main\\resources\\applicationContext.xml"});

    //绝对目录
    ApplicationContext ctx = new FileSystemXmlApplicationContext(
        new String[]{"E:\\spring\\src\\main\\resources\\applicationContext.xml"});
```





