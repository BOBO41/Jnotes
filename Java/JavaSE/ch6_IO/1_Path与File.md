---
title : 1.Path与File
categories : 
- JavaSE
- ch6IO
date : 2018-5-15
---

# File、Path、Path、Files

我们知道IO就是从某个地方获取或写入数据，毫无疑问，文件是我们非常常用的获取和写入数据的地方。

所以我们很有必要了解一下Java中如何处理文件。

**File类**

File类就是文件和目录的路径名的抽象，就是File对象代表的是文件和目录的路径名

**Path接口**

看到File类居然代表的是一个路径，是不是觉得很变扭？

所以在Java7就引入了java.nio.file.Path这个接口，Path接口里面具有大量的方法，File能做的，它都能做，而且做的更好。

**Paths**

既然Path是接口，那它的实现类是什么呢？我看jdk8的文档好像没看到，那么怎么获取Path类型的对象呢？

就是通过Paths类了，Paths是个工厂类，它提供了两个静态方法让我们获取Path类型的对象。

**我看到Paths的时候，还以为是path的工具类0.0，没想到居然是工厂类**

```java
Path get(String first, String... more);
Path get(URI uri);
```

**Files**

**看到Files的时候，我以为它是File的工具类，没想到居然是Path的工具类0.0**

# Path

```java
int	compareTo(Path other); // 按字典顺序比较两条抽象路径
boolean	endsWith(Path other);// 测试此路径是否以给定路径结束。
boolean	endsWith(String other);// 测试此路径是否以给定路径结束。
boolean	equals(Object other);//测试此路径是否与给定对象相等。
Path	getFileName();//返回此路径表示的文件或目录的名称作为Path对象。
FileSystem	getFileSystem();//返回创建该文件的文件系统
Path	getName(int index);//以Path对象的形式返回此路径的名称元素
int	getNameCount();//返回路径中的名称元素个数。
Path	getParent();//
Path	getRoot()
int	hashCode()
boolean	isAbsolute()
Iterator<Path>	iterator()
Path	normalize()
WatchKey	register(WatchService watcher, WatchEvent.Kind<?>... events)
WatchKey	register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers)
Path	relativize(Path other)
Path	resolve(Path other)
Path	resolve(String other)
Path	resolveSibling(Path other)
Path	resolveSibling(String other)
boolean	startsWith(Path other)
boolean	startsWith(String other)
Path	subpath(int beginIndex, int endIndex)
Path	toAbsolutePath()
File	toFile()
Path	toRealPath(LinkOption... options)
String	toString()
URI	toUri()
```

# Paths

```java
static Path	get(String first, String... more)
static Path	get(URI uri)
```

# Files

额。。。 方法很多很多，具体要自己看文档。

- 复制
- 创建目录、文件
- 创建链接
- 创建软链接
- 创建临时目录、文件
- 存在、删除、删除如果存在
- ........真的太多了........

# File

通过java.io.File类我们可以访问文件系统，通过File类，你可以做到以下几点：

- 检测文件是否存在
- 读取文件长度
- 重命名或移动文件
- 删除文件
- 检测某个路径是文件还是目录
- 读取目录中的文件列表

File类代表的文件，而不是文件里面的内容.

## 路径相关

```java
String getName(); //返回该File对象代表的文件或目录的名字.
String getPath();//返回该File对象对应的路径
String getAbsolutePath();//返回该File对象对应的绝对路径.
String getParent(); //返回此File对象的父目录的名字
File getAbsoluteFile(); // 返回代表此File对象的绝对路径的File对象.
boolean isAbsolute();
```

## 文件操作

```java
boolean creatNewFile();//当该File对象对应的文件不存在时,创建这个对象.
static File createTempFile(String prefix,String suffix);
// 在默认的路径下创建指定的文件,并返回代表该文件的File对象.
static File createTempFile(String prefix,String suffix,File director);
// 在指定的文件夹下创建指定的文件,并返回代表该文件的File对象.
boolean delete();
void deleteOnExit();
```

## 目录操作

```java
boolean mkdir();
String[] list();
File listFiles();
static File[] listRoots();//WTF???????
```

## 其他

```java
boolean renameTo(File newFile)
boolean exists();
boolean isFile();
boolean isDirectory();
boolean canRead();
boolean canWrite();
long lastModified();
long length(); // 返回文件内容长度
```

## 文件过滤器

list()方法可以接受一FilenameFilter参数,FilenameFilter是各函数式接口,你懂的

```java
package File;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class TestFile {

	public static void main(String[] args) throws IOException {

		File desktop = new File("C:\\Users\\Administrator\\Desktop");

		FilenameFilter ff = (dir, name) -> name.equals("English");
		for (String f : desktop.list(ff)) {
			System.out.println(f);
		}
	}

}
```



