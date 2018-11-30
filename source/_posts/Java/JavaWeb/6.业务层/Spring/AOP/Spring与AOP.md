---
title : Spring与AOP
categories : 
- JavaWeb
- Spring
date : 2018-11-29
---

# Spring与AOP

## AOP概念

- Aspect：对类进行切割的模块。可以通过实现类的方法来创建Aspect，也可以对类使用@Aspect注解从而创建Aspect
- Join point：程序运行中的一个点。例如方法的执行、异常处理。在Spring AOP中Join point专门指方法执行
- Advice：Aspect在特定Join potin执行的操作。分为`before` 、`after`、`around`几种类型。
- Pointcut：通过某些规则筛选出来的Join point。（因为我们不想对所有Join point都添加Advice）
- 