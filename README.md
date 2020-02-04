# Spring boot的零配置 -> Spring MVC 如何去掉所有的XML

## web.xml 内容
#### 1) DispatcherServlet 放到tomcat容器
（1）load springMVC.xml
（2）load application.xml
（3）<load-on-startup> 设置DispatcherServlet的init在web容器启动时执行的init方法
（4）<url-pattern> 配置DispatcherServlet 拦截的所有请求

```
<servlet>  
    <servlet-name>springMVC</servlet-name>  
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>  
    <init-param>  
        <param-name>contextConfigLocation</param-name>  
        <param-value>classpath*:/springMVC.xml</param-value>  
    </init-param>  
    <load-on-startup>1</load-on-startup>  
</servlet>  
<servlet-mapping>  
    <servlet-name>springMVC</servlet-name>  
    <url-pattern>/</url-pattern>  
</servlet-mapping>
```

```
public class MyWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletCxt) {

        // Load Spring web application configuration
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        ac.register(AppConfig.class);
        ac.refresh();

        // Create and register the DispatcherServlet
        DispatcherServlet servlet = new DispatcherServlet(ac);
        ServletRegistration.Dynamic registration = servletCxt.addServlet("app", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/app/*");
    }
}
```

#### 2）load springMVC.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>  
<beans xmlns="http://www.springframework.org/schema/beans"  
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"  
 xmlns:mvc="http://www.springframework.org/schema/mvc"  
 xsi:schemaLocation="http://www.springframework.org/schema/beans  
      http://www.springframework.org/schema/beans/spring-beans-3.2.xsd  
      http://www.springframework.org/schema/context   
      http://www.springframework.org/schema/context/spring-context-3.2.xsd  
      http://www.springframework.org/schema/mvc  
      http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd">  
	<!-- 静态资源映射,可以配置多个 -->	
	<mvc:resources mapping="/resources/**" location="/resources/" />
	<mvc:resources mapping="/src/**" location="/src/" />
	<mvc:default-servlet-handler />
	
	<!-- controller扫描 -->	
	<context:component-scan base-package="com.sas.webapp.web" />
	<context:component-scan base-package="com.sas.webapp.wap" />
	<context:component-scan base-package="com.sas.core.controller" />
	
	<!-- 开启注解扫描-->	
	<mvc:annotation-driven>
		<mvc:message-converters>
			<bean class="org.springframework.http.converter.FormHttpMessageConverter" />
		</mvc:message-converters>
	</mvc:annotation-driven>
	<!-- 未解读-->	
	<bean class="org.springframework.web.servlet.view.BeanNameViewResolver">
		<property name="order" value="1" />
	</bean>

	<bean id="viewResolver" class="org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver">
		<property name="order" value="2" />
		<property name="suffix" value=".ftl" />
		<property name="contentType" value="text/html; charset=UTF-8" />
		<property name="exposeRequestAttributes" value="true" />
		<property name="exposeSessionAttributes" value="true" />
		<property name="exposeSpringMacroHelpers" value="true" />
		<property name="cache" value="true" />
	</bean>

	<bean id="freemarkerConfig" class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
		<property name="templateLoaderPath" value="/WEB-INF/views/" />
		<property name="freemarkerSettings">
			<props>
				<prop key="template_update_delay">0</prop><!-- 10 minutes -->
				<prop key="default_encoding">UTF-8</prop>
				<prop key="locale">zh_cn</prop>
				<prop key="number_format">0.##########</prop>
				<prop key="url_escaping_charset">UTF-8</prop>
				<!-- <prop key="template_exception_handler"> com.sas.backend.util.FreeMarkerExceptionHandler 
					</prop> -->
			</props>
		</property>
	</bean>

	<bean name="/hessianservice"  class="org.springframework.remoting.caucho.HessianServiceExporter">
         <property name="service" ref="hessianServerService"/>
         <property name="serviceInterface" value="com.sas.core.service.HessianService"/>
    </bean>
    
	<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver"></bean>

	<bean id="json" class="org.springframework.web.servlet.view.json.MappingJackson2JsonView">
		<property name="disableCaching" value="true" />
	</bean>

	<bean id="freemarkTemplateResolver"   class="com.sas.core.spring.FreemarkTemplateResolver"  />
	
</beans>
```


#### 3）初始化Spring的环境
扫描业务类


模拟Spring Boot的
1） new DispatcherServlet 
程序员手动注册给tomcat

