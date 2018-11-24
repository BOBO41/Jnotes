---
title : SpringMVC架构
categories : 
- JavaWeb
- SpringMVC
date : 2018-8-01
---

# Spring MVC架构

Spring MVC也是根据前端控制器模式而设计的。

通过一个中央Servlet,DispatcherServlet,接受请求,然后根据请求信息调用相应的组件处理请求，再由DispatcherServlet把响应发送给请求方。

## 处理请求的组件

**DispatcherServlet委派的用来处理请求的组件有**

- HandlerMapping
  - 根据当前请求的找到对应的 Handler，并将 Handler与一堆 HandlerInterceptor（拦截器）封装到 HandlerExecutionChain 对象中。
  - Handler 有可能是一个 HandlerMethod（封装了 Controller 中的方法）对象，也有可能是一个 Controller 
    对象、 HttpRequestHandler 对象或 Servlet 对象，而这个 Handler 具体是什么对象，也是与所使用的 
    HandlerMapping 实现类有关。
- HandlerAdapter
  - 根据 Handler 来找到支持它的 HandlerAdapter，通过 HandlerAdapter 执行这个 Handler 得到 ModelAndView 对象。
- HandlerExceptionResolver
  - 异常的解决方案，可能将它们映射到处理程序，HTML错误视图或其他目标。 
- ViewResolver
  - 将从handler返回的基于字符串的视图名称解析为用于呈现给响应的实际View。
- LocaleResolver, LocaleContextResolver
  - 解析客户端正在使用的区域设置以及可能的时区，以便能够提供国际化服务。
- ThemeResolver
  - 解决Web应用程序可以使用的主题 - 例如，提供个性化布局。
- MultipartResolver
  - 在一些multipart解析库的帮助下，解析multipart请求（例如，浏览器表单文件上载）的抽象。
- FlashMapManager
  - 存储和检索“输入”和“输出”FlashMap，可用于将属性从一个请求传递到另一个请求，通常是用作重定向。

本章讲述

- dispatcher servlet 和 request mapping （请求映射）
- web application context （网络应用上下文） 和 configuration (配置)
- SpringMVC 请求流程 和 Web MVC模式
- web application architecutre （网络应用架构）

## Dispatcher servlet

在第一章中，我们简单的介绍了什么是dispatcher servlet 以及如何在web.xml文件中声明它。

所有的请求都将由dispatcher servlet进行统一处理。

dispatcher servlet 根据映射关系调用相应的方法来处理请求。

**dispatcher servlet 的主要任务就是根据请求路径调用相应的处理器方法（controller method）**

### 请求映射的例子

1. 创建一个处理器
2. 声明处理器中的方法处理哪个请求路径

```java
@Controller
public class HomeController {
    @RequestMapping("/")
    public String welcome(Model model){
        model.addAttribute("greeting","Welcome to Web Store");
        model.addAttribute("tagline","The one and only amazing web store");
        return "welcome";
    }
}
```

## Web application context

在Spring框架中，被容器管理的对象就叫 Spring-managed beans，而容器则被称作 application context；

application context（应用上下文）创建beans，组织beans，派出beans来处理请求。

Web application context 是 application context的扩展，它一般包含与前端有关的beans，例如views 和 view resolvers，它被用来与标准的servlet context一起工作。

### Web application context 配置文件

在前面的例子中，我们接触了一个叫 *DefaultServlet-servlet.xml* 的文件，它就是一个Web application context 配置文件，现在我们就来讲讲它。

#### 配置文件的命名与位置

如果你是一个有个性的人，在敲我们第一个例子的时候，你可能会采用自己的命名，而不是 *DefaultServlet-servlet.xml*   然后你就会发现你的程序跑不动了，只有命名为 *DefaultServlet-servlet.xml*  才能跑得动。这是为什么呢？

正如我们前面所说， *DefaultServlet-servlet.xml*  是一个配置文件，配置文件起作用的前提是它能被找到，如果它不能被找到，那么使用它的配置更是无从谈起，那么要怎么做才能让配置文件被找到？

在默认情况下，dispatcher servlet会在WEB-INF目录下，查找XXX-servlet.xml这种命名方式的文件。XXX就是你在web.xml文件中引入DispatcherServlet时，设置的servlet-name。

当然，有些时候，我们真的需要把配置文件放在其他目录下，或者我们真的不喜欢这样的命名，那要怎么办呢？

```xml
<servlet>
        <servlet-name>DefaultServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param> <!--添加这个元素-->
            <param-name>contextConfigLocation</param-name>
            <param-value>/WEB-INF/configuration/MyConfiguration.xml</param-value>
        </init-param>
    </servlet>
```

#### 配置文件的内容

来来看看我们之前写的 *DefaultServlet-servlet.xml* 。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
	<!---->
    <!--告诉Spring MVC 我们需要用到它提供的注解-->
    <mvc:annotation-driven/>
    <!--告诉Spring MVC 大概在哪里会找到使用了@Controller注解的Java类-->
    <context:component-scan base-package="com.packt.webstore"/>

    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

</beans>
```

## View resolvers （视图解析器）

View resolvers 帮助 dispatcher servlet识别出哪一个views需要被渲染，然后返回给发起请求的用户。

Spring MVC框架提供了 View resolvers 的实现类，例如上面用到的InternalResourceViewResolver。

在后面我们会详细讲解View resolvers ，目前我们只讲一下InternalResourceViewResolver。

```java
    @RequestMapping("/")
    public String welcome(Model model){
        model.addAttribute("greeting","Welcome to Web Store");
        model.addAttribute("tagline","The one and only amazing web store");
        return "welcome";
        // 该方法返回 welcome 给InternalResourceViewResolver
        // InternalResourceViewResolver 根据prefix 和 suffix
        // 返回 /WEB-INF/jsp/welcome.jsp 给dispatcher servlet。
    }
```

## Spring MVC框架的工作流程

![](https://github.com/huangdaren1997/CreatePicUrl/blob/master/Java/JavaWeb/SpringMVC/spring%20mvc%20request%20flow.png?raw=true)

1. 请求来到dispatcher servlet，dispatcher servlet把请求发送给相应的Controller进行处理。
2. Controller对Model中的对象进行更新，然后返回view的名称给Controller。
3. Controller使用view resolver找出view的实际路径，然后把model传递给view。
4. view使用Model提供的数据进行渲染，然后把页面传递给dispatcher servlet
5. dispatcher servlet 把页面返回给用户。



# Web application architecture

## The domain layer （领域层）

领域层通常由领域模型组成。那么什么是领域模型？

领域模型是数据存储类型的表现。它描述了多个领域对象，例如它们的属性，介绍，关系。

我们来根据实际例子来理解理解。

![](https://github.com/huangdaren1997/CreatePicUrl/blob/master/Java/JavaWeb/SpringMVC/sample%20domain%20model.png?raw=true)

每一块代表一个业务实体，直线代表它们的之间的联系。通过上图，我们可以知道，在订单处理的领域，一个顾客可以有多个订单，一个订单可以有多个订单项目，每个订单项目代表一个产品。

在开发中，开发人员把领域模型转化成相应的具有联系的领域对象。

**目前为止我们的网络商城只有欢迎页面，是时候添加商品了**





## 总结

在本章中，首先我们知道了dispatcher servlet的作用，以及它是如何通过使用@RequestMapping注解来映射请求的。

然后我们了解了什么Web Application context以及如何对它进行配置。

然后我们对view resolvers 和 InternalResourceViewResolver 进行了简单的介绍。

同时我们还了解了Spring MVC处理请求的整个工作流程。

最后我们还做了一个完成了我们网络商城的一部分。