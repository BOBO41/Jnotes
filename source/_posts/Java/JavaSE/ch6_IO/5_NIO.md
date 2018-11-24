---
title : 5.NIO
categories : 
- JavaSE
- ch4IO
date : 2018-5-18
---

# NIO

从JDK1.4开始,Java提供了一系列改进的输入输出处理的新功能,这些功能被称为NIO.

NIO 与 传统IO的区别在于,数据打包和传输的方法.

传统IO以流的方式处理数据,而NIO以块的方式处理数据.

## NIO原理

### NIO与IO的区别

首先来讲一下传统的IO和NIO的区别，传统的IO又称BIO，即阻塞式IO，NIO就是非阻塞IO了。还有一种[AIO](http://stevex.blog.51cto.com/4300375/1284437)就是异步IO，这里不加阐述了。

Java IO的各种流是阻塞的。这意味着，当一个线程调用read() 或 write()时，该线程被阻塞，直到有一些数据被读取，或数据完全写入。该线程在此期间不能再干任何事情了。 

Java NIO的非阻塞模式，使一个线程从某通道发送请求读取数据，但是它仅能得到目前可用的数据，如果目前没有数据可用时，就什么都不会获取。而不是保持线程阻塞，所以直至数据变的可以读取之前，该线程可以继续做其他的事情。 非阻塞写也是如此。一个线程请求写入一些数据到某通道，但不需要等待它完全写入，这个线程同时可以去做别的事情。 线程通常将非阻塞IO的空闲时间用于在其它通道上执行IO操作，所以一个单独的线程现在可以管理多个输入和输出通道（channel）。

### NIO的工作原理

在系统文件与缓冲区(buffer)之间建立通道(channel),程序从缓冲区中读取或写入数据,从而实现文件的读写.

## 缓冲区Buffer

Buffer是一个抽象类,其常用的子类是ByteBuffer 和 CharBuffer.

Buffer对象是一个固定数量的数据的容器,在这里数据可被存储并在之后用于检索。

Buffer有四个重要的属性:

- 容量（Capacity）：缓冲区能够容纳的数据元素的最大数量。这一个容量在缓冲区创建时被设定，并且永远不能改变。
- 上界(Limit)：缓冲区的第一个不能被读或写的元素。或者说,缓冲区中现存元素的计数。
- 位置(Position)：下一个要被读或写的元素的索引。位置会自动由相应的 get( )和 put( )函数更新。
- 标记(Mark)：做个标志,使用reset()方法把position转到这个标记位置.

```java
package NIO;

import java.nio.ByteBuffer;

public class BufferDemo {

	public static void main(String[] args) {
		
		// 创建一个字节缓冲区,容量是1024个字节.
		ByteBuffer buf = ByteBuffer.allocate(1024);
		// 把数据放入缓冲区
		buf.put((byte)10);
		buf.put((byte)20);
		buf.put((byte)30);
		System.out.println("position:"+buf.position());
		System.out.println("limit:"+buf.limit());
		System.out.println("capacity:"+buf.capacity());
		
		System.out.println("----------------");
		
		// 缓冲区反转
		buf.flip();
		/*
		 * 这是干嘛的呢?
		 * 其实就是 limit = position; position = 0;
		 */
		System.out.println("position:"+buf.position());
		System.out.println("limit:"+buf.limit());
		System.out.println("capacity:"+buf.capacity());
		
		System.out.println("----------------");
		// 获取数据
		if(buf.hasRemaining()) // 判断缓冲区是否有数据
		{
			for(int i=0; i<buf.remaining();i++)
			{
				Byte b = buf.get(i);
				System.out.println(b);
			}
		}

	}

}

```

Buffer 常用方法

```java
int capacity(); //获取Buffer的容量
boolean hasRemaining(); // 判断position到limit之间是否还有元素可处理
int limit(); 
Buffer limit(int newLt); //获取具有新的limit的缓冲区对象.
Buffer mark(); // 设置mark
Buffer reset(); // 将position弄到mark的位置
int postion();
Buffer position(int newPs);
int remaining();
Buffer rewind(); //将position设置为0,清除mark
Buffer clear(); //清楚Buffer的内容.
```



## channel

```java
package NIO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelDemo {

	public static void main(String[] args) throws Exception {
		//创建文件通道
		FileChannel fcIn = new FileInputStream("C:\\Users\\Administrator\\Desktop\\test.doc").getChannel();
		FileChannel fcOut = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\test2.doc").getChannel();
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		while(fcIn.read(buf)!=-1)
		{
			buf.flip();  //防止buffer没有被填满
			fcOut.write(buf);
			buf.clear();
		}
		fcIn.close();
		fcOut.close();
		System.out.println("done");

	}

}
```



## 字符集和Charset

Java.nio.charset.Charset

```java
//获取Java支持的全部字符集
SortedMap<String,Charset> map = Charset.availableCharsets();
for (String alias:map.keySet())
{
    System.out.println(alias+":"+map.get(alias));
}
// 创建字符集对象
Charset cs =  Charset.forName("GBK");
// 创建编码对象
CharsetEncoder csEncoder = cn.newEncoder();
// 创建解码对象
CharsetDecoder csDecoder = cn.newDecoder();
// 创建字符缓冲区
CharBuffer cbuff = CharBuffer.allocate(8);
cbuff.put("黄");
cbuff.put("大");
cbuff.put("仁");
cbuff.flip();

ByteBuffer bbuff = csEncoder.encode(cbuff);
// ByteBuffer bbuff = cs.encode(cbuff);
for(int i=0; i<bbuff.capacity();i++)
{
	System.out.print(bbuff.get(i)+" ");
}
System.out.println("\n"+ csDecoder.decode(bbuff));

```



## 文件锁

在NIO中,Java提供了FileLock来支持文件锁定功能,在FileChannel中提供的lock() / tryLock()方法可以获得文件锁FileLock对象.

```java
lock(Long postion,long size,boolean shared)
// 对文件从postion开始,长度为size的内容加锁,该方法是阻塞式的
tryLock(long position,long size ,boolean shared)
// 同上,该方法是非阻塞式的
shared 表示该锁是否共享.默认为false;
```

```java
package NIO;

import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.io.*;

public class FileLockDemo {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
			try 
			(
				FileChannel fcOut = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\test.doc").getChannel();
			)
			{
				FileLock lock = fcOut.tryLock();
				Thread.sleep(1000);
				lock.release();
			}
	}

}
```

### 文件锁的注意点

① 在某些平台上,一个程序不能获得文件锁,它也能对该文件进行读写.

② 在某些平台上,不能同步地锁定一个文件并把他映射到内存中.

③ 文件锁是由Java虚拟机所持有的,如果两个Java程序使用同一个Java虚拟机运行,则他们不能对同一个文件进行加锁.

④ 在某些平台上,关闭FileChannel时,会释放Java虚拟机在该文件上的所有锁,因此应该皮面对同一个被锁定的文件打开多个FileChannel.

## Java7的NIO

Java7对NIO进行了重大改进

① 提供了全面的文件IO和文件系统访问支持.

② 基于异步Channel的IO

### Path

早起Java只提供了一个File类来访问文件系统,为了弥补不足,Java7中引入了一个Path接口.

Path接口代表一个平台无关的平台路径.

同时还加入Files 和 Paths两个工具类.

具体请看API并且总结出来



