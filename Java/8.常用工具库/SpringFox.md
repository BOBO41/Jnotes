# 简介

Swagger：一个API开发框架，简单来说就是自动生成API文档，并提供测试方法。

SpringFox：由于Spring很流行，Marty Pitt编写了一个基于Spring的组件swagger-springmvc，用于将swagger集成到springmvc中来。而springfox则是从这个组件发展而来，同时springfox也是一个新的项目。

工作原理：

1. 通过springfox-swagger2生成OAS文件，OAS本身是一个API规范，它用于描述一整套API接口。
2. springfox-swagger-ui根据OAS文件生成API阅读界面



# springfox-swagger-ui

1. 依赖
2. 配置
3. 编写API
4. 常用注解

## 依赖

```xml
<!--核心依赖-->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>

<!--用于生成API阅读界面的类库-->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>

<!--原生的UI界面有点丑，使用其他UI-->
<dependency>
	<groupId>com.github.xiaoymin</groupId>
	<artifactId>swagger-bootstrap-ui</artifactId>
	<version>1.9.0</version>
</dependency>
```

## 配置

```java
@SpringBootApplication
@EnableConfigurationProperties({SwaggerConfig.class})
public class SpringfoxApplication {

   public static void main(String[] args) {
      SpringApplication.run(SpringfoxApplication.class, args);
   }

}
```

```yml
swagger:
  title: learn springfox
  description: 学习springfox
  version: 1.0.0
  name: 黄大仁
  url: https://github.com/huangdaren1997
  email: huangdaren1997@gmail.com
```

```java
@Configuration
@EnableSwagger2
@ConfigurationProperties("swagger")
@Getter @Setter
public class SwaggerConfig {

   private String title;
   private String description;
   private String version;
   private String name;
   private String url;
   private String email;

   @Bean
   public Docket customDocket() {
      return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo());
   }

   private ApiInfo apiInfo() {
      Contact contact = new Contact(name, url, email);
      return new ApiInfoBuilder()
            .title(title)
            .description(description)
            .contact(contact)
            .version(version)
            .build();
   }
}
```

## 编写API

```java
@RestController
@Api(tags = "用户模块")
public class UserController {

   @ApiOperation(value = "获取用户信息")
   @GetMapping("/user")
   public String userInfo() {
      return "hdr huangdaren1997@gmail.com";
   }
}
```

## 常用注解

- @Api
- @ApiOperation
- @ApiImplicitParams
  - @ApiImplicitParam
- @ApiResponses
  - @ApiResponse
- @ApiModel
- @ApiModelProperty

# Springfox Support for JSR-303

# springfox-data-rest

