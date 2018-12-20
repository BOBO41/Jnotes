# Content Negotiation 内容协商 

分析需要通过哪种数据形式返回数据叫做内容协商。

请求方有三种方式告诉SpringMVC它需要的是哪一种形式的数据。

- URL后缀

    ```
    http://myserver/myapp/accounts/list.html
    http://myserver/myapp/accounts/list.xls
    ```

- URL参数

    ```
    http://myserver/myapp/accounts/list?format=html
    http://myserver/myapp/accounts/list?format=xls
    ```

- 请求头的Accept

    ```
    Accept: text/html,application/xhtml+xml,application/xml;
    ```

**先检查URL后缀，再检查URL参数，最后检查Accept**

## URL后缀

默认情况下，Spring框架可以通过检测URL后缀来去确定响应消息体的内容类型的。

接下来通过Java和XML文件配置两种方法来对这种以后缀作为内容协商方法的策略进行设置。

```java
@Override
public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
  configurer
    .favorPathExtension(true) // 使用后缀方式进行内容协商
    .favorParameter(false) // 禁用使用URL查询方式进行内容协商
    .ignoreAcceptHeader(true) // 忽略请求头部的Accept字段
    .useJaf(false) // 禁用JAF去解析内容类型
    .defaultContentType(MediaType.APPLICATION_JSON); // 设置默认响应消息体内容类型为JSON
}
```

```xml
<bean id="contentNegotiationManager"
  class="org.springframework.web.accept.ContentNegotiationManagerFactoryBean">
    <property name="favorPathExtension" value="true" />
    <property name="favorParameter" value="false"/>
    <property name="ignoreAcceptHeader" value="true" />
    <property name="defaultContentType" value="application/json" />
    <property name="useJaf" value="false" />
</bean>
```

## 方法查询

```java
public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
  configurer
    .favorPathExtension(false)
    .favorParameter(true)
    .parameterName("format") // 内容类型查询参数为format
    .ignoreAcceptHeader(true)
    .useJaf(false)
    .defaultContentType(MediaType.APPLICATION_JSON)
    .mediaType("xml", MediaType.APPLICATION_XML)    // 设定不同参数值所对应的内容类型
    .mediaType("json", MediaType.APPLICATION_JSON); // 设定不同参数值所对应的内容类型
}
```

```xml
<bean id="contentNegotiationManager"
  class="org.springframework.web.accept.ContentNegotiationManagerFactoryBean">
    <property name="favorPathExtension" value="false" />
    <property name="favorParameter" value="true"/>
    <property name="parameterName" value="format"/>
    <property name="ignoreAcceptHeader" value="true" />
    <property name="defaultContentType" value="application/json" />
    <property name="useJaf" value="false" />

    <property name="mediaTypes">
        <map>
            <entry key="json" value="application/json" />
            <entry key="xml" value="application/xml" />
        </map>
    </property>
</bean>
```

## 头部Accept字段

```java
@Override
public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
  configurer
    .favorPathExtension(false)
    .favorParameter(false)
    .ignoreAcceptHeader(false)
    .useJaf(false)
    .defaultContentType(MediaType.APPLICATION_JSON);
}
```

```xml
<bean id="contentNegotiationManager"
  class="org.springframework.web.accept.ContentNegotiationManagerFactoryBean">
    <property name="favorPathExtension" value="false" />
    <property name="favorParameter" value="false"/>
    <property name="ignoreAcceptHeader" value="false" />
    <property name="defaultContentType" value="application/json" />
    <property name="useJaf" value="false" />
</bean>
```

