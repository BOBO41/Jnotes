---
title : AJAX
categories : 
- FrontEnd
- JavaScript
date : 2018-7-7
---

# AJAX

## 什么是AJAX

AJAX = Asynchronous JavaScript and XML（异步的 JavaScript 和 XML）。 

AJAX 是一种用于创建快速动态网页的技术。

使用AJAX技术可以在不重新加载整个网页的情况下，对网页的某部分进行更新。

---

## 如何使用AJAX

使用AJAX技术的步骤:

①创建XMLHttpRequest对象

②为XMLHttpRequest对象指定一个回调函数

③构造请求

④发送请求

⑤处理服务器响应

### 创建 XMLHttpRequest 对象

```html
<script>
    var xmlhttp;
    if(window.XMLHttpRequest){
        xmlhttp = new XMLHttpRequest();
    }
    else{
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
</script>
```

### 指定回调函数

```java
xmlhttp.onreadystatechange = functionName; //不用传递参数的
xmlhttp.onreadystatechange = function(){functionName(param)}; //需要传递参数的
/**
XMLHttpRequest对象具有一个叫作readyState的属性
该属性表示AJAX请求的状态
0:请求未初始化
1:服务器连接已经建立
2:请求已接收
3:请求处理中
4:请求已完成,且响应已就绪
*/
```

### 构造请求

```javascript
xmlhttp.open(method, url, async);
```

### 发送请求

```javascript
xmlhttp.send([string]); //发送请求 可以附带参数 DOM对象的实例 输入流  字符串
```

### 处理响应

```javascript
/**
XMLHttpRequest对象提供了两个用来访问服务器响应的属性
responseText 返回字符串响应
responseXML 返回XML响应
*/
```

### 例子

```html
<!DOCTYPE html>
<HTML lang="en">
<head>
    <meta charset="UTF-8">
    <title>ajax</title>

    <script>
        //定义回调函数
        function cFunction(xmlHttp,id){
            if(xmlHttp.readyState==4&&xmlHttp.status==200){
                document.getElementById(id).innerHTML = xmlHttp.responseText;
            }
        }
        // 创建XMLHttpRequest对象
        var xmlHttp;
        if(window.XMLHttpRequest){xmlHttp=new XMLHttpRequest()}
        else{xmlHttp=new ActiveXObject("Microsoft.XMLHTTP")}

        // 指定回调函数
        xmlHttp.onreadystatechange = function(){
            cFunction(xmlHttp,"title");
        };

        // 构造请求
        xmlHttp.open("GET","hello.txt",true);
        // 发送请求
        xmlHttp.send();

    </script>

</head>
<body>
<h1 id="title"></h1>
</body>
</HTML>
```

---

## XMLHttpRequest对象的属性和方法

### 属性

```javascript
readyState:HTTP请求的状态
status:HTTP响应状态码
statusText:HTTP响应状态码描述
responseText:目前为止接收到的响应体
responseXML:目前为止接收到的响应体
```

### 方法

```javascript
open(method,url,async) //构造请求
send();  //发送请求
abort(); //终止请求
setRequestHeader(header,value); //添加请求头

getAllResponseHeaders();//获取所有响应头
getResponseHeader(header);//获取指定响应头
```

---

## 练习

### 表单验证实例

