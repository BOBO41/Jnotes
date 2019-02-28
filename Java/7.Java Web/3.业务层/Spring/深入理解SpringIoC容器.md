### 简介

Spring的IoC容器有两个重要的接口，分别是`BeanFactory`和`ApplicationContext`。

`BeanFactory`是最基础的容器，它规定了容器应该提供的基本服务。

`ApplicationContext`是前者的扩展，它添加很多开发企业级应用需要用到的功能。

为了更好的区分，我把`BeanFactory`称为低级容器，`ApplicationCon`称为高级容器。

本文将从源码级别对这两种容器进行解析。

### IoC与IoC容器

什么是IoC？IoC有什么用？

IoC，inversion of control，控制反转。大部分应用都是由多个类相互合作来实现业务逻辑，也就说一个对象完成它的功能往往都需要依赖其它对象。如果对象获取其依赖的过程，由对象自身来完成，这将导致代码高耦合以及难以测试。

控制反转原则，让我们把依赖对象统一交由容器来管理，然后通过依赖注入，或者依赖查找这样的方式来让对象获取其依赖。

### SpringIoC容器的工作流程

无论是`BeanFactory`还是`ApplicationContext`它们的工作流程都是大致相同的。

-   获取配置文件
-   读取配置文件中定义的Bean，然后转换成BeanDefinition对象
-   在容器上注册BeanDefinition对象（使用HashMap）
-   根据BeanDefinition完成Bean的创建以及依赖注入

### BeanFactory

看一下`BeanFactory`提供的基本服务

```java
public interface BeanFactory {
	 /*
	 	用来区分FactoryBean与它产生的实例
	 	容器中存放两种Bean，一种是直接使用的，另一种则是FactoryBean，该Bean用来生成直接使用的Bean。
	 	例如，如果你注册了一个Bean，叫myJndiObject，然后这个对象又实现了FactoryBean接口
	 	factory.getBean("&myJndiObject")获得的是FactoryBean
	 	factory.getBean("myJndiObject")获得的是FactoryBean产生的Bean
	 */
   String FACTORY_BEAN_PREFIX = "&";
   Object getBean(String name) throws BeansException;
   <T> T getBean(String name, Class<T> requiredType) throws BeansException;
   Object getBean(String name, Object... args) throws BeansException;
   <T> T getBean(Class<T> requiredType) throws BeansException;
   <T> T getBean(Class<T> requiredType, Object... args) throws BeansException;

   <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType);
   <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType);

   boolean containsBean(String name);
   boolean isSingleton(String name) throws NoSuchBeanDefinitionException;
   boolean isPrototype(String name) throws NoSuchBeanDefinitionException;
   boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException;
   boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException;

   @Nullable
   Class<?> getType(String name) throws NoSuchBeanDefinitionException;
   String[] getAliases(String name);

}
```

### DefaultListableBeanFactory

`DefaultListableBeanFactory`是简单容器的一个实现，在它下面本来还有个XmlBeanFactory的，不过在Spring3.1以后就被废弃了。

![DefaultListableBeanFactory](/home/hdr/Desktop/Note/Java/JavaWeb/6.业务层/Spring/图片/DefaultListableBeanFactory.png)

**Spring文档对DefaultListableBeanFactory的描述**

`DefaultListableBeanFactory`是`ConfigurableListableBeanFactory`和`BeanDefinitionRegistry`接口的默认实现。它是一个有毛有翼的Bean工厂。。。。。。

**构造器**

```java
	public DefaultListableBeanFactory() {super();}
  // Create a new DefaultListableBeanFactory with the given parent.
	public DefaultListableBeanFactory(@Nullable BeanFactory parentBeanFactory) {
		super(parentBeanFactory);
	}
// 查看父类的构造器
	public AbstractAutowireCapableBeanFactory() {
		super();
		ignoreDependencyInterface(BeanNameAware.class);
		ignoreDependencyInterface(BeanFactoryAware.class);
		ignoreDependencyInterface(BeanClassLoaderAware.class);
	}
	public AbstractAutowireCapableBeanFactory(@Nullable BeanFactory parentBeanFactory) {
		this();
		setParentBeanFactory(parentBeanFactory);
	}
// 再查看父类的构造器.....
	public AbstractBeanFactory() {
    // ？？？ 一脸懵逼
	}
	public AbstractBeanFactory(@Nullable BeanFactory parentBeanFactory) {
		this.parentBeanFactory = parentBeanFactory;
	}
```

**完蛋。。。。。。应该从XmlFactoryBean开始讲起的，就算它已经被废弃了**

我有点疑问，`XmlFactoryBean`里面容器的初始化用的是`DefaultListableBeanFactory`，然后读取`BeanDefinition`用的是`XmlBeanDefinitionReader`，读取到的`BeanDefinition`是怎么装入容器的？？？

