---
title : 1.Servlet概述
categories : 
- JavaWeb
- Servlet
date : 2018-7-3
---

# Servlet

## 什么是Servlet？

Java Servlets 是一个运行在Web服务器或Application服务器上的程序，是浏览器或HTTP客户端与HTTP服务器上的数据库或应用进行交互的中间人。

Servlets是一种实现了 **javax.servlet.Servlet** 接口用来处理HTTP请求的类。
Web application developers typically write servlets that extend javax.servlet.http.HttpServlet, an abstract class that implements the Servlet interface and is specially designed to handle HTTP requests. 

## Servlets Architecture

下图向你展示Servlets在Web应用中的位置。

![](https://www.tutorialspoint.com/servlets/images/servlet-arch.jpg)

## Servlets Tasks

Servlets主要任务

1. 读取显式（explicit）发送到服务器的数据。This includes an HTML form on a Web page or it could also come from an applet or a custom HTTP client program. 
2. 读取隐式（implicit）发送到服务器的数据。This includes cookies, media types and compression schemes the browser understands, and so forth. 
3. 处理数据并且返回结果。This process may require talking to a database, executing an RMI or CORBA call, invoking a Web service, or computing the response directly. 
4. 发送显式数据回客户端。This document can be sent in a variety of formats, including text (HTML or XML), binary (GIF images), Excel, etc. 
5. 发送隐式数据回客户端。 This includes telling the browsers or other clients what type of document is being returned (e.g., HTML), setting cookies and caching parameters, and other such tasks. 