# Spring Security笔记

Spring Security是一个安全框架,其主要提供了认证与访问控制功能.

Spring Security不仅适用于基于HTTP和Servlet API构建Web应用,也适用于普通的APP.

自从Spring Security 3.0以后,它被划分为多个模块,每个模块提供不同的功能,下面我们来大致了解一下这些模块的功能.

1. Core - spring-security-core.jar

   包含认证与访问控制的核心类与接口

2. Config - spring-security-config.jar

   包含负责XML配置和Java配置的代码

3. Web - spring-security-web.jar

   包含一些Servlet Filter以及与Web安全相关的代码

4. Test - spring-security-test.jar

5. OAuth2.0相关

   1. OAuth 2.0 Core - spring-security-oauth2-core.jar

      包含了用于支持*OAuth 2.0 Authorization Framework* 和 *OpenID Connect Core 1.0*. 的核心类和接口

   2. OAuth 2.0 Client - spring-security-oauth2-client.jar

   3. OAuth 2.0 JOSE - spring-security-oauth2-jose.jar

      提供对JOSE框架(Javascript Object Signing and Encryption,JS对象签名与加密)的支持

      The *JOSE* framework is intended to provide a method to securely transfer claims between parties. It is built from a collection of specifications:

      - JSON Web Token (JWT)
      - JSON Web Signature (JWS)
      - JSON Web Encryption (JWE)
      - JSON Web Key (JWK)

6. ACL - spring-security-acl.jar

   A Java access control list (ACL) is a data structure that grants or denies permission to access resources based on its object entries.

7. CAS - spring-security-cas.jar

8. OpenID - spring-security-openid.jar

9. Remoting - spring-security-remoting.jar

   与Spring Remoting进行整合

10. LDAP - spring-security-ldap.jar

    包含负责LDAP认证和配置的代码

## 核心组件

`Authentication`:代表一个认证

`SecurityContext`:存储`Authentication`

`SecurityContextHolder`:存储`SecurityContext`

`SecurityContextHolder`默认使用`ThreadLocal`模式存储`SecurityContext`

`AuthenticationProvider`:处理`Authentication`

`AuthenticationManager`:处理`Authentication`,默认实现`ProviderManager`通过调用`AuthenticationProvider`去处理`Authentication`

`AuthenticationManagerBuilder`：用于创建`AuthenticationManager`，方便构建in memory authentication, LDAP authentication, JDBC based authentication, adding UserDetailsService, and adding AuthenticationProvider's.



`UserDetails` :存储用户信息,这些信息有一部分稍后会被封装进`Authentication`

`UserDetailsService`:加载用户相关信息,充当于该框架中的UserDAO

`UserDetailsManager`：`UserDetailsService`的子类，添加了创建新User和更新已存在的User的功能

`User`：`UserDetails`的实现，方便我们使用。

**表单登录大致过程**

在Web容器启动前，我们已经往注册了一系列的的Filter。请求到达后，容器调用FilterChainProxy，FilterChainProxy遍历调用我们注册的Filter，在UsernamePasswordAuthenticationFilter，它识别出当前是一个表单登录的请求，然后调用自身的attemptAuthentication方法进行认证，attemptAuthentication方法调用AuthenticationManager进行认证，



## 认证

## Web应用中的认证

## 访问控制