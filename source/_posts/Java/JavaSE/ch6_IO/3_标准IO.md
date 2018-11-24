---
title : 3.标准IO
categories : 
- JavaSE
- ch4IO
date : 2018-5-16
---

# 标准IO

对于某些应用程序，需要在程序运行的整个生命周期中，从同一个数据源读入数据，或者向同一个数据汇输出数据，例如日志信息。

在JDK的java.lang.System类中，java提供了三个静态变量

- System.in  InputStream 默认读取键盘输入的内容
- System.out PrintStream 默认把信息输出到控制台
- System.err PrintStream 默认把信息输出到控制台

```java
public static void main(String[] args) throws IOException {
   BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
   String data = br.readLine();
   while (data != null && !data.equals("exit")) {
      System.out.println("echo:"+data);
      data = br.readLine();
   }

}
```



## 重定向标准I/O

```java
setIn(InputStream in);
setOut(OutputStream out);
setErr(OutputStream out);
```

