# Web Application Security

本章我们会探索Spring Security如何为基于HTTP和Servlet API构建的Web应用提供认证与访问控制功能，以及其背后是哪些类和接口在发挥作用。

## Security Filter Chain

Spring Security通过一条Filter链来维护Web应用的安全，每个Filter负责各自的任务。

### DelegatingFilterProxy

`DelegatingFilterProxy`是一个Servlet Filter代理，代理的是Spring管理的实现了Filter接口的Bean。

作用是让真正的Filter能享受Spring的依赖注入以及Bean生命周期管理等功能。

### FilterChainProxy

理论上，我们可以在Spring容器注册我们想要的`Filter Bean`，然后在`Web.xml`中注册对应的`DelegatingFilterProxy`，但是这样`Web.xml`文件很容易变得很臃肿。这时候我们可以使用`FilterChainProxy`,我们在`FilterChainProxy`设置需要的Filter,然后在`Web.xml`注册`FilterChainProxy`即可。

```xml
<bean id="filterChainProxy" class="org.springframework.security.web.FilterChainProxy">
<constructor-arg>
    <list>
    <sec:filter-chain pattern="/restful/**" filters="
        securityContextPersistenceFilterWithASCFalse,
        basicAuthenticationFilter,
        exceptionTranslationFilter,
        filterSecurityInterceptor" />
    <sec:filter-chain pattern="/**" filters="
        securityContextPersistenceFilterWithASCTrue,
        formLoginFilter,
        exceptionTranslationFilter,
        filterSecurityInterceptor" />
    </list>
</constructor-arg>
</bean>
```

### Filter Ordering

Filter的定义顺序是很重要的，要按照下面的顺序

- `ChannelProcessingFilter`, because it might need to redirect to a different protocol
- `SecurityContextPersistenceFilter`, so a `SecurityContext` can be set up in the `SecurityContextHolder` at the beginning of a web request, and any changes to the `SecurityContext` can be copied to the `HttpSession` when the web request ends (ready for use with the next web request)
- `ConcurrentSessionFilter`, because it uses the `SecurityContextHolder` functionality and needs to update the `SessionRegistry` to reflect ongoing requests from the principal
- Authentication processing mechanisms - `UsernamePasswordAuthenticationFilter`, `CasAuthenticationFilter`, `BasicAuthenticationFilter` etc - so that the `SecurityContextHolder` can be modified to contain a valid `Authentication` request token
- The `SecurityContextHolderAwareRequestFilter`, if you are using it to install a Spring Security aware `HttpServletRequestWrapper` into your servlet container
- The `JaasApiIntegrationFilter`, if a `JaasAuthenticationToken` is in the `SecurityContextHolder` this will process the `FilterChain` as the `Subject` in the `JaasAuthenticationToken`
- `RememberMeAuthenticationFilter`, so that if no earlier authentication processing mechanism updated the `SecurityContextHolder`, and the request presents a cookie that enables remember-me services to take place, a suitable remembered `Authentication` object will be put there
- `AnonymousAuthenticationFilter`, so that if no earlier authentication processing mechanism updated the `SecurityContextHolder`, an anonymous `Authentication` object will be put there
- `ExceptionTranslationFilter`, to catch any Spring Security exceptions so that either an HTTP error response can be returned or an appropriate `AuthenticationEntryPoint` can be launched
- `FilterSecurityInterceptor`, to protect web URIs and raise exceptions when access is denied

### Request Matching and HttpFirewall

## Core Security Filters

### FilterSecurityInterceptor

### ExceptionTranslationFilter

#### AuthenticationEntryPoint

#### AccessDeniedHandler

#### SavedRequest s and the RequestCache Interface

### SecurityContextPersistenceFilter

### UsernamePasswordAuthenticationFilter

上面我们已经了解了Spring Security Web应用中的三个重要Filter，这三个Filter会一直存在，并且不能被替换。现在我们缺少一套认证机制，它被用来对用户进行认证，并且支持自定义。`UsernamePasswordAuthenticationFilter`就是一个很常用的认证机制.

#### Application Flow on Authentication Success and Failure

Spring Security调用`AuthenticationManager` 来处理认证请求,认证的结果分别由`AuthenticationSuccessHandler` 或`AuthenticationFailureHandler` 的实现来处理.

Spring Security提供的实现有:

- `SimpleUrlAuthenticationSuccessHandler`
- `SavedRequestAwareAuthenticationSuccessHandler`
-  `SimpleUrlAuthenticationFailureHandler`
- `ExceptionMappingAuthenticationFailureHandler` 
-  `DelegatingAuthenticationFailureHandler`.

具体看文档,顺便看看`AbstractAuthenticationProcessingFilter` ,了解这些Handler的工作原理.

如果认证成功,`Authentication` 会被放入`SecurityContextHolder`,然后调用配置好的`AuthenticationSuccessHandler` .默认配置的是`SavedRequestAwareAuthenticationSuccessHandler`,用户会被重定向到其最初请求的目标.

## 整合Servlet API

本节讲述Spring Security如何与Servlet API进行整合.

### Servlet 2.5+ Integration

### Servlet 3+ Integration

## Basic and Digest Authentication

[Basic Authentication](https://swagger.io/docs/specification/authentication/basic-authentication/)

[Digest Authentication](https://searchsecurity.techtarget.com/definition/digest-authentication)

[HTTP的四种认证方式](https://blog.csdn.net/u013177446/article/details/54135356)

### BasicAuthenticationFilter

## Remember-Me Authentication

Spring Security支持Remember-Me功能,它提供了两个Remember-Me的实现.One uses hashing to preserve the security of cookie-based tokens and the other uses a database or other persistent storage mechanism to store the generated tokens.
Note that both implementations require a `UserDetailsService`. 

### Simple Hash-Based Token Approach

返回一个令牌给浏览器,令牌的内容如下

```
base64(username + ":" + expirationTime + ":" +
md5Hex(username + ":" + expirationTime + ":" password + ":" + key))

username:          As identifiable to the UserDetailsService
password:          That matches the one in the retrieved UserDetails
expirationTime:    The date and time when the remember-me token expires, expressed in milliseconds
key:               A private key to prevent modification of the remember-me token
```

###  Persistent Token Approach

### Remember-Me Interfaces and Implementations

