|        数据库CRUD         |            表的CRUD             |       数据库元数据        |       CRUD        |       多表操作        |
| :-----------------------: | :-----------------------------: | :-----------------------: | :---------------: | :-------------------: |
| [:scroll:](#数据库的CRUD) | [:scroll:](#存储引擎与表的CRUD) | [:scroll:](#数据库元数据) | [:scroll:](#CRUD) | [:scroll:](#多表操作) |

# 数据库的CRUD

在讲数据库的CRUD之前我们需要了解两样东西。

## SQL语句大小写规则

1.  SQL关键字和函数名是不区分大小写的。

2.  数据库名、表名和视图名跟操作系统的文件系统有关，例如Windows是不区分大小写，而Linux是区分的。

    因为MySQL使用主机上的文件和目录来表示数据库和表。

3.  存储函数、过程和时间的名字都不区分大小写

4.  列名和索引名不区分大小写

5.  别名，默认情况下表的别名区分大小写

6.  字符串值

## 字符集的支持

MySQL支持多种字符集，而且允许在服务器、数据库、表、列和字符串常量等不同层次单独指定字符集。

一个给定的字符集可以有一种或多种排序规则，所谓排序规则就是该字符集是通过什么方式来进行比较的。

我们可以通过以下例子说明：

```
mysql> show character set like '%gbk%';
+---------+------------------------+-------------------+--------+
| Charset | Description            | Default collation | Maxlen |
+---------+------------------------+-------------------+--------+
| gbk     | GBK Simplified Chinese | gbk_chinese_ci    |      2 | 
+---------+------------------------+-------------------+--------+
1 row in set (0.00 sec)

mysql> show collation like '%gbk%';
+----------------+---------+----+---------+----------+---------+
| Collation      | Charset | Id | Default | Compiled | Sortlen |
+----------------+---------+----+---------+----------+---------+
| gbk_chinese_ci | gbk     | 28 | Yes     | Yes      |       1 | 
| gbk_bin        | gbk     | 87 |         | Yes      |       1 | 
+----------------+---------+----+---------+----------+---------+
2 rows in set (0.00 sec)
从上例中我们可以看出字符集gbk有两个排序方式（分别为gbk_chinese_ci何gbk_bin），其中默认的排序方式为gbk_chinese_ci。
排序方式的命名规则为：字符集名字_语言_后缀，其中各个典型后缀的含义如下：

1）_ci：不区分大小写的排序方式

2）_cs：区分大小写的排序方式

3）_bin：二进制排序方式，大小比较将根据字符编码，不涉及人类语言，因此_bin的排序方式不包含人类语言

因此，gbk_chinese_ci排序方式就表示：字符集为gbk，人类语言使用中文来比较大小，比较时区分大小写。
```

### 指定字符集和排序规则

在用于创建数据库和表的SQL语句里，有两个句子可用于指定数据库、表和列的字符集和排序规则。

```sql
# 指定字符集
CHARACTER SET charset
# 指定字符集的排序方式
COLLATE collation
# 例子 使用utf8字符集，区分大小写的排序方法
CREATE DATABASE learn_mysql CHARACTER SET utf8 COLLATE utf8_general_cs;
```

## 数据库的CRUD

### 创建数据库

```sql
CREATE DATABASE [IF NOT EXISTS] learn_mysql [CHARACTER SET charset] 
	[COLLATE collation];
```

### 删除数据库

```sql
DROP DATABASE learn_mysql;
```

### 查看数据库

```sql
SHOW DATABASES；
```

### 使用数据库

```sql
USE learn_mysql;
```

### 更改数据库

```sql
# 这里说的更改数据库的全局属性，也就是默认字符集和排序规则，而不是数据库的名字
ALTER DATABASE learn_mysql [CHARACTER SET charset] [COLLATE collation];
```

[返回头部](#数据库的CRUD)

# 存储引擎与表的CRUD

## 存储引擎

MySQL支持多种存储引擎（以前被称为表处理器），存储引擎有好几种，其中最常用的是`InnoDB`和`MyISAM`

| 存储引擎 | 描述                           |
| -------- | ------------------------------ |
| InnoDB   | 具备外键支持功能的事务处理引擎 |
| MyISAM   | 主要的非事务处理存储引擎       |

查看空的存储引擎

```sql
SHOW ENGINES;
```

### 表的磁盘存储方式

每次创建表时，MySQL都会创建一个磁盘文件，用于保存该表的格式（即它的定义）。该文件名与表名一致，后缀是`.frm`。个别存储引擎还可能会为表再创建接特定的文件，用于存储表的内容。

| 存储引擎 | 磁盘文件                |
| -------- | ----------------------- |
| InnoDB   | .ibd（数据和索引）      |
| MyISAM   | .MVD(数据) .MYI(索引)   |
| CSV      | .CSV(数据) .CSM(元数据) |

### InnoDB

`InnoDB`是MySQL的默认存储引擎，它具有以下功能：

-   其表在执行提交和回滚操作时是事务安全的。
-   在系统崩溃后可以自动回复。
-   外键和引用完整性支持，包括级联删除和更新。

额。。。。。目前还不懂，所以也就不深究了

## 表的CRUD

### 创建表

```sql
CREATE Table user(
  name VARCHAR(20) ,
  birth DATE NOT NULL ,
  weight INT(11) ,
  gender ENUM('F','M')
)
```

**指定存储引擎**

在创建表的时候，我们可以为它指定存储引擎。

```sql
CREATE Table user(
  name VARCHAR(20) ,
  birth DATE NOT NULL ,
  weight INT(11) ,
  gender ENUM('F','M')
)ENGINE=InnoDB;
# 引擎名字不区分大小写
```

#### 创建临时表

在创建表的时候加上`TEMPORARY`关键字，那么服务器讲创建出一个临时表

```sql
CREATE TEMPORARY TABLE user(
  name VARCHAR(20) ,
  birth DATE NOT NULL ,
  weight INT(11) ,
  gender ENUM('F','M')
)ENGINE=InnoDB;
```

注意：

-   与服务器终止连接后，临时表将会消失
-   临时表只对创建该表的客户端可见
-   如果临时表与永久表的名字相同，那么在临时表消失之前，所有操作都会用在临时表身上

#### 根据其他表或查询结果来创建表

MySQL提供了两条语句，可用于根据其它表或根据查询结果创建新表。

-   `CREATE TABLE ... LIKE`

    根据原有表创建一个新表，该表是原有表的一个空副本，会把原有表的结构丝毫不差的复制过来。

    ```sql
    CREATE TABLE new_user LIKE user;
    ```

-   `CREATE TABLE ... SELECT`

    根据查询结果创建一个新表,该表不会复制所有的列属性，如`AUTO_INCREMENT`列就不会被复制

    ```sql
    CREATE TABLE new_user SELECT * FROM user;
    ```

    注意：新的列会根据你所选择的列来命名，如果某个列是以表达式的计算结果生成的，那么该列的名字就是该表达式的问题表示，例如

    ```sql
    mysql> CREATE TABLE mytbl SELECT PI()*2;
    mysql> SELECT * FROM mytbl;
    +----------+
    | PI()*2   | # 注意这个列名
    +----------+
    | 6.283185 |
    +----------+
    ```

    为了避免这个状况，我们可以使用别名

    ```sql
    mysql> CREATE TABLE mytbl SELECT PI()*2 AS mycol;
    ```

    特别是多表查询时，如果不同的表具有相同列，如果不指定列别名，那么就会报错。

    前面说过这种方式创建的表不会复制所有的列属性，如果想解决这个问题，请查看`cast()`函数的内容。

#### 使用分区表

MySQL支持表分区，让表的内容分散存储在不同的物理存储位置。

假设你想要创建一个表，用于存储日期和性别的数据，并且假设有累计多年的数据需要加载到这个表里。那么我们可以根据日期，使用范围分区的方式，按年份讲各行分配到某个给定的分区。

```sql
CREATE TABLE log_partition (
  dt   DATETIME     NOT NULL,
  info VARCHAR(100) NOT NULL,
  INDEX (dt)
)PARTITION BY RANGE (YEAR(dt)) (
  PARTITION p0 VALUES LESS THAN (2010),
  partition p1 VALUES LESS THAN (2011),
  partition p2 VALUES LESS THAN (2012),
  partition p3 VALUES LESS THAN (2013),
  partition PMAX VALUES LESS THAN MAXVALUE
);
```

当2014过完后

```sql
ALTER TABLE log_partition
REORGANIZE PARTITION pamx
INTO (
  partition p4 VALUES LESS THAN (2014),
  partition pamx VALUES LESS THAN MAXVALUE
);
```

默认情况下，MySQL会将分区存储在专属于分区表的数据库目录里。如果想存储分散到其他地方，则需要分区选项`DATA_DIRECTORY`和`INDEX_DIRECTORY`，具体使用方式上网查询。

### 删除表

```sql
DROP TABLE table_name;
DROP TABLE t1,t2;
DROP TABLE IF EXISTS table_name;
DROP TEMPORARY TABLE IF EXISTS table_name;
```

### 查看表

```sql
SHOW CREATE TABLE table_name；
SHOW TABLE STATUS;
DESC TABLE table_name;
```

### 修改表结构

我们可以通过`ALTER TABLE`语句来对表结构进行修改。

```sql
ALTER TABLE tb_name1 action [,action] ...;
```

其中的每个`action`是指对表所做的修改。

#### 改变列的数据类型

```sql
ALTER TABLE user MODIFY password int(11);
ALTER TABLE user CHANGE password password varchar(50);
# 为什么使用CHANGE需要写两次列名？
# 因为CHANGE不仅能改变列的数据结构，还能改变列名
```

>   修改列的数据类型的一个重要原因是，提高多表连接的查询效率。索引经常用于两个相似类型之间的连接比较，如果两个类型完全相同，比较速度会更快。

#### 让表使用另一种存储引擎

```sql
ALTER TABLE user ENGINE=InnoDB;
```

#### 重新命名表

```sql
ALTER TABLE user  RENAME TO old_user; 
RENAME TABLE user TO old_user,table2 TO old_table2;
```

#### 表的转移

```sql
ALTER TABLE db1.user RENAME TO db2.user; 
RENAME TABLE db1.user TO db2.user;
```

[返回头部](#数据库的CRUD)

# 数据库元数据

## SHOW语句

```sql
SHOW DATABASES;
SHOW CREATE DATABASE db_name;

SHOW TABLES;
SHOW TABLE STATUS;
SHOW CREATE TABLE table_name;

SHOW COLUMNS FROM table_name;
SHOW INDEX FROM table_name;
```

## INFORMATION_SCHEMA

`INFORMATION_SCHEMA`是一个数据库。查看该数据库具有哪些表

```sql
SHOW TABLES IN INFORMATION_SCHEMA;
```

[返回头部](#数据库的CRUD)

# CRUD

## SELECT

SELECT的基本语法

```sql
SELECT select_list
FROM table_list
WHERE row_constraint
GROUP BY grouping_columns # GROUP BY sex;
ORDER BY sorting_columns # ORDER BY last_name DESC, first_name ASC;
HAVING group_constraint
LIMIT count;
```

奇怪..... 我之前的笔记呢...........

[返回头部](#数据库的CRUD)

# 多表操作

## 多表检索

### 1.使用连接实现多表检索

连接（join）

#### 内连接

一个表的的所有行与另一个表的所有行，排列组合。

我们可以通过ON子句来添加排列组合的规则,从而减少排列组合的结果数

```sql
SELECT * FROM t1 INNER JOIN t2 ON t1.i1 = t2.i2
```

#### 左（外）连接和右（外）连接

内连接只会显示在连接表里都匹配上的行。外连接除了显示同样的结果，还可以把其中一个表在另一个表没有匹配的行也显示出来。左连接就是把左表里没有匹配的内容也显示出来。

```sql
SELECT *
FROM t1
       LEFT JOIN t2 on t1.i1 = t2.i2
ORDER BY t1.i1 ASC;
```

### 2.使用子查询实现多表检索

子查询：用括号括起来，并嵌入另一条语句里的那条SELECT语句。

```sql
SELECT * FROM socre WHERE event_id IN (SELECT event_id FROM grade_event WHERE category = 'T');
```

子查询可以返回各种不同类型的信息

-   标量子查询：返回一个值
-   列子查询：返回一个由一个值或多个值构成的列
-   行子查询：返回一个由一个值或多个值构成的行
-   表子查询：返回一个由一个行或多个行组成的表

#### IN 和 NOT IN 子查询

```sql
# 查询逃课学生
SELECT * FROM student WHERE student_id IN (SELECT student_id FROM absence);
# 查询全勤学生
SELECT * FROM student WHERE student_id NOT IN (SELECT student_id FROM absence);
```

#### ALL、ANY和SOME子查询

```sql
# 外查询找出比子查询的到的生日都要小的生日
SELECT last_name ,first_name birth FORM persident WHERE birth <= ALL (SELECT birth FROM president);

SELECT last_name ,first_name birth FORM persident WHERE birth <= ANY (SELECT birth FROM president);

SELECT last_name ,first_name , state , city FORM persident WHERE (state,city) = ANY (SELECT state , city FORM persident WHERE last_name = 'Roosevelt');
```

#### EXISTS 和 NOT EXISTS 子查询

这两个运算符只会测试某个子查询是否返回了行。如果有返回，EXISTS结果为真。

#### FORM子句里的子查询

```sql
SELECT * FROM (SELECT 1,2) AS t1 INNER JOIN (SELECT 3,4) AS t2;
```

### 3.使用UNION实现多表检索

如果想把多个查询的结果合并成一个结果集，那么需要使用`UNION`语句。

```sql
SELECT i FROM t1 UNION SELECT j FROM t2 UNION  SELECT k FROM t3;
```

#### UNION的特性

-   列名和数据类型

    `UNION`结果集里的列名来自第一个SELECT里的列名。UNION选取的列数，必须跟第一个SELECT选取的列数相同。各列不需要名字相同，甚至数据结构也不需要相同，MySQL会进行必要的类型转换。

-   重复行处理

    默认情况下，UNION会将结果集里的重复行剔除掉

    如果想保留重复的行，则需要把UNION 改为 UNION ALL

-   ORDER BY 和 LIMIT处理

    如果想对UNION结果作为一个整体进行排序，那么需要用括号把每个SELECT语句括起来，并在最后加上ORDER BY子句。ORDER BY子句必须引用第一个SELECT语句选中的列名。

    ```sql
    (SELECT i FROM t1) UNION ALL (SELECT j FROM t2) UNION ALL (SELECT k FROM t3) 
    ORDER BY i;
    ```

## 多表删除

在编写涉及多表的DELETE语句时，需要把所有涉及的表全部列在FROM子句里，并把用来匹配表中各行的检索条件写在WHERE子句里。

```sql
DELETE FROM t1 INNER JOIN t2 ON t1.id=t2.id;
DELETE FROM t1 INNER JOIN t2 ON t1.id = t2.id;
DELETE FROM t1 LEAF JOIN t2 ON t1.id = t2.id WHERE t2.id IS NULL;

DELETE FROM t1 USING t1 INNDER JOIN t2 ON t1.id = t2.id;
DELETE FROM t1,t2 USING t1 INNDER JOIN t2 ON t1.id = t2.id;
DELETE FROM t1 USING t1 LEFT JOIN t2 ON t1.id = t2.id WHERE t2.id IS NULL;
```



## 多表更新

```sql
UPDATE score, grade_event SET score.score = score.score + 1
WHERE score.event_id = grade_event.event_id
AND grade_event.date = '2012-09-23' AND grade_event.category = 'Q';

UPDATE t1, t2 SET t2.a = t1.a WHERE t2.id = t1.id;
```

[返回头部](#数据库的CRUD)



# 练习

```mysql
# 工资第n大
SELECT DISTINCT Salary FROM Employee ORDER BY Salary DESC LIMIT M, 1;
```

