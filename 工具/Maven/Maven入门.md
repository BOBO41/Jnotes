## Maven是什么？

Maven可以翻译作“知识的积累”，也可以翻译为“专家”或“内行”。Maven主要提供的服务是基于Java平台的项目构建、依赖管理、项目信息管理。

**什么是构建？**

在开发中，除了编写代码，我们还需要做编译、单元测试、生成文档、打包和部署等重复的事情，这就是构建。

## 一个例子

这是一个运用了策略模式的加密器，用来对字符串进行加密。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion> <!--当前POM的版本-->
    <groupId>com.hdr.learn.DesignPattern</groupId>
    <artifactId>DesignPattern</artifactId>
    <version>1.0-SNAPSHOT</version>
  	<!--
			groupId、artifactId、version这三个元素定义了一个项目的基本坐标
		-->
  	
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <target>1.8</target>
                    <source>1.8</source>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

