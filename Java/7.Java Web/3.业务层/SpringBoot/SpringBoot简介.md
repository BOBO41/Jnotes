## 什么是Spring Boot

Spring Boot是一个为Spring实现约定优于配置的框架。

在Spring Boot之前，我们编写Spring应用需要进行大量的配置，这是一个很繁琐的过程，因此Spring社区推出了Spring Boot框架，它使开发人员从大量的配置中解放出来。

## 自动配置原理

### @SpringBootApplication

用来修饰一个类，说明该类是Spring配置类，同时触发自动配置以及组件扫描。

```java
@Target(ElementType.TYPE) // 只能用来修饰类
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration // 自己看看源码，没啥好说的
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {
      @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
      @Filter(type = FilterType.CUSTOM,
            classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {

   @AliasFor(annotation = EnableAutoConfiguration.class)
   Class<?>[] exclude() default {};

   @AliasFor(annotation = EnableAutoConfiguration.class)
   String[] excludeName() default {};

   @AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
   String[] scanBasePackages() default {};


   @AliasFor(annotation = ComponentScan.class, 
             attribute = "basePackageClasses")
   Class<?>[] scanBasePackageClasses() default {};

}
```

### @EnableAutoConfiguration

预测你需要使用的Bean，然后自动配置并注册到Spring容器中。

通常就是根据类路径中存在的依赖以及你所定义Bean来预测你需要使用哪些Bean。

例如你的类路径中存在`tomcat-embedded.jar`，而你自己又没有配置`TomcatServletWebServerFactory`，那么它就会帮你自动注册这个Bean。

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {

   String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";

   // Exclude specific auto-configuration classes such that they will never be applied.
   Class<?>[] exclude() default {};


   // Exclude specific auto-configuration class names such that they will never be
   String[] excludeName() default {};

}
```

### AutoConfigurationImportSelector.class

一个用来实现自动配置的`DeferredImportSelector`。

`DeferredImportSelector`是`ImportSelector`的扩展，只有当所有的`@Configuration`处理完以后才执行操作。

```javascript
public class AutoConfigurationImportSelector
      implements DeferredImportSelector, BeanClassLoaderAware, ResourceLoaderAware,
      BeanFactoryAware, EnvironmentAware, Ordered {

   private static final AutoConfigurationEntry EMPTY_ENTRY = new AutoConfigurationEntry();

   private static final String[] NO_IMPORTS = {};

   private static final Log logger = LogFactory
         .getLog(AutoConfigurationImportSelector.class);

   private static final String PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE 
       = "spring.autoconfigure.exclude";

   private ConfigurableListableBeanFactory beanFactory;

   private Environment environment;

   private ClassLoader beanClassLoader;

   private ResourceLoader resourceLoader;
```

```java
   // 找出需要
   @Override
   public String[] selectImports(AnnotationMetadata annotationMetadata) {
      if (!isEnabled(annotationMetadata)) {
         return NO_IMPORTS;
      }
       
      // AutoConfigurationMetadataLoader 用来读取AutoConfigurationMetadata的工具类
      AutoConfigurationMetadata autoConfigurationMetadata = 
          AutoConfigurationMetadataLoader.loadMetadata(this.beanClassLoader);
      //------------- loadMetadata ---------------//
      protected static final String PATH = "META-INF/"
			+ "spring-autoconfigure-metadata.properties";

	  public static AutoConfigurationMetadata loadMetadata(ClassLoader classLoader) {
		 return loadMetadata(classLoader, PATH);
	  }
      //-----------------------------------------//
       
      AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(
            autoConfigurationMetadata, annotationMetadata);
       
      return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
   }
```



```java
   /**
    * Return the {@link AutoConfigurationEntry} based on the {@link AnnotationMetadata}
    * of the importing {@link Configuration @Configuration} class.
    * @param autoConfigurationMetadata the auto-configuration metadata
    * @param annotationMetadata the annotation metadata of the configuration class
    * @return the auto-configurations that should be imported
    */
   protected AutoConfigurationEntry getAutoConfigurationEntry(
         AutoConfigurationMetadata autoConfigurationMetadata,
         AnnotationMetadata annotationMetadata) {
      if (!isEnabled(annotationMetadata)) {
         return EMPTY_ENTRY;
      }
      AnnotationAttributes attributes = getAttributes(annotationMetadata);
      List<String> configurations = getCandidateConfigurations(annotationMetadata,
            attributes);
      configurations = removeDuplicates(configurations);
      Set<String> exclusions = getExclusions(annotationMetadata, attributes);
      checkExcludedClasses(configurations, exclusions);
      configurations.removeAll(exclusions);
      configurations = filter(configurations, autoConfigurationMetadata);
      fireAutoConfigurationImportEvents(configurations, exclusions);
      return new AutoConfigurationEntry(configurations, exclusions);
   }

   @Override
   public Class<? extends Group> getImportGroup() {
      return AutoConfigurationGroup.class;
   }
   
   // 判断是否使用自动配置
   protected boolean isEnabled(AnnotationMetadata metadata) {
      if (getClass() == AutoConfigurationImportSelector.class) {
         return getEnvironment().getProperty(
               EnableAutoConfiguration.ENABLED_OVERRIDE_PROPERTY, Boolean.class,
               true);
      }
      return true;
   }

   /**
    * Return the appropriate {@link AnnotationAttributes} from the
    * {@link AnnotationMetadata}. By default this method will return attributes for
    * {@link #getAnnotationClass()}.
    * @param metadata the annotation metadata
    * @return annotation attributes
    */
   protected AnnotationAttributes getAttributes(AnnotationMetadata metadata) {
      String name = getAnnotationClass().getName();
      AnnotationAttributes attributes = AnnotationAttributes
            .fromMap(metadata.getAnnotationAttributes(name, true));
      Assert.notNull(attributes,
            () -> "No auto-configuration attributes found. Is "
                  + metadata.getClassName() + " annotated with "
                  + ClassUtils.getShortName(name) + "?");
      return attributes;
   }

   /**
    * Return the source annotation class used by the selector.
    * @return the annotation class
    */
   protected Class<?> getAnnotationClass() {
      return EnableAutoConfiguration.class;
   }

   /**
    * Return the auto-configuration class names that should be considered. By default
    * this method will load candidates using {@link SpringFactoriesLoader} with
    * {@link #getSpringFactoriesLoaderFactoryClass()}.
    * @param metadata the source metadata
    * @param attributes the {@link #getAttributes(AnnotationMetadata) annotation
    * attributes}
    * @return a list of candidate configurations
    */
   protected List<String> getCandidateConfigurations(AnnotationMetadata metadata,
         AnnotationAttributes attributes) {
      List<String> configurations = SpringFactoriesLoader.loadFactoryNames(
            getSpringFactoriesLoaderFactoryClass(), getBeanClassLoader());
      Assert.notEmpty(configurations,
            "No auto configuration classes found in META-INF/spring.factories. If you "
                  + "are using a custom packaging, make sure that file is correct.");
      return configurations;
   }

   /**
    * Return the class used by {@link SpringFactoriesLoader} to load configuration
    * candidates.
    * @return the factory class
    */
   protected Class<?> getSpringFactoriesLoaderFactoryClass() {
      return EnableAutoConfiguration.class;
   }

   private void checkExcludedClasses(List<String> configurations,
         Set<String> exclusions) {
      List<String> invalidExcludes = new ArrayList<>(exclusions.size());
      for (String exclusion : exclusions) {
         if (ClassUtils.isPresent(exclusion, getClass().getClassLoader())
               && !configurations.contains(exclusion)) {
            invalidExcludes.add(exclusion);
         }
      }
      if (!invalidExcludes.isEmpty()) {
         handleInvalidExcludes(invalidExcludes);
      }
   }

   /**
    * Handle any invalid excludes that have been specified.
    * @param invalidExcludes the list of invalid excludes (will always have at least one
    * element)
    */
   protected void handleInvalidExcludes(List<String> invalidExcludes) {
      StringBuilder message = new StringBuilder();
      for (String exclude : invalidExcludes) {
         message.append("\t- ").append(exclude).append(String.format("%n"));
      }
      throw new IllegalStateException(String
            .format("The following classes could not be excluded because they are"
                  + " not auto-configuration classes:%n%s", message));
   }

   /**
    * Return any exclusions that limit the candidate configurations.
    * @param metadata the source metadata
    * @param attributes the {@link #getAttributes(AnnotationMetadata) annotation
    * attributes}
    * @return exclusions or an empty set
    */
   protected Set<String> getExclusions(AnnotationMetadata metadata,
         AnnotationAttributes attributes) {
      Set<String> excluded = new LinkedHashSet<>();
      excluded.addAll(asList(attributes, "exclude"));
      excluded.addAll(Arrays.asList(attributes.getStringArray("excludeName")));
      excluded.addAll(getExcludeAutoConfigurationsProperty());
      return excluded;
   }

   private List<String> getExcludeAutoConfigurationsProperty() {
      if (getEnvironment() instanceof ConfigurableEnvironment) {
         Binder binder = Binder.get(getEnvironment());
         return binder.bind(PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE, String[].class)
               .map(Arrays::asList).orElse(Collections.emptyList());
      }
      String[] excludes = getEnvironment()
            .getProperty(PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE, String[].class);
      return (excludes != null) ? Arrays.asList(excludes) : Collections.emptyList();
   }

   private List<String> filter(List<String> configurations,
         AutoConfigurationMetadata autoConfigurationMetadata) {
      long startTime = System.nanoTime();
      String[] candidates = StringUtils.toStringArray(configurations);
      boolean[] skip = new boolean[candidates.length];
      boolean skipped = false;
      for (AutoConfigurationImportFilter filter : getAutoConfigurationImportFilters()) {
         invokeAwareMethods(filter);
         boolean[] match = filter.match(candidates, autoConfigurationMetadata);
         for (int i = 0; i < match.length; i++) {
            if (!match[i]) {
               skip[i] = true;
               candidates[i] = null;
               skipped = true;
            }
         }
      }
      if (!skipped) {
         return configurations;
      }
      List<String> result = new ArrayList<>(candidates.length);
      for (int i = 0; i < candidates.length; i++) {
         if (!skip[i]) {
            result.add(candidates[i]);
         }
      }
      if (logger.isTraceEnabled()) {
         int numberFiltered = configurations.size() - result.size();
         logger.trace("Filtered " + numberFiltered + " auto configuration class in "
               + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)
               + " ms");
      }
      return new ArrayList<>(result);
   }

   protected List<AutoConfigurationImportFilter> getAutoConfigurationImportFilters() {
      return SpringFactoriesLoader.loadFactories(AutoConfigurationImportFilter.class,
            this.beanClassLoader);
   }

   protected final <T> List<T> removeDuplicates(List<T> list) {
      return new ArrayList<>(new LinkedHashSet<>(list));
   }

   protected final List<String> asList(AnnotationAttributes attributes, String name) {
      String[] value = attributes.getStringArray(name);
      return Arrays.asList((value != null) ? value : new String[0]);
   }

   private void fireAutoConfigurationImportEvents(List<String> configurations,
         Set<String> exclusions) {
      List<AutoConfigurationImportListener> listeners = getAutoConfigurationImportListeners();
      if (!listeners.isEmpty()) {
         AutoConfigurationImportEvent event = new AutoConfigurationImportEvent(this,
               configurations, exclusions);
         for (AutoConfigurationImportListener listener : listeners) {
            invokeAwareMethods(listener);
            listener.onAutoConfigurationImportEvent(event);
         }
      }
   }

   protected List<AutoConfigurationImportListener> getAutoConfigurationImportListeners() {
      return SpringFactoriesLoader.loadFactories(AutoConfigurationImportListener.class,
            this.beanClassLoader);
   }

   private void invokeAwareMethods(Object instance) {
      if (instance instanceof Aware) {
         if (instance instanceof BeanClassLoaderAware) {
            ((BeanClassLoaderAware) instance)
                  .setBeanClassLoader(this.beanClassLoader);
         }
         if (instance instanceof BeanFactoryAware) {
            ((BeanFactoryAware) instance).setBeanFactory(this.beanFactory);
         }
         if (instance instanceof EnvironmentAware) {
            ((EnvironmentAware) instance).setEnvironment(this.environment);
         }
         if (instance instanceof ResourceLoaderAware) {
            ((ResourceLoaderAware) instance).setResourceLoader(this.resourceLoader);
         }
      }
   }

   @Override
   public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
      Assert.isInstanceOf(ConfigurableListableBeanFactory.class, beanFactory);
      this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
   }

   protected final ConfigurableListableBeanFactory getBeanFactory() {
      return this.beanFactory;
   }

   @Override
   public void setBeanClassLoader(ClassLoader classLoader) {
      this.beanClassLoader = classLoader;
   }

   protected ClassLoader getBeanClassLoader() {
      return this.beanClassLoader;
   }

   @Override
   public void setEnvironment(Environment environment) {
      this.environment = environment;
   }

   protected final Environment getEnvironment() {
      return this.environment;
   }

   @Override
   public void setResourceLoader(ResourceLoader resourceLoader) {
      this.resourceLoader = resourceLoader;
   }

   protected final ResourceLoader getResourceLoader() {
      return this.resourceLoader;
   }

   @Override
   public int getOrder() {
      return Ordered.LOWEST_PRECEDENCE - 1;
   }

   private static class AutoConfigurationGroup implements DeferredImportSelector.Group,
         BeanClassLoaderAware, BeanFactoryAware, ResourceLoaderAware {

      private final Map<String, AnnotationMetadata> entries = new LinkedHashMap<>();

      private final List<AutoConfigurationEntry> autoConfigurationEntries = new ArrayList<>();

      private ClassLoader beanClassLoader;

      private BeanFactory beanFactory;

      private ResourceLoader resourceLoader;

      private AutoConfigurationMetadata autoConfigurationMetadata;

      @Override
      public void setBeanClassLoader(ClassLoader classLoader) {
         this.beanClassLoader = classLoader;
      }

      @Override
      public void setBeanFactory(BeanFactory beanFactory) {
         this.beanFactory = beanFactory;
      }

      @Override
      public void setResourceLoader(ResourceLoader resourceLoader) {
         this.resourceLoader = resourceLoader;
      }

      @Override
      public void process(AnnotationMetadata annotationMetadata,
            DeferredImportSelector deferredImportSelector) {
         Assert.state(
               deferredImportSelector instanceof AutoConfigurationImportSelector,
               () -> String.format("Only %s implementations are supported, got %s",
                     AutoConfigurationImportSelector.class.getSimpleName(),
                     deferredImportSelector.getClass().getName()));
         AutoConfigurationEntry autoConfigurationEntry = ((AutoConfigurationImportSelector) deferredImportSelector)
               .getAutoConfigurationEntry(getAutoConfigurationMetadata(),
                     annotationMetadata);
         this.autoConfigurationEntries.add(autoConfigurationEntry);
         for (String importClassName : autoConfigurationEntry.getConfigurations()) {
            this.entries.putIfAbsent(importClassName, annotationMetadata);
         }
      }

      @Override
      public Iterable<Entry> selectImports() {
         if (this.autoConfigurationEntries.isEmpty()) {
            return Collections.emptyList();
         }
         Set<String> allExclusions = this.autoConfigurationEntries.stream()
               .map(AutoConfigurationEntry::getExclusions)
               .flatMap(Collection::stream).collect(Collectors.toSet());
         Set<String> processedConfigurations = this.autoConfigurationEntries.stream()
               .map(AutoConfigurationEntry::getConfigurations)
               .flatMap(Collection::stream)
               .collect(Collectors.toCollection(LinkedHashSet::new));
         processedConfigurations.removeAll(allExclusions);

         return sortAutoConfigurations(processedConfigurations,
               getAutoConfigurationMetadata())
                     .stream()
                     .map((importClassName) -> new Entry(
                           this.entries.get(importClassName), importClassName))
                     .collect(Collectors.toList());
      }

      private AutoConfigurationMetadata getAutoConfigurationMetadata() {
         if (this.autoConfigurationMetadata == null) {
            this.autoConfigurationMetadata = AutoConfigurationMetadataLoader
                  .loadMetadata(this.beanClassLoader);
         }
         return this.autoConfigurationMetadata;
      }

      private List<String> sortAutoConfigurations(Set<String> configurations,
            AutoConfigurationMetadata autoConfigurationMetadata) {
         return new AutoConfigurationSorter(getMetadataReaderFactory(),
               autoConfigurationMetadata).getInPriorityOrder(configurations);
      }

      private MetadataReaderFactory getMetadataReaderFactory() {
         try {
            return this.beanFactory.getBean(
                  SharedMetadataReaderFactoryContextInitializer.BEAN_NAME,
                  MetadataReaderFactory.class);
         }
         catch (NoSuchBeanDefinitionException ex) {
            return new CachingMetadataReaderFactory(this.resourceLoader);
         }
      }

   }

   protected static class AutoConfigurationEntry {

      private final List<String> configurations;

      private final Set<String> exclusions;

      private AutoConfigurationEntry() {
         this.configurations = Collections.emptyList();
         this.exclusions = Collections.emptySet();
      }

      /**
       * Create an entry with the configurations that were contributed and their
       * exclusions.
       * @param configurations the configurations that should be imported
       * @param exclusions the exclusions that were applied to the original list
       */
      AutoConfigurationEntry(Collection<String> configurations,
            Collection<String> exclusions) {
         this.configurations = new ArrayList<>(configurations);
         this.exclusions = new HashSet<>(exclusions);
      }

      public List<String> getConfigurations() {
         return this.configurations;
      }

      public Set<String> getExclusions() {
         return this.exclusions;
      }

   }

}
```

## 运行原理

```java
public static void main(String[] args) {
   SpringApplication.run(SpringRedisCachingApplication.class, args);
}
```

### SpringApplication

一个用来从main方法中引导和运行Spring应用的类。默认引导步骤如下

- Create an appropriate [`ApplicationContext`](https://docs.spring.io/spring-framework/docs/5.1.6.RELEASE/javadoc-api/org/springframework/context/ApplicationContext.html?is-external=true) instance (depending on your classpath)
- Register a [`CommandLinePropertySource`](https://docs.spring.io/spring-framework/docs/5.1.6.RELEASE/javadoc-api/org/springframework/core/env/CommandLinePropertySource.html?is-external=true) to expose command line arguments as Spring properties
- Refresh the application context, loading all singleton beans
- Trigger any [`CommandLineRunner`](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/CommandLineRunner.html) beans

