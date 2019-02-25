---
title : 4.RandomAccessFile
categories : 
- JavaSE
- ch6IO
date : 2018-5-16
---

# RandomAccessFile

前面我们讲的InputStream、OutputStream、Reader、Writer都是按照数据的先后顺序读写数据的。

而RandomAccessFile具有随机读写的能力，它既可以读取文件内容,也可以向文件输出数据.更重要的是,程序可以直接跳转到文件的任意地方来读写数据.

### 操作文件记录指针的方法

```java
long getFilePointer();
void seek(long pos);
```

### 读取模式

创建RandomAccessFile对象时,需要指定一个mode参数.

| **r**   | **只读**                                                     |
| ------- | ------------------------------------------------------------ |
| **rw**  | **可读写,如果文件不存在则创建文件**                          |
| **rws** | **可读写,并且要求文件的内容或元数据的每个更新都同步写入底层存储设备.** （最慢） |
| **rwd** | **可读写,并且要求文件内容的每个更新都同步写入底层存储设备.** （慢） |

**rws flushes the contents of the file and the modification date of the file.**

**rwd flushs the contents of the file, but the modification date might not change until the file is closed.**

**rw only flushes when you tell it to and doesn't change the modifcation date until you close the file.**

**BTW rwd is much slower for writes than rw, and rws is slower again.**

```java
package File;

import java.io.*;

public class TestFile {

	public static void main(String[] args) throws IOException {
		File file = new File("C:\\Users\\Administrator\\Desktop\\新区块链.txt");
		RandomAccessFile raf = new RandomAccessFile(file,"rw");
		byte[] b = new byte[1024];
		int hasRead;
		while((hasRead=raf.read(b))>0)
		{
			System.out.println(new String(b));
		}
		raf.close();
	}

}
```

