# 配置Spring Boot

## 有关配置的基础知识

### 基于XML的配置方式

### 基于注解的配置方式

### 基于Java代码的配置方法

# 

# 构造你的代码

## 推荐的包结构

**Spring Boot推荐使用传统的包结构，com.example.project.**

## 找到你主应用程序类

**SpringBoot建议把主应用程序类放在包的跟目录，也就是com.example.project.MainApplication.java。**

我们通常会用@SpringBootApplication来修饰主应用程序类，该注解会为某些框架提供基础扫搜路径。

例如，在我们写JPA应用的时候，@SpringBootApplicaiton注解修饰的类的包用于搜索@Entity。

使用根包（root package）还允许让组件扫描仅用于你的项目。

> @SpringBootApplication注解内部使用了@EnableAutoConfiguration和@ComponentScan注解，如果你不想使用@SpringBootApplication，那么你可以用着两个注解代替。

**一个主应用程序类的模板**

```java
package com.example.myapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application{
    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }   
}
```

## 基于Java的配置方式

SpringBoot推荐使用基于Java的配置方式。

### @Configuration

```java
@Target(value=TYPE)
@Retention(value=RUNTIME)
@Documented
@Component
public @interface Configuration
```

@Configuration表示一个类是配置类,它声明了一个或多个@Bean方法,告诉Spring,需要把什么组件添加到容器中。

(容器管理的组件叫做Bean)



### 往容器添加组件的三种方法

- 在配置类使用@Bean修饰方法.
- 包扫描+组件类型的注解
- @Import注解

#### @Bean

```java
@Configuration
public class MyConfig {

    @Bean
    public String hello(){
        return "Hello World";
    }

}
```

#### @包扫描+组件类型的注解

```java
@Configuration
@ComponentScan("com.hdr.spring_demo")
/**
* @ComponentScan 配置组件扫描,指定从哪个包下扫描组件
* 需要与@Configuration一起使用
* 相当于XML配置的 <context:component-scan>
*/
public class MyConfig {

    @Bean
    public String hello(){
        return "Hello World";
    }

}

@Component
/**
* 组件类型的注解:@Controller @Service @Repository  @Component等等
*/
public class MyComponent {

    public void sayHello(){
        System.out.println("Hello I am Component");
    }
}
```

#### @Import注解

@Import注解，它的作用其实就是引入一个或多个类，可以引入普通类，也可以引入配置类。 

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {
    Class<?>[] value();
}
// value可以是Configuration, ImportSelector, ImportBeanDefinitionRegistrar 和普通组件类
```

##### ImportSelector接口

```java
java.lang.String[] selectImports(AnnotationMetadata importingClassMetadata)
    //根据导入的@Configuration类的AnnotationMetadata选择并返回应导入哪个类的名称。
```

##### ImportBeanDefinitionRegistrar接口

```java
void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                             BeanDefinitionRegistry registry)
    //importingClassMetadata 导入类的注释元数据
    //包含bean定义的注册表的接口
```

### @Enable*注解的详解

**Enable 中文意思是启动。**

现在我们通过@EnableWebMvc注解来了解了解。

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({DelegatingWebMvcConfiguration.class})
public @interface EnableWebMvc {
}
```

很明显,这里导入了一个配置类`DelegatingWebMvcConfiguration`

```java
@Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport
```

根据API的描述，它是`WebMvcConfigurationSupport`类的子类,它的功能是检测和调用所有`WebConfigurer`类型的Bean.

允许这些Bean自定义WebMvcConfigurationSupport提供的配置.

- @EnableAspectJAutoProxy  激活Aspect自动代理
  - 对应的XML配置 `<aop:aspectj-autoproxy/>`
- @EnableAsync         开启异步方法的支持。
- @EnableScheduling 开启计划任务的支持。
- @EnableWebMVC   开启Web MVC的配置支持。
- @EnableConfigurationProperties 开启对@ConfigurationProperties注解配置Bean的支持。
- @EnableJpaRepositories   开启对Spring Data JPA Repostory的支持。
- @EnableTransactionManagement  开启注解式事务的支持。
- @EnableCaching   开启注解式的缓存支持

## 添加额外的配置类

正如我们上面提到的，@Import可以帮我们引入额外配置类。另外，我们还可以使用@ComponentScan注解获取所有Spring组件（component）。

## 导入XML配置

我们可以使用@ImportResource注解导入XML配置文件

## 自动配置（Auto-configuration）

Spring Boot会尝试根据你所添加的jar依赖来自动对你的Spring应用进行配置。

例如，导入HSQLDB包后，你不需要配置任何的数据库连接的Bean，Spring Boot自动为你配置一个内存中的数据库。

你需要通过将@EnableAutoConfiguration或@SpringBootApplication注解添加到其中一个@Configuration类来进行自动配置。

## 替换部分默认配置

## 禁用特定的自动配置类

如果你想禁用某个自动配置类，有两种办法

- @EnableAutoConfiguration注解

```java
@Configuration
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
// 如果这个自动配置类不在类路径中，你可以使用excludeName属性，指定一个全限定名。
public class MyConfiguration{}
```

- 在SpringBoot配置文件中修改spring.autoconfigure.exclude属性的值。

