# 基本表达式

- `${...}` ：变量表达式。

- `*{...}` ：选择表达式。

- `#{...}` ：消息（i18n）表达式。

  消息表达式允许我们从外部源（`.properties`文件）检索特定于语言环境的消息，通过键引用它们应用一组参数。

- `@{...}` ：链接（URL）表达式。

- `~{...}` ：片段表达式。

```html
<!--变量表达式-->
<span th:text="${book.author.name}">
<!--选择表达式 将在先前选择的对象上执行-->
<div th:object="${book}">
  ...
  <span th:text="*{title}">...</span>
  ...
</div>
```

# 基本属性

```xml
<p th:text="#{msg.welcome}">Welcome everyone!</p>
<li th:each="book : ${books}" th:text="${book.title}">En las Orillas del Sar</li>
<form th:action="@{/createOrder}">
<input type="button" th:value="#{form.submit}" />
<a th:href="@{/admin/users}">
```

# Thymeleaf读取SpringMVC数据

## Model

### 设置数据

```java
    @RequestMapping(value = "message", method = RequestMethod.GET)
    public String messages(Model model) {
        model.addAttribute("messages", messageRepository.findAll());
        return "message/list";
    }
```

```java
    @RequestMapping(value = "message", method = RequestMethod.GET)
    public ModelAndView messages() {
        ModelAndView mav = new ModelAndView("message/list");
        mav.addObject("messages", messageRepository.findAll());
        return mav;
    }
```

```java
    @ModelAttribute("messages")
    public List<Message> messages() {
        return messageRepository.findAll();
    }
```

### 读取数据

```html
    <tr th:each="message : ${messages}">
        <td th:text="${message.id}">1</td>
        <td><a href="#" th:text="${message.title}">Title ...</a></td>
        <td th:text="${message.text}">Text ...</td>
    </tr>
```

## 请求参数

```java
    @Controller
    public class SomeController {
        @RequestMapping("/")
        public String redirect() {
            return "redirect:/query?q=Thymeleaf+Is+Great!";
        }
    }
```

```html
<!--需要用param修饰-->    
<p th:text="${param.q}">Test</p>
<!--如果参数q不存在，空字符串将显示在上面的段落中，否则q将显示值-->
```

由于参数可以是多值的（例如`https://example.com/query?q=Thymeleaf%20Is%20Great !&q=Really%3F`），您可以使用括号语法访问它们：

```html
    <p th:text="${param.q[0] + ' ' + param.q[1]}" th:unless="${param.q == null}">Test</p>
```

## session

```java
    @RequestMapping({"/"})
    String index(HttpSession session) {
        session.setAttribute("mySessionAttribute", "someValue");
        return "index";
    }
```

```html
  <p th:text="${session.mySessionAttribute}" th:unless="${session == null}">[...]</p>
```

## ServletContext属性

ServletContext属性在请求和会话之间共享。

要在Thymeleaf中访问ServletContext属性，您可以使用`#servletContext.`前缀：

```html
<table>
	<tr>
		<td>My context attribute</td>
        <!-- Retrieves the ServletContext attribute 'myContextAttribute' -->
        <td th:text="${#servletContext.getAttribute('myContextAttribute')}">42</td>
    </tr>
    <tr th:each="attr : ${#servletContext.getAttributeNames()}">
        <td th:text="${attr}">javax.servlet.context.tempdir</td>
        <td th:text="${#servletContext.getAttribute(attr)}">/tmp</td>
    </tr>
</table>
```

## SpringBean

Thymeleaf允许使用`@beanName`语法访问在Spring Application Context中注册的bean ，例如：

```java
    @Configuration
    public class MyConfiguration {
        @Bean(name = "urlService")
        public UrlService urlService() {
            return () -> "domain.com/myapp";
        }
    }

    public interface UrlService {
        String getApplicationUrl();
    }
```

```html
    <div th:text="${@urlService.getApplicationUrl()}">...</div> 
```

