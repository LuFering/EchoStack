D:\JAVA\bin\java.exe -XX:TieredStopAtLevel=1 -Dspring.output.ansi.enabled=always -Dcom.sun.management.jmxremote -Dspring.jmx.enabled=true -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true "-Dmanagement.endpoints.jmx.exposure.include=*" "-javaagent:D:\IntelliJ IDEA 2024.3.7\lib\idea_rt.jar=1269" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 @C:\Users\25153\AppData\Local\Temp\idea_arg_file459346283 com.nageoffer.ai.ragent.RagentApplication

.   ____          _            __ _ _
/\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
'  |____| .__|_| |_|_| |_\__, | / / / /
=========|_|==============|___/=/_/_/_/

:: Spring Boot ::                (v3.5.7)

2026-05-11T21:26:22.017+08:00  INFO 33892 --- [ragent-service] [           main] c.nageoffer.ai.ragent.RagentApplication  : Starting RagentApplication using Java 24.0.2 with PID 33892 (D:\ragent\bootstrap\target\classes started by 25153 in D:\ragent)
2026-05-11T21:26:22.020+08:00  INFO 33892 --- [ragent-service] [           main] c.nageoffer.ai.ragent.RagentApplication  : No active profile set, falling back to 1 default profile: "default"
2026-05-11T21:26:23.279+08:00  INFO 33892 --- [ragent-service] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Multiple Spring Data modules found, entering strict repository configuration mode
2026-05-11T21:26:23.283+08:00  INFO 33892 --- [ragent-service] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data Redis repositories in DEFAULT mode.
2026-05-11T21:26:23.383+08:00  INFO 33892 --- [ragent-service] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 74 ms. Found 0 Redis repository interfaces.
2026-05-11T21:26:24.978+08:00  INFO 33892 --- [ragent-service] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 9090 (http)
2026-05-11T21:26:25.001+08:00  INFO 33892 --- [ragent-service] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2026-05-11T21:26:25.002+08:00  INFO 33892 --- [ragent-service] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.48]
2026-05-11T21:26:25.088+08:00  INFO 33892 --- [ragent-service] [           main] o.a.c.c.C.[.[localhost].[/api/ragent]    : Initializing Spring embedded WebApplicationContext
2026-05-11T21:26:25.088+08:00  INFO 33892 --- [ragent-service] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 3009 ms
2026-05-11T21:26:25.164+08:00  INFO 33892 --- [ragent-service] [           main] org.redisson.Version                     : Redisson 4.0.0
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::allocateMemory has been called by io.netty.util.internal.PlatformDependent0$2 (file:/C:/Users/25153/.m2/repository/io/netty/netty-common/4.1.128.Final/netty-common-4.1.128.Final.jar)
WARNING: Please consider reporting this to the maintainers of class io.netty.util.internal.PlatformDependent0$2
WARNING: sun.misc.Unsafe::allocateMemory will be removed in a future release
2026-05-11T21:26:32.127+08:00 ERROR 33892 --- [ragent-service] [           main] o.s.b.web.embedded.tomcat.TomcatStarter  : Error starting Tomcat context. Exception: org.springframework.beans.factory.UnsatisfiedDependencyException. Message: Error creating bean with name 'uploadRateLimitFilter' defined in file [D:\ragent\bootstrap\target\classes\com\nageoffer\ai\ragent\knowledge\filter\UploadRateLimitFilter.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'redisson' defined in class path resource [org/redisson/spring/starter/RedissonAutoConfigurationV2.class]: Failed to instantiate [org.redisson.api.RedissonClient]: Factory method 'redisson' threw exception with message: java.util.concurrent.ExecutionException: org.redisson.client.RedisConnectionException: Unable to connect to Redis server: 127.0.0.1/127.0.0.1:6379
2026-05-11T21:26:32.201+08:00  INFO 33892 --- [ragent-service] [           main] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2026-05-11T21:26:32.212+08:00  WARN 33892 --- [ragent-service] [           main] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.context.ApplicationContextException: Unable to start web server
2026-05-11T21:26:32.219+08:00  INFO 33892 --- [ragent-service] [           main] .s.b.a.l.ConditionEvaluationReportLogger :

Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2026-05-11T21:26:32.245+08:00 ERROR 33892 --- [ragent-service] [           main] o.s.boot.SpringApplication               : Application run failed

org.springframework.context.ApplicationContextException: Unable to start web server
at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.onRefresh(ServletWebServerApplicationContext.java:170) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:621) ~[spring-context-6.2.12.jar:6.2.12]
at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:752) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:439) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.SpringApplication.run(SpringApplication.java:318) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1361) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1350) ~[spring-boot-3.5.7.jar:3.5.7]
at com.nageoffer.ai.ragent.RagentApplication.main(RagentApplication.java:39) ~[classes/:na]
Caused by: org.springframework.boot.web.server.WebServerException: Unable to start embedded Tomcat
at org.springframework.boot.web.embedded.tomcat.TomcatWebServer.initialize(TomcatWebServer.java:147) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.web.embedded.tomcat.TomcatWebServer.<init>(TomcatWebServer.java:107) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory.getTomcatWebServer(TomcatServletWebServerFactory.java:517) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory.getWebServer(TomcatServletWebServerFactory.java:219) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.createWebServer(ServletWebServerApplicationContext.java:193) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.onRefresh(ServletWebServerApplicationContext.java:167) ~[spring-boot-3.5.7.jar:3.5.7]
... 8 common frames omitted
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'uploadRateLimitFilter' defined in file [D:\ragent\bootstrap\target\classes\com\nageoffer\ai\ragent\knowledge\filter\UploadRateLimitFilter.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'redisson' defined in class path resource [org/redisson/spring/starter/RedissonAutoConfigurationV2.class]: Failed to instantiate [org.redisson.api.RedissonClient]: Factory method 'redisson' threw exception with message: java.util.concurrent.ExecutionException: org.redisson.client.RedisConnectionException: Unable to connect to Redis server: 127.0.0.1/127.0.0.1:6379
at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:804) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:240) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(AbstractAutowireCapableBeanFactory.java:1395) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1232) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:569) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:529) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:339) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:373) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:337) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:207) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.boot.web.servlet.ServletContextInitializerBeans.getOrderedBeansOfType(ServletContextInitializerBeans.java:230) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.web.servlet.ServletContextInitializerBeans.addAsRegistrationBean(ServletContextInitializerBeans.java:184) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.web.servlet.ServletContextInitializerBeans.addAsRegistrationBean(ServletContextInitializerBeans.java:179) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.web.servlet.ServletContextInitializerBeans.addAdaptableBeans(ServletContextInitializerBeans.java:164) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.web.servlet.ServletContextInitializerBeans.<init>(ServletContextInitializerBeans.java:96) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.getServletContextInitializerBeans(ServletWebServerApplicationContext.java:271) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.selfInitialize(ServletWebServerApplicationContext.java:245) ~[spring-boot-3.5.7.jar:3.5.7]
at org.springframework.boot.web.embedded.tomcat.TomcatStarter.onStartup(TomcatStarter.java:52) ~[spring-boot-3.5.7.jar:3.5.7]
at org.apache.catalina.core.StandardContext.startInternal(StandardContext.java:4452) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1201) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1191) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:328) ~[na:na]
at org.apache.tomcat.util.threads.InlineExecutorService.execute(InlineExecutorService.java:81) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at java.base/java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:148) ~[na:na]
at org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:747) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.apache.catalina.core.StandardHost.startInternal(StandardHost.java:770) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1201) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1191) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:328) ~[na:na]
at org.apache.tomcat.util.threads.InlineExecutorService.execute(InlineExecutorService.java:81) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at java.base/java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:148) ~[na:na]
at org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:747) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.apache.catalina.core.StandardEngine.startInternal(StandardEngine.java:201) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.apache.catalina.core.StandardService.startInternal(StandardService.java:410) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.apache.catalina.core.StandardServer.startInternal(StandardServer.java:868) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:164) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.apache.catalina.startup.Tomcat.start(Tomcat.java:436) ~[tomcat-embed-core-10.1.48.jar:10.1.48]
at org.springframework.boot.web.embedded.tomcat.TomcatWebServer.initialize(TomcatWebServer.java:128) ~[spring-boot-3.5.7.jar:3.5.7]
... 13 common frames omitted
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisson' defined in class path resource [org/redisson/spring/starter/RedissonAutoConfigurationV2.class]: Failed to instantiate [org.redisson.api.RedissonClient]: Factory method 'redisson' threw exception with message: java.util.concurrent.ExecutionException: org.redisson.client.RedisConnectionException: Unable to connect to Redis server: 127.0.0.1/127.0.0.1:6379
at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:657) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:489) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1375) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1205) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:569) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:529) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:339) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:373) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:337) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1770) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1653) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:913) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:791) ~[spring-beans-6.2.12.jar:6.2.12]
... 54 common frames omitted
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.redisson.api.RedissonClient]: Factory method 'redisson' threw exception with message: java.util.concurrent.ExecutionException: org.redisson.client.RedisConnectionException: Unable to connect to Redis server: 127.0.0.1/127.0.0.1:6379
at org.springframework.beans.factory.support.SimpleInstantiationStrategy.lambda$instantiate$0(SimpleInstantiationStrategy.java:200) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiateWithFactoryMethod(SimpleInstantiationStrategy.java:89) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:169) ~[spring-beans-6.2.12.jar:6.2.12]
at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:653) ~[spring-beans-6.2.12.jar:6.2.12]
... 68 common frames omitted
Caused by: org.redisson.client.RedisConnectionException: java.util.concurrent.ExecutionException: org.redisson.client.RedisConnectionException: Unable to connect to Redis server: 127.0.0.1/127.0.0.1:6379
at org.redisson.connection.MasterSlaveConnectionManager.doConnect(MasterSlaveConnectionManager.java:252) ~[redisson-4.0.0.jar:4.0.0]
at org.redisson.connection.MasterSlaveConnectionManager.connect(MasterSlaveConnectionManager.java:190) ~[redisson-4.0.0.jar:4.0.0]
at org.redisson.connection.ConnectionManager.create(ConnectionManager.java:98) ~[redisson-4.0.0.jar:4.0.0]
at org.redisson.Redisson.<init>(Redisson.java:76) ~[redisson-4.0.0.jar:4.0.0]
at org.redisson.Redisson.create(Redisson.java:119) ~[redisson-4.0.0.jar:4.0.0]
at org.redisson.spring.starter.RedissonAutoConfiguration.redisson(RedissonAutoConfiguration.java:318) ~[redisson-spring-boot-starter-4.0.0.jar:4.0.0]
at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104) ~[na:na]
at java.base/java.lang.reflect.Method.invoke(Method.java:565) ~[na:na]
at org.springframework.beans.factory.support.SimpleInstantiationStrategy.lambda$instantiate$0(SimpleInstantiationStrategy.java:172) ~[spring-beans-6.2.12.jar:6.2.12]
... 71 common frames omitted
Caused by: java.util.concurrent.ExecutionException: org.redisson.client.RedisConnectionException: Unable to connect to Redis server: 127.0.0.1/127.0.0.1:6379
at java.base/java.util.concurrent.CompletableFuture.wrapInExecutionException(CompletableFuture.java:347) ~[na:na]
at java.base/java.util.concurrent.CompletableFuture.reportGet(CompletableFuture.java:442) ~[na:na]
at java.base/java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2142) ~[na:na]
at org.redisson.connection.MasterSlaveConnectionManager.doConnect(MasterSlaveConnectionManager.java:247) ~[redisson-4.0.0.jar:4.0.0]
... 79 common frames omitted
Caused by: org.redisson.client.RedisConnectionException: Unable to connect to Redis server: 127.0.0.1/127.0.0.1:6379
at org.redisson.connection.ConnectionsHolder.lambda$createConnection$2(ConnectionsHolder.java:169) ~[redisson-4.0.0.jar:4.0.0]
at java.base/java.util.concurrent.CompletableFuture.uniHandle(CompletableFuture.java:980) ~[na:na]
at java.base/java.util.concurrent.CompletableFuture$UniHandle.tryFire(CompletableFuture.java:957) ~[na:na]
at java.base/java.util.concurrent.CompletableFuture.postComplete(CompletableFuture.java:556) ~[na:na]
at java.base/java.util.concurrent.CompletableFuture.completeExceptionally(CompletableFuture.java:2246) ~[na:na]
at org.redisson.connection.ConnectionsHolder.lambda$createConnection$5(ConnectionsHolder.java:183) ~[redisson-4.0.0.jar:4.0.0]
at java.base/java.util.concurrent.CompletableFuture.uniWhenComplete(CompletableFuture.java:909) ~[na:na]
at java.base/java.util.concurrent.CompletableFuture$UniWhenComplete.tryFire(CompletableFuture.java:887) ~[na:na]
at java.base/java.util.concurrent.CompletableFuture.postComplete(CompletableFuture.java:556) ~[na:na]
at java.base/java.util.concurrent.CompletableFuture.completeExceptionally(CompletableFuture.java:2246) ~[na:na]
at org.redisson.client.RedisClient$2$2.run(RedisClient.java:325) ~[redisson-4.0.0.jar:4.0.0]
at io.netty.util.concurrent.AbstractEventExecutor.runTask(AbstractEventExecutor.java:173) ~[netty-common-4.1.128.Final.jar:4.1.128.Final]
at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:166) ~[netty-common-4.1.128.Final.jar:4.1.128.Final]
at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:472) ~[netty-common-4.1.128.Final.jar:4.1.128.Final]
at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:569) ~[netty-transport-4.1.128.Final.jar:4.1.128.Final]
at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:998) ~[netty-common-4.1.128.Final.jar:4.1.128.Final]
at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) ~[netty-common-4.1.128.Final.jar:4.1.128.Final]
at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) ~[netty-common-4.1.128.Final.jar:4.1.128.Final]
at java.base/java.lang.Thread.run(Thread.java:1447) ~[na:na]
Caused by: java.util.concurrent.CompletionException: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: getsockopt: 127.0.0.1/127.0.0.1:6379
at java.base/java.util.concurrent.CompletableFuture.wrapInCompletionException(CompletableFuture.java:325) ~[na:na]
at java.base/java.util.concurrent.CompletableFuture.encodeRelay(CompletableFuture.java:414) ~[na:na]
at java.base/java.util.concurrent.CompletableFuture.completeRelay(CompletableFuture.java:423) ~[na:na]
at java.base/java.util.concurrent.CompletableFuture$UniRelay.tryFire(CompletableFuture.java:1143) ~[na:na]
... 11 common frames omitted
Caused by: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: getsockopt: 127.0.0.1/127.0.0.1:6379
Caused by: java.net.ConnectException: Connection refused: getsockopt
at java.base/sun.nio.ch.Net.pollConnect(Native Method) ~[na:na]
at java.base/sun.nio.ch.Net.pollConnectNow(Net.java:628) ~[na:na]
at java.base/sun.nio.ch.SocketChannelImpl.finishConnect(SocketChannelImpl.java:1046) ~[na:na]
at io.netty.channel.socket.nio.NioSocketChannel.doFinishConnect(NioSocketChannel.java:336) ~[netty-transport-4.1.128.Final.jar:4.1.128.Final]
at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.finishConnect(AbstractNioChannel.java:339) ~[netty-transport-4.1.128.Final.jar:4.1.128.Final]
at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:784) ~[netty-transport-4.1.128.Final.jar:4.1.128.Final]
at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:732) ~[netty-transport-4.1.128.Final.jar:4.1.128.Final]
at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:658) ~[netty-transport-4.1.128.Final.jar:4.1.128.Final]
at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:562) ~[netty-transport-4.1.128.Final.jar:4.1.128.Final]
at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:998) ~[netty-common-4.1.128.Final.jar:4.1.128.Final]
at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) ~[netty-common-4.1.128.Final.jar:4.1.128.Final]
at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) ~[netty-common-4.1.128.Final.jar:4.1.128.Final]
at java.base/java.lang.Thread.run(Thread.java:1447) ~[na:na]


进程已结束，退出代码为 1
