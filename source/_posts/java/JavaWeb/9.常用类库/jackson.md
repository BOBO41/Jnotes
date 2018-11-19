---
title : Jackson
categories : 
- JavaWeb
- 常用类
date : 2018-11-1
---

# Jackson

## 什么是Jackson

Jackson是一个Java应用库，Jackson可以轻松的将Java对象转换成json对象和xml文档，同样也可以将json、xml转换成Java对象。

## Jackson的三种使用方法

**jackson提供了三种处理JSON的方式**

- Streaming API

  使用org.codehaus.jackson.JsonParser解析Json，使用org.codehaus.jackson.JsonGenerator生成Json

- Tree Model 

  使用org.codehaus.jackson.map.ObjectMapper构建树，节点是JsonNode，树模型类似于XML的DOM模型

- Data Binding 使用属性方法或者注解实现JSON和POJOs之间的相互转换

  - 完全对象绑定（full data binding）
    - 用来在JSON和java bean对象之间的转换，同样适用于java内置对象
  - 简单对象绑定（simple data binding）
    - 用来在JSON和java的Maps, Lists, Strings, Numbers, Booleans, and null之间转换

**各自的优势**

- Streaming API性能最好（最低的消耗、最快的读写速度、其他两种方法的基础） 
- Tree Model 灵活
- Data Binding 好用、便利 

## 例子

### Full Data Binding (POJO) 

```json
{
  "name" : { "first" : "Joe", "last" : "Sixpack" },
  "gender" : "MALE",
  "verified" : false,
  "userImage" : "Rm9vYmFyIQ=="
}
```

```java
public class User {
       // 名字 内部类
       public static class Name {
         private String _first, _last;

         public String getFirst() { return _first; }
         public String getLast() { return _last; }

         public void setFirst(String s) { _first = s; }
         public void setLast(String s) { _last = s; }
       }
	   // 性别 枚举类
       public enum Gender { MALE, FEMALE };
       // 实例变量
       private Gender _gender;
       private Name _name;
       private boolean _verified;
       private byte[] _userImage;

       public Name getName() { return _name; }
       public boolean isVerified() { return _isVerified; }
       public Gender getGender() { return _gender; }
       public byte[] getUserImage() { return _userImage; }

       public void setName(Name n) { _name = n; }
       public void setVerified(boolean b) { _isVerified = b; }
       public void setGender(Gender g) { _gender = g; }
       public void setUserImage(byte[] b) { _userImage = b; }
   }
```

```java
ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
// 读取json
User user = mapper.readValue(new File("user.json"), User.class); 
// 写入json
mapper.writeValue(new File("user-modified.json"), user);
```

### Simple Data Binding

如果我们没有或者不想创建一个单独的类来做JSON和POJO之间的转换，那么可以使用Simple Data Binding

JSON转换成java对象实例：

```java
Map<String,Object> userData = mapper.readValue(new File("user.json"), Map.class);
```

java对象实例转换成JSON:

```java
Map<String,Object> userData = new HashMap<String,Object>();
Map<String,String> nameStruct = new HashMap<String,String>();
nameStruct.put("first", "Joe");
nameStruct.put("last", "Sixpack");
userData.put("name", nameStruct);
userData.put("gender", "MALE");
userData.put("verified", Boolean.FALSE);
userData.put("userImage", "Rm9vYmFyIQ==");
mapper.writeValue(new File("user-modified.json"), userData);
```

你会发现在JSON转成Map.class的过程中没有指定Map的泛型类型，但是jackson也能正确的转换。
jackson有一个自己的转换关系，能够对json数据进行默认转换。默认转换关系如下：
JSON TypeJava TypeobjectLinkedHashMap

如果我们想指定具体的Key和Value类型，也是可以的。比如，想转换成Map

```java
Map<String,User> result = mapper.readValue(src, 
                                           new TypeReference<Map<String,User>>() { });
```

### Tree Model

树模型和XML的DOM方式类似。jackson会构建一课由JsonNode组成的树，里面的JsonNode暴露了一般需要用到的取值接口。当然，树里面的Node是JsonNode的子类，只有需要修改值的时候，你才有必要转换到子类型。

**把json文本读取到一棵树**

```java
ObjectMapper m = new ObjectMapper();
// can either use mapper.readTree(source), or mapper.readValue(source, JsonNode.class);
JsonNode rootNode = m.readTree(new File("user.json"));
// ensure that "last name" isn't "Xmler"; if is, change to "Jsoner"
JsonNode nameNode = rootNode.path("name");
String lastName = nameNode.path("last").getTextValue().
if ("xmler".equalsIgnoreCase(lastName)) {
 ((ObjectNode)nameNode).put("last", "Jsoner");
}
// and write it out:
m.writeValue(new File("user-modified.json"), rootNode);
```

**直接在内存创建一棵树**

```java
TreeMapper treeMapper = new TreeMapper();
ObjectNode userOb = treeMapper.objectNode();
Object nameOb = userRoot.putObject("name");
nameOb.put("first", "Joe");
nameOb.put("last", "Sixpack");
userOb.put("gender", User.Gender.MALE.toString());
userOb.put("verified", false);
byte[] imageData = getImageData(); // or wherever it comes from
userOb.put("userImage", imageData);
```

### Streaming API

很少用，这里不讲

## 注意：

当POJO里面包含isXXX这样的字段的时候，gson转出来的结果是is_xxx，但是jackson转出来的是xxx，也就是自动抹掉了is。解决办法有一个就是在对应属性上添加@JsonPropety(“is_xxx”)注解，指名属性名。就算如此，在一个些复杂的项目里面也会有其他的坑，所以尽量不要用isXXX来做属性名！！