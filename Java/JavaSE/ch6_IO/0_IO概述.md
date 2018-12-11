---
title : 0.IO概述
categories : 
- JavaSE
- ch4IO
date : 2018-5-15
---

# Java IO 概述

程序的主要任务是操纵数据，在运行时，这些数据都必须位于内存中，并且属于特定的类型，这样程序才能处理它们。

本章介绍如何从数据源中读取数据供程序使用，以及如何把程序处理后的数据写到数据目的地。

Java.io包是java提供的用于处理程序输入输出的类库。

![1541226107923](https://github.com/huangdaren1997/pictures/blob/master/JavaIO%E4%BD%93%E7%B3%BB.png?raw=true)

**节点流与处理流**

按照流是否直接与特定的地方(如磁盘、内存、设备等)相连，分为节点流和处理流两类。

**节点流**：可以从或向一个特定的地方(节点)读写数据

**处理流**：对一个已存在的流的连接和封装，通过所封装的流的功能调用实现数据读写

下面四个类的子类都是处理流

- FilterInputStream
- FilterOutputStream
- FilterReader
- FilterWriter

不是上面四个类的子类，也有可能是处理流。

