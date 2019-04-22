# MyBatis生成器

## 快速入门

1. 创建和填写MyBatis生成器的配置文件（下面是你必须填写）

    1. `<jdbcConnection>`：指定如何连接数据库
    2. `<javaModelGenerator>`：指定在哪个项目哪个目录下生成模型对象
    3. `<sqlMapGenerator>`：指定在哪个项目哪个目录下生成`Mapper`文件
    4. `<table>`：（至少指定一个）根据表生成模型对象和Mapper文件

2. 命令行运行

    `java -jar mybatis-generator-core-x.x.x.jar -configfile generatorConfig.xml -overwrite`


## 运行MyBatis生成器

我们可以通过以下方式运行MyBatis生成器:

- From the [command prompt](http://www.mybatis.org/generator/running/runningFromCmdLine.html) with an XML configuration
- As an [Ant task](http://www.mybatis.org/generator/running/runningWithAnt.html) with an XML configuration
- As a [Maven Plugin](http://www.mybatis.org/generator/running/runningWithMaven.html)
- From another [Java program](http://www.mybatis.org/generator/running/runningWithJava.html) with an XML configuration
- From another [Java program](http://www.mybatis.org/generator/running/runningWithJava.html) with a Java based configuration
- As an [Eclipse Feature](http://www.mybatis.org/generator/running/runningWithEclipse.html)

## 配置文件详解

MyBaits生成器`MyBatis Generator (MBG)`的配置文件主要有以下功能：

- 指定连接哪个服务器
- 生成什么对象以及如何生成
- 根据哪些数据库表来生成对象

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
  PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
  "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
  <!--指定用于连接数据库的驱动的所在位置-->
  <classPathEntry location="/Program Files/IBM/SQLLIB/java/db2java.zip" />
  
  <context id="DB2Tables" targetRuntime="MyBatis3">
    <jdbcConnection driverClass="COM.ibm.db2.jdbc.app.DB2Driver"
        connectionURL="jdbc:db2:TEST"
        userId="db2admin"
        password="db2admin">
    </jdbcConnection>

    <javaTypeResolver >
      <!--关闭强制转换BigDecimals-->
      <property name="forceBigDecimals" value="false" />
    </javaTypeResolver>
      

    <javaModelGenerator targetPackage="test.model" targetProject="\MBGTestProject\src">
      <!--启动子包后模型对象会被放置在test.model.db2admin（数据库名）目录下-->
      <!--不启动子包后模型对象会被放置在test.model目录下-->
      <property name="enableSubPackages" value="true" />
      <property name="trimStrings" value="true" />
    </javaModelGenerator>

    <sqlMapGenerator targetPackage="test.xml"  targetProject="\MBGTestProject\src">
      <!--同上-->
      <property name="enableSubPackages" value="true" />
    </sqlMapGenerator>

    <javaClientGenerator type="XMLMAPPER" targetPackage="test.dao"  
                         targetProject="\MBGTestProject\src">
      <!--同上-->
      <property name="enableSubPackages" value="true" />
    </javaClientGenerator>

    <table schema="DB2ADMIN" tableName="ALLTYPES" domainObjectName="Customer" >
      <property name="useActualColumnNames" value="true"/>
      <generatedKey column="ID" sqlStatement="DB2" identity="true" />
      <columnOverride column="DATE_FIELD" property="startDate" />
      <ignoreColumn column="FRED" />
      <columnOverride column="LONG_VARCHAR_FIELD" jdbcType="VARCHAR" />
    </table>

  </context>
</generatorConfiguration>
```

[更多可选元素请看这里。](http://www.mybatis.org/generator/configreference/classPathEntry.html)

## 使用生成的对象

`MBG`生成以下类型的对象 (unless you use the MyBatis3DynamicSql runtime):

1. [Java Model Objects](http://www.mybatis.org/generator/generatedobjects/javamodel.html) (always)
2. [SQL Map Files](http://www.mybatis.org/generator/generatedobjects/sqlmap.html) (always for iBATIS, usually for MyBatis)
3. [Java Client Objects (optional)](http://www.mybatis.org/generator/generatedobjects/javaclient.html)
4. A class for use in the xxxByExample methods. See the following pages for information about that class:
    - [Example Class Usage Notes](http://www.mybatis.org/generator/generatedobjects/exampleClassUsage.html)
    - [Extending the Example Classes](http://www.mybatis.org/generator/generatedobjects/extendingExampleClass.html)