# Spring Web MVC 框架

SpringMVC是一个实现MVC设计模式的框架.通过@Controller和@RestController注解创建Bean来处理HTTP请求.

下面代码展示了如何使用@RestController返回JSON数据.

```java
@RestController
@RequestMapping(value="/users")
public class MyRestController {

	@RequestMapping(value="/{user}", method=RequestMethod.GET)
	public User getUser(@PathVariable Long user) {
		// ...
	}

	@RequestMapping(value="/{user}/customers", method=RequestMethod.GET)
	List<Customer> getUserCustomers(@PathVariable Long user) {
		// ...
	}

	@RequestMapping(value="/{user}", method=RequestMethod.DELETE)
	public User deleteUser(@PathVariable Long user) {
		// ...
	}

}
```

Spring MVC 是核心Spring框架的一部分,它的具体详细可以查看[官方文档](https://docs.spring.io/spring/docs/5.1.0.RC2/spring-framework-reference/web.html#mvc). [spring.io/guides](https://spring.io/guides)网页提供了一些关于SpringMVC的Demo.

## 1自动配置Spring MVC

Spring Boot对SpringMVC进行了以下配置.

- 配置了`ContentNegotiatingViewResolver` 和 `BeanNameViewResolver` 两个Bean.
- 支持提供静态资源包括WebJars.
- 自动注册`Converter`, `GenericConverter`, 和 `Formatter` 三个Bean.

- 支持HttpMessageConverters
- 自动注册 HttpMessageConverters
- 静态 index.html 支持
- 自定义Favicon
- 自动使用ConfigurableWebBindingInitializer Bean

如果你想额外添加MVC配置,你可以定义一个类,使用@Configuration修饰并且继承WebMvcConfiguer类.

如果你想完全取代Spring Boot为SpringMVC写的配置,你只需要在你的配置类上添加@Configuration以及@EnableWebMvc注解.

如果你想提供自定义的`RequestMappingHandlerMapping`, `RequestMappingHandlerAdapter`,  `ExceptionHandlerExceptionResolver`实例 ,你可以获取WebMvcRegistrationsAdapter实例,通过该实例注册实例.

### 这里需要写例子

## 2Http信息转换器

Http信息转换器,HttpMessageConverters.

SpringMVC使用Http信息转换器接口转换HTTP请求和响应.例如对象会被自动转换成JSON或XML.默认字符编码为UTF8.

如果你需要添加自定义的转换器,你可以使用Spring Boot的HttpMessageConverters类

```java
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.*;

@Configuration
public class MyConfiguration {

	@Bean
	public HttpMessageConverters customConverters() {
		HttpMessageConverter<?> additional = ...
		HttpMessageConverter<?> another = ...
		return new HttpMessageConverters(additional, another);
	}

}
```

Context中存在的任何HttpMessageConverter bean都将添加到转换器列表中。 您可以通过相同的方式覆盖默认转换器。

## 3自定义Json序列化器和反序列化器

如果你使用Jackson来对JSON数据进行序列化和反序列化,你可能想要使用自己的`JsonSerializer` 和 `JsonDeserializer` 类.

自定义序列化类通常通过模块向Jackson注册，但Spring Boot提供了另一种@JsonComponent注释，可以更容易地直接注册Spring Beans。

您可以直接在JsonSerializer或JsonDeserializer实现类上使用@JsonComponent注释。 您还可以在包含序列化器/反序列化器作为内部类的类上使用它.

```java
import java.io.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.boot.jackson.*;

@JsonComponent
public class Example {

	public static class Serializer extends JsonSerializer<SomeObject> {
		// ...
	}

	public static class Deserializer extends JsonDeserializer<SomeObject> {
		// ...
	}

}
```

## 4MessageCodesResolver

我暂时也不太懂,我的理解就是用来读取properties文件中的内容,作为验证错误时,输出的信息.

## 5静态内容

默认情况下，Spring Boot从类路径中的/ static（或/ public或/ resources或/ META-INF / resources）目录或ServletContext的根目录中提供静态内容。它使用SpringMVC的ResourceHttpRequestHandler实现该效果.因此你可以通过添加你自己的WebMvcConfigurer然后重写addResourceHandlers方法来修改它的行为.

默认情况下，资源映射到/ **，但您可以使用spring.mvc.static-path-pattern属性对其进行调整.

例如，可以按如下方式将所有资源重新定位到/ resources / **：

```properties
spring.mvc.static-path-pattern=/resources/**
```

您还可以使用spring.resources.static-locations属性自定义静态资源位置.

除了上面提到的"标准"的静态资源,我们还有一种特殊的静态资源,叫做Webjars.

Spring Boot还支持Spring MVC提供的高级资源处理功能，如禁止缓存静态资源和忽略Webjars版本。

禁止缓存静态资源

```properties
spring.resources.chain.strategy.content.enabled=true
spring.resources.chain.strategy.content.paths=/**
```

由于为Thymeleaf和FreeMarker自动配置了ResourceUrlEncodingFilter，因此在运行时模板中资源链接会被重写。

如果要从url中忽略Webjars的版本,引入webjars-locator-core依赖.

动态加载资源时,例如使用JavaScript模块加载器，不能重命名文件。

我也看不太懂,不过此功能已在专门的[博客文章](https://spring.io/blog/2014/07/24/spring-framework-4-1-handling-static-web-resources) 和Spring Framework参考文档中进行了详细描述。

## 6

## 11 错误处理

默认的Spring Boot提供/error映射，以合理的方式处理所有错误，并在servlet容器中注册为“全局”错误页面。

对于机器客户,它返回JSON格式的信息,包括错误的细节,HTTP状态码,以及异常信息。

对于浏览器客户,它返回HTML页面。

如果想完全取代默认行为,可以实现`ErrorController` 接口然后注册实现类的Bean,或者添加一个`ErrorAttributes` 类型的Bean,从而实现使用现有的机制来展示我们自定义的内容。

> BasicErrorController类是ErrorController接口的实现类,我们可以通过继承它,然后改写里面的方法从而得到我们想要的结果。

你还可以使用@ControllerAdvice修饰一个类,来为某个Controller定义应该返回什么样的JSON文档。

```java
@ControllerAdvice(basePackageClasses = AcmeController.class)
public class AcmeControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(YourException.class)
	@ResponseBody
	ResponseEntity<?> handleControllerException(HttpServletRequest request, 
                                                Throwable ex) {
		HttpStatus status = getStatus(request);
		return new ResponseEntity<>(
            new CustomErrorType(status.value(), ex.getMessage()),
            status
        );
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.valueOf(statusCode);
	}

}
```

在上面的例子中,如果AcmeController抛出了YourException 异常,则会返回CustomErrorType类型对应的JSON数据。

### 自定义错误页面

如果你想自定义错误页面,你可以在/error目录下添加错误页面。错误页面即可以是静态HTML也可以是通过模板引擎创建出来的文件。

举个栗子,通过静态HTML文件映射404错误的

```
src/
 +- main/
     +- java/
     |   + <source code>
     +- resources/
         +- public/
             +- error/
             |   +- 404.html
             +- <other public assets>
```

通过FreeMarker 模板映射5xxx错误

```
src/
 +- main/
     +- java/
     |   + <source code>
     +- resources/
         +- templates/
             +- error/
             |   +- 5xx.ftl
             +- <other templates>
```

如果想要更加复杂的映射,你可以添加一个实现了ErrorViewResolver接口的Bean。

```java
public class MyErrorViewResolver implements ErrorViewResolver {

	@Override
	public ModelAndView resolveErrorView(HttpServletRequest request,
			HttpStatus status, Map<String, Object> model) {
		// Use the request or status to optionally return a ModelAndView
		return ...
	}

}
```

您还可以使用常规的Spring MVC功能，例如@ExceptionHandler方法和@ControllerAdvice。 然后，ErrorController将获取任何未处理的异常。

### 非Spring MVC框架下错误页面映射

有时候应用可能使用的不是Spring MVC,你可以使用ErrorPageRegistrar接口直接注册ErrorPages。

这种方式直接与底层嵌入式servlet容器一起工作，即使你没有Spring MVC DispatcherServlet也可以工作。

```java
@Bean
public ErrorPageRegistrar errorPageRegistrar(){
	return new MyErrorPageRegistrar();
}

// ...

private static class MyErrorPageRegistrar implements ErrorPageRegistrar {

	@Override
	public void registerErrorPages(ErrorPageRegistry registry) {
		registry.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/400"));
	}

}
```

**尚未完成.......................**