# JDBC基础结构

JDBC提供了统一的API来对数据库进行操作。JDBC的核心是驱动类（Driver），驱动类由具体的数据库供应商所提供。当驱动被载入后，它会通过`java.sql.DriverManager `进行注册，`DriverManager`负责管理驱动以及提供静态方法建立与数据库的连接。它的`getConnection`方法会返回`java.sql.Connection`接口的实现类，该实现类也是由数据库供应商实现，通过该实现类对数据库运行SQL语句。

# SpringJDBC基础结构

## 数据库连接和数据源

通过注册一个实现了`javax.sql.DataSource`接口的Bean，Spring可以为我们管理数据库连接（`database connection`）。数据源（`DataSource`）和连接（`Connection`）的区别在于，前者提供以及管理后者。

`org.springframework.jdbc.datasource.DriverManagerDataSource`是最简单的实现类，通过类名你就可以猜出，它是利用`DriverManager`来获取`Connection`。事实上，`DriverManagerDataSource`并不支持数据库连接池，所以它只能用来做测试使用。

```java
@Configuration
@PropertySource("classpath:db/jdbc2.properties")
public class DbConfig {
	@Value("${driverClassName}")
	private String driverClassName;
	@Value("${url}")
	private String url;
	@Value("${username}")
    private String username;
	@Value("${password}")
	private String password;

	@Lazy
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}
} 
```

在实际生产中，你可以使用Apache Commons `BasicDataSource` 或者其它。

另一种配置`DataSource Bean`的方法是使用JNDI。未完待续

## 内置数据库支持

从Spring3.0开始，Spring提供内置数据库支持，下面是配置内置数据库的一些代码

```java
@Configuration
public class EmbeddedJdbcConfig {
	private static Logger logger = LoggerFactory.getLogger(EmbeddedJdbcConfig.class);

	@Bean
    public DataSource dataSource() {
		try {
			EmbeddedDatabaseBuilder dbBuilder = new EmbeddedDatabaseBuilder();
			return dbBuilder.setType(EmbeddedDatabaseType.H2)
                .addScripts("classpath:db/h2/schema.sql", 
                            "classpath:db/h2/test-data.sql")
                .build();
		} catch (Exception e) {
			logger.error("Embedded DataSource bean cannot be created!", e);
			return null;
		}
	} 
    // ...
}
```

## DAO模式

Data Access Object 模式由以下几个组件组成：

- DAO接口
- DAO接口实现类
- 模型对象（Model Object）也叫作（data object、entities）

