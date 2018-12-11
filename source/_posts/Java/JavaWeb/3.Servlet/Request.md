---
title :Request 
categories : 
- JavaWeb
- Servlet
date : 
---

# Request

`Request`对象封装了客户端请求的所有信息。在 HTTP 协议中，这些信息是从客户端发送到服务器请求的 HTTP
头部和消息体。

## Parameter

`ServletRequest` 接口的下列方法可访问这些参数：

- `getParameter`
- `getParameterNames`
- `getParameterValues`
- `getParameterMap`

## Attribute

`ServletRequest`接口提供了以下方法来操作`Attribute`

- getAttribute
- getAttributeNames
- setAttribute

**Parameter与Attribute的区别**

`Parameter`：客户端发送给服务端的HTTP请求数据(get/post)，只能是string类型的。

`Attribute`：`Request`范围内共享的数据

## Header

`servlet`可以通过`HttpServletRequest`接口的下面方法访问 HTTP 请求的头部信息：

- `getHeader`
- `getHeaders`
- `getHeaderNames`

## 请求路径相关的元素

- `Context Path`：？？？？？
- `Servlet Path`：？？？？？
- `PathInfo`：？？？？？

使用 `HttpServletRequest` 接口中的下面方法来访问这些信息：

- `getContextPath`
- `getServletPath`
- `getPathInfo`

`requestURI = contextPath + servletPath + pathInfo`

## Request 对象的生命周期

每个 request 对象只在 servlet 的 service 方法的作用域内，或过滤器的 doFilter 方法的作用域内有效，除非该
组件启用了异步处理并且调用了 request 对象的 startAsync 方法。在发生异步处理的情况下， request 对象一
直有效，直到调用 AsyncContext 的 complete 方法。容器通常会重复利用 request 对象，以避免创建 request
对象的性能开销。开发人员必须注意的是，不建议在上述范围之外保持 startAsync 方法还没有被调用的请
求对象的引用，因为这样可能产生不确定的结果。



