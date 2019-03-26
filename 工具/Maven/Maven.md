# Maven入门

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

# 坐标、依赖配置、仓库

## 坐标

由于Java使用者多，它们创造了大量构件，也就是平时用的一些jar、war等文件。Maven其中一个作用就是方便我们下载和管理这些构件。在Maven世界中，我们通过groupId、artifactId、version这三个元素定义了一个构件的基本坐标。有了这个坐标，我们就能从Maven提供的中央仓库中下载所需的构件。

## 依赖配置

```xml
<dependencies>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

- groupId、artifactId、version
- type：依赖的类型，默认为jar
- scope：依赖的范围
- optional：依赖是否可选
- exclusions：用来排除传递性依赖

## 依赖的范围

Maven在编译项目主代码的时候需要使用一套classpath、测试代码的时候会使用另一套classpath、运行的时候，又会使用一套classpath。依赖范围就是对这三个classpath起作用。

- compile：编译依赖范围。默认，对于编译、测试、运行classpath都有效。

- test：测试依赖范围。只对测试classpath有效，典型例子junit。

- provided：对于编译和测试时有效，但在运行时无效。典型例子servlet-api。

- runtime：运行时依赖。对于测试和使用有效，但是编译时无效。典型例子JDBC驱动。

- system：本地Maven仓库之外的类库文件。对于编译和测试时有效，但在运行时无效。

  ```xml
  <dependency>
      <groupId>xxx</groupId>
      <artifactId>xx</artifactId>
      <version>1.0</version>
      <scope>system</scope>
      <systemPath>${path-to-the-jar}</systemPath>
  </dependency>
  ```

## 依赖的传递

项目A需要项目B，使用Maven导入项目A的时候，它会自动帮我们导入项目B。![1553415397708](../images/Maven依赖范围对依赖传递的影响.png)

最左边一列是项目A的依赖范围取值，最上面一行是项目B的依赖范围取值。

## 依赖冲突

有些时候，导入的两个项目可能会依赖于同一个项目，但是依赖的项目版本不一样，这时候，

1. 根据路径来选择，导入路径更短的依赖

2. 如果路径相等，先声明的先导入

3. 排除掉其中一个

   ```xml
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-context</artifactId>
       <version>5.1.5.RELEASE</version>
       <exclusions>
           <exclusion>
               <groupId>org.springframework</groupId>
               <artifactId>spring-aop</artifactId>
           </exclusion>
       </exclusions>
   </dependency>
   ```

## 可选依赖

A依赖B，X、Y是B的可选依赖。这时候X、Y不会对A有任何作用。

## 仓库分类

- 本地仓库
- 远程仓库
  - 中央仓库
  - 其他公共仓库
  - 私服

本地仓库默认在`~/.m2/repository`,如果想修改本地仓库存储位置，可以修改`~/.m2/settings.xml`，当然这个配置文件默认是不存在，需要我们复制maven项目conf目录下的settings.xml。

把当前项目添加到本地仓库`mvn clean install`。

**远程仓库的配置**

**远程仓库的认证**

```xml
<settings>
...
    <servers>
    	<server>
        	<id></id> <!--仓库id-->
            <username></username>
            <password></password>
        </server>
    </servers>
</settings>
```

**把构件部署到远程仓库**

```xml
<project>
...
    <distributionManagement>
    	<repository>
        	<id></id>
            <name></name>
            <url></url>
        </repository>
        <snapshotRepository>
        	<id></id>
            <name></name>
            <url></url>
        </snapshotRepository>
    </distributionManagement>
</project>

<!--配置正确后，运行 mvn clean deploy将会把项目部署到远程仓库中-->
```

**想一想为什么要有发布版本和快照版本、以及版本号的命名规则**

# 生命周期与插件

Maven生命周期包含了项目的清理、初始化、编译、测试、打包、集成测试、验证、部署和站点生产等几乎所有构建步骤。Maven生命周期只是负责抽象，具体的实现是通过插件来完成的。Maven默认为我们提供了一些插件，例如编译周期提供了`maven-compile-plugin`、测试周期提供了`maven-surefire-plugin`。

Maven拥有三套相互独立的生命周期。每个生命周期含有一些阶段（phase）

- clean：清理项目
- default：构建项目 [官方文档](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)
- site：？？？

## 插件目标

一个插件往往能完成多个目标。例如`maven-dependency-plugin`插件提供了不少的目标。

`mvn dependency:tree`、`mvn dependency:list`

## 插件绑定

# 聚合与继承

## 聚合

一个项目往往由多个模块组成，我们需要分别对这些项目进行构建，这很麻烦，我们想要一个功能，就是一次就能把所有项目构建完毕。

为了实现这个功能，我们需要创建一个额外的模块，通过这个模块构建整个项目的所有模块。下面是这个模块的POM文件。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.hdr.mymall</groupId>
    <artifactId>mymall-aggregator</artifactId>
    <version>1.0-SNAPSHOT</version>
	<packaging>pom</packaging> <!--对于聚合模块来说，它的打包方式必须是pom-->
    <name>MyMall Aggregator</name>
    
    <modules>
    	<module>mymall-account</module>
        <module>mymall-order</module>
        <!--这里每个module的值都该模块与聚合模块pom文件的相对位置-->
    </modules>
</project>
```

这时候对该聚合模块执行构建就会对所有模块进行构建。

## 继承

很多时候，一个项目的多个模块会共用一些依赖，如果每个在每个项目都导入这些依赖，那就太浪费了，这时候我们可以创建一个父模块，在父模块导入这些共用的依赖，子模块继承父模块即可得到那些依赖。

```xml
<!--父模块-->
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.hdr.learn</groupId>
    <artifactId>maven-parent</artifactId>
    <version>1.0-SNAPSHOT</version>
	<packaging>pom</packaging> 
    
    <dependencies>
    	<dependency>
            ......
        </dependency>
    </dependencies>
    

</project>
```

```xml
<!--子模块-->
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
   
    <parent>
    	<groupId></groupId>
        <artifactId></artifactId>
        <version></version>
        <relativePath></relativePath>
        <!--构建项目的时候，首先会根据relativePath检查父POM，如果找不到再从本地仓库找-->
    </parent>
    <!--这里没有声明groupId、version，因为它能继承父模块的-->
    <artifactId>maven-son</artifactId>
    
    <dependencies>
    	<dependency>
            ......
        </dependency>
    </dependencies>
    

</project>
```

## 依赖管理

有些时候，父模块导入的依赖不是所有子模块都需要的，这时候我们可以使用`dependencyManagement`元素。

`dependencyManagement`元素下的依赖声明并不会引入实际的依赖，不过它能约束`dependencies`下的依赖使用。

## 插件管理

对于插件，同样也提供了`pluginsManagement`元素。

# 使用Nexus创建私服

# 使用Maven进行测试

## 测试

Maven本身并不是一个单元测试框架，Maven通过插件来执行JUnit、TestNG的测试用例。这一插件就是`maven-surefire-plugin`。通过`mvn test`命令即可执行测试。

在默认情况下，`maven-surefire-plugin`的test目标会自动执行测试源码路径（src/test/java）下所有符合下面命名模式的测试类。

- `**/Test*.java`
- `**/*Test.java`
- `**/*TestCase.java`

## 跳过测试

有时候我们需要Maven跳过测试。可以通过参数`skipTests`实现.`mvn package -D skipTests`。

## 动态指定要运行的测试用例

`mvn test -D test=RandomGeneratorTest`

`mvn test -D test=TestOne,TestTwo`

`mvn test -D test=*Test`

## 包括与排除测试用例

## 测试报告

默认情况下，`maven-surefire-plugin`会在项目的`target/surefire-reports`目录下生成两种格式的错误报告：

- 简单文本格式
- 与JUnit兼容的XML格式

**测试覆盖率报告**

通过`cobertura-maven-plugin`我们可以为Maven项目生成测试覆盖率报告。

`mvn cobertura:cobertura` 会在`target/site/cobertura/`下生成一个index.html文件，打开就能看到测试覆盖率报告。

## 重用测试代码

# 使用Hudson进行持续集成



# 版本管理

**版本管理：** 项目整体版本的演变过程管理

**版本控制：** 借助版本控制工具追踪代码的每一个变更

## Maven版本号定义约定

Maven的版本号约定是这样的：`主版本号.次版本号.增量版本-里程碑版本`。

主版本：表示项目重大架构的变更

次版本：表示较大范围 的功能增大和变化，及bug修复

增量版本：一般表示重大Bug的修复

里程碑版本：

## 自动化版本发布

发布一个版本往往需要走很多个流程，因此Maven提供了`Maven-Release-Plugin`。

该插件主要有三个目标。`release：prepare`、`release：rollback`、`release：perform`。

# 常用Maven插件

