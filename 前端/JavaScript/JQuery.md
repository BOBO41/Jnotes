## 1.选择器

**CSS选择器**

| 选择器     | 语法          | 描述 |
| ---------- | ------------- | ---- |
| Id选择器   | #ID{}         |      |
| 类选择器   | E.className{} |      |
| 群组选择器 | E1,E2,E3{}    |      |
| 后代选择器 | E F{}         |      |
| 通配选择符 | *{}           |      |

**JQuery选择器**

基本选择器、层次选择器、过滤选择器、表单选择器。

### 基本选择器

通过元素Id、class、标签名等查找DOM元素。

| 实例                    | 返回     | 描述                            |
| ----------------------- | -------- | ------------------------------- |
| $("#test")              | 单个元素 | 选取id为test的元素              |
| $(".test")              | 元素集合 | 选取所有class为test的元素       |
| $("p")                  | 元素集合 | 选取所有p标签元素               |
| $("*")                  | 元素集合 | 选取所有元素                    |
| $("div,span,p.myClass") | 元素集合 | 选取div、span、class=myClass的p |

### 层次选择器

通过DOM元素之间的层次关系获取特定元素，例如后代元素、子元素、相邻元素、兄弟元素。

| 实例            | 返回     | 描述                                          |
| --------------- | -------- | --------------------------------------------- |
| $("div span")   | 集合元素 | 选div中的所有span元素                         |
| $("div > span") | 集合元素 | 选div中span子元素                             |
| $(".one + div") | 集合元素 | 选取class为one的下一个div元素（注意是下一个） |
| $("#two~div")   | 集合元素 | 选取id为two的元素后面所有div兄弟元素          |

### 过滤选择器

#### 基本过滤选择器

#### 内容过滤选择器

#### 可见性过滤选择器

#### 属性过滤选择器

#### 子元素过滤选择器

#### 表单对象属性过滤选择器

### 表单选择器

| 实例           | 返回     | 描述 |
| -------------- | -------- | ---- |
| $(":input")    | 集合元素 |      |
| $(":text")     | 集合元素 |      |
| $(":password") | 集合元素 |      |
| $(":radio")    | 集合元素 |      |
| $(":checkbox") | 集合元素 |      |
| $(":submit")   | 集合元素 |      |
| $(":image")    | 集合元素 |      |
| $(":reset")    | 集合元素 |      |
| $(":button")   | 集合元素 |      |
| $(":file")     | 集合元素 |      |
| $(":hidden")   | 集合元素 |      |

## 2.JQuery操作DOM

**获取元素节点中的属性**

```javascript
$("input").attr("nmae");
```

### 创建节点

```javascript
$("<li></li>");
$("<li>雪梨</li>")
$("<li title='雪梨'>雪梨</li>")
```

### 插入节点

```javascript
$("p").append($li);
($li).appendTo("p");
# prepend prependTo
# after insertAfter before insertBefore
```

### 删除节点

```javascript
$("ul li:eq(1)").remove(); # 删除所有匹配的节点
$("ul li").remove("li[title!=菠萝]");
$("ul li:eq(1)").empty(); # 清空所有后代元素
```

### 复制节点

```javascript
$("ul li").click(function(){
  $(this).clone().appendTo("ul"); # clone(true) 也复制行为
});
```

### 替换节点

```javascript
$("p").replaceWith("<div>hello</div>");
$("<div>hello</div>").replaceAll("p");
```

### 包裹节点

```javascript
$("strong").wrap("<b></b>"); # 用b标签包裹strong标签 (一对一)
$("strong").wrapAll("<b></b>"); # 用一个b标签包裹所有strong标签 (一对多)
$("strong").wrapInner("<b></b>"); # 用b标签包裹strong标签的内部
```

### 属性操作

```javascript
$("strong").attr(attrName,attrValue);
$("strong").removeAttr(attrName);
# attr操作是替换或删除原来的属性
# 对于像class这种具有多个属性值的 JQuery提供了addClass()方法
$("p").addClass("another");
$("p").removeClass("another");
$("p").removeClass("another high");
$("p").hasClass("another");
$("p").toggleClass("another");
```

### 设置和获取HTML、文本和值

```javascript
# <p>我有一只<strong>小毛驴</strong></p>
$("p").html(); # 我有一只<strong>小毛驴</strong>
$("p").text(); # 我有一只小毛驴
$("input").val("请输入邮箱密码"); # 用于操作标签中的value属性
$("input").focus(function(){ # focus
  console.log($(this).attr("name")+"focus");
})

$("input").blur(function(){ # blur
  console.log($(this).attr("name")+"blur");
})
```

### 遍历节点

```javascript
$("p").children(); # 遍历子节点
$("p").prev();
$("p").next();
$("p").siblings();
$("p").closest("li"); # 符合规则的临近节点
```

### CSS-DOM操作

```javascript
$("p").css("color","read");
$("p").css({"font-size":"30px","background-color":"#888888"});
```

## 3.Jquery中的事件

```javascript
$(document).ready() # 等DOM加载完毕
window.onload # 等待页面所有元素加载完毕
$("img").load(function()){} # 等图片加载完毕后执行函数
# bind(type,[,data],fn);
$("p").bind("click",function(){
  consolo.log("hello");
});
```

**事件冒泡**

```javascript
$("span").bind("click",function(event){
  // 传递事件对象
  
  event.stopPropagtion(); # 阻止事件冒泡
  event.preventDefault(); # 阻止默认行为
});
```

## 4.jQuery中的动画

```javascript
show() hide() # display 
fadeIn() fadeOut()
```

