---
title : Tomcat安装与基本配置
categories : 
- JavaWeb
- Tomcat
date : 2018-7-2
---

# Tomcat

Tomcat是一个执行Java Servlet然后渲染包含JSP代码网页的应用服务器。

## Tomcat安装与运行

### 安装:

[https://tomcat.apache.org/download-90.cgi]()

### 配置:

1. 在环境变量中添加JAVA_HOME

![](https://github.com/HuangYiCheng1997/create-picture-url/blob/master/java/Tomcat%E7%8E%AF%E5%A2%83%E5%8F%98%E9%87%8F.png?raw=true)

2. 在环境变量中添加CATALINA_HTOM

![](https://github.com/HuangYiCheng1997/create-picture-url/blob/master/java/catalina_home.png?raw=true)

3.添加环境变量

![](https://github.com/HuangYiCheng1997/create-picture-url/blob/master/java/tomcat%E7%8E%AF%E5%A2%83%E5%8F%98%E9%87%8F2.png?raw=true)



### 运行:

打开tomcat安装路径下的bin文件夹,双击start.bat.

### 检验:

打开浏览器,输入127.0.0.1:8080,看看是否出现了Tomcat的网页.



## Tomcat目录结构

1. bin:	 存放着启动和关闭tomcat的脚本
2. conf: 存放着tomcat的配置文件
3. lib:    存放着tomcat服务器和web应用依赖的jar文件
4. logs:  存放着tomcat运行产生的日志文件
5. temp: 运行产生的临时文件
6. webapps: web应用目录以及他的资源文件
7. work:  tomcat将JSP文件翻译成servlet源码以及编译字节码




## Web应用基础知识

### 什么是web应用?

​	浏览器访问的程序.

1. 组成web应用的资源有哪些?
    1. html css js image

       2. JSP Java 支持的jar

       3. 配置文件

2. 什么是网站的根目录?

   1. 包裹全部web应用资源的目录

3. 虚拟目录的映射

   http://127.0.0.1:8080/ 对应的就是tomcat/webapps这个目录

4. 虚拟目录的配置

   因为我们不可能所有东西都放到webapss这个目录里面,所以我们需要修改虚拟目录的映射

   

   配置方式1:

   ​	修改tomcat/conf/server.xml

   ​	在Host标签中插入Content标签

   ```xml
   <Context path="/blog" docBase="D:\apache-tomcat-9.0.7\webapps\FirstWebProject"></Context>
   <!-- path虚拟目录名称  docBase Web应用所在的目录 -->
   ```

   **注意:官方不推荐这种配置方式**

   ​

   配置方式2:

   ​	在tomcat/conf/catalina/localhost目录下创建一个xml

   ​	xml文件的名字会作为虚拟目录名

   ```xml
   <Context docBase="D:\apache-tomcat-9.0.7\webapps\FirstWebProject"></Context>
   ```

   ​	缺省配置:ROOT.xml(需要重启)

## JavaWeb动态项目的目录结构

```
|-- webapp                         # 站点根目录
    |--html,css,js image 可以被外界直接访问
    |--WEB-INF 浏览器无法访问的目录 
    	|--lib 存放web依赖的jar文件
    	|--classes 存放当前web项目源码生成的class文件
    	|--web.xml web项目配置文件(必须的)
```
**注意:webapp是存放在tomcat安装目录下的webapps目录中**



## 网络资源访问流程

![](https://github.com/HuangYiCheng1997/create-picture-url/blob/master/java/%E7%BD%91%E7%BB%9C%E8%B5%84%E6%BA%90%E7%9A%84%E8%AE%BF%E9%97%AE%E8%BF%87%E7%A8%8B.png?raw=true)





### 其他问题

[https://www.bilibili.com/video/av19008301/?p=10]()