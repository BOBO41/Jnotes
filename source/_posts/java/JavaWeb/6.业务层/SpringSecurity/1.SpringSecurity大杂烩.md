---
title : 1.Spring Secuirty 大杂烩
categories : 
- JavaWeb
- Spring Security
date : 2018-11-13
---

# Spring Secuirty 大杂烩

Spring Security是一个企业级应用中用来用户验证、鉴权以及安全保护的Java框架。

![1541988651265](https://github.com/huangdaren1997/pictures/blob/master/SpringSecurity%E5%9F%BA%E6%9C%AC%E5%8E%9F%E7%90%86.png?raw=true)

- Authentication：认证
- Authenticated：已认证
- Authorize：授权

通过一个个Authentication Filter(认证过滤器)组成的链，来对用户进行验证，最后进入FilterSecurity拦截器，如果认真过程出现异常就交由ExceptionTranslationFilter处理。

## 流程

![1541989918171](https://github.com/huangdaren1997/pictures/blob/master/SpringSecurity%E8%A1%A8%E5%8D%95%E8%AE%A4%E8%AF%81%E6%B5%81%E7%A8%8B.png?raw=true)

通过表单提交了一个登录请求，请求就会进入用户名密码认证过滤器（`UsernamePasswordAuthenticationFilter`），该过滤器获取表单提交的用户名和密码然后生成一个用户名密码认证令牌（`UsernamePasswordAuthenticationToken`）然后调用认证管理器（`AuthenticationManager`）的认证方法（`authentication`），认证管理器(`AuthenticationManager`)只是个接口，它的实现类(`ProviderManager`)本身不会对token进行验证，它负责找出相应的认证提供者（`AuthenticationProvider`），让它进行认证工作。

在表单登录中，认证提供者（`AuthenticationProvider`）是DAO认证提供者（`DaoAuthenticationProvider`），它负责根据输入的用户名查找出相应的用户信息，然后对密码进行验证，验证通过后返回一个新的带有全名用户信息的token，（未完成待续）





## UsernamePasswordAuthenticationFilter

对一次提交进行认证处理。

登录表单需要提交两个参数给该过滤器，默认是`username`和`password`。

```java
public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
```

当然，可以通过修改`usernameParameter` 和 `passwordParameter`属性制定我们想要的参数名

```java
private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
```

## ExceptionTranslationFilter

处理所有在过滤器链中抛出的`AccessDeniedException` 和 `AuthenticationException`。

该过滤器在Java异常和HTTP响应之间建立一座桥梁。

如果是`AuthenticationException`，那么过滤器会运行`authenticationEntryPoint`，它允许共同处理源自`AbstractSecurityInterceptor`的任何子类的身份验证失败。

如果是`AccessDeniedException` ，过滤器首先判断用户是不是匿名用户，如果是则运行`authenticationEntryPoint`，否则调用`AccessDeniedHandler`。

## FilterSecurityInterceptor

通过实现过滤器接口（Filter）对HTTP资源进行安全处理。具体的工作流程需要查看它所继承的抽象类`AbstractSecurityInterceptor`

## 

# 自定义用户认证逻辑

- 处理用户信息获取逻辑：UserDetailsService
- 处理用户校验逻辑：UserDetails
- 处理密码加密解密：PasswordEncoder

## UserDetailsService

加载用户相关信息的核心接口。

框架中用它作为User DAO，以及使用了策略模式，它的具体策略会被`DaoAuthenticationProvider`所调用。

```java
public interface UserDetailsService {
   UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
```

### DaoAuthenticationProvider

从UserDetailsService中获取用户信息

## UserDetails

定义了需要存储那些用户信息的接口。

| 方法类型                                           | f方法描述                                                    |
| -------------------------------------------------- | ------------------------------------------------------------ |
| `java.util.Collection<? extends GrantedAuthority>` | `getAuthorities()`Returns the authorities granted to the user. |
| `java.lang.String`                                 | `getPassword()`                                              |
| `java.lang.String`                                 | `getUsername()`                                              |
| `boolean`                                          | `isAccountNonExpired()`                                      |
| `boolean`                                          | `isAccountNonLocked()`                                       |
| `boolean`                                          | `isCredentialsNonExpired()`                                  |
| `boolean`                                          | `isEnabled()`                                                |

## PasswordEncoder

对密码进行编码的服务接口，推荐使用其实现类`BCryptPasswordEncoder`



# 自定义登录界面

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    // 该方法返回一个 FormLoginConfigurer
    http.formLogin()
}
```

## FormLoginConfigurer

```java
public final class FormLoginConfigurer<H extends HttpSecurityBuilder<H>> 
	extends AbstractAuthenticationFilterConfigurer<H,FormLoginConfigurer<H>,
		UsernamePasswordAuthenticationFilter>
```

添加基于表单的身份验证。所有属性都有合理的默认值，所有参数都是可选的。

```java
// 根据loginProcessingUrl创建RequestMatcher 
// RequestMatcher 匹配HttpServletRequest的简单策略。
@Override
protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
   return new AntPathRequestMatcher(loginProcessingUrl, "POST");
}
```

```java
public FormLoginConfigurer<H> successForwardUrl(String forwardUrl) {
	successHandler(new ForwardAuthenticationSuccessHandler(forwardUrl));
	return this;
}

public FormLoginConfigurer<H> failureForwardUrl(String forwardUrl) {
   failureHandler(new ForwardAuthenticationFailureHandler(forwardUrl));
   return this;
}
```

它继承自AbstractAuthenticationFilterConfigurer，所以更多的方法在AbstractAuthenticationFilterConfigurer上。

## loginPage(string path)

重定向到这个路径，可以直接返回文件，也可以到达Controller再进行处理。



## loginProcessingUrl("/authentication/login")

表单中action的值

# SpringSecurity表单登录验证流程

![1541988651265](https://github.com/huangdaren1997/pictures/blob/master/SpringSecurity%E5%9F%BA%E6%9C%AC%E5%8E%9F%E7%90%86.png?raw=true)

![1541989918171](https://github.com/huangdaren1997/pictures/blob/master/SpringSecurity%E8%A1%A8%E5%8D%95%E8%AE%A4%E8%AF%81%E6%B5%81%E7%A8%8B.png?raw=true)

1. 表单登录请求进入`UsernamePasswordAuthenticationFilter`，
2. `UsernamePasswordAuthenticationFilter`使用传递过来的用户名和密码创建一个`UsernamePasswordAuthenticationToken`对象，
3. `UsernamePasswordAuthenticationFilter`调用`getAuthenticationManager()`获得`ProviderManager`对象
4. 调用`ProviderManager`对象的`authenticate(Authentication authentication)`方法
5. `authenticate(Authentication authentication)`方法找到`DaoAuthenticationProvider`类进行`authenticate`
6. 

