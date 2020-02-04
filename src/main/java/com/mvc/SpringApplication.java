package com.mvc;

import java.io.File;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class SpringApplication {
	public static void run() {
		File base = new File(System.getProperty("java.io.tmpdir"));
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8888);
		tomcat.addContext("/", base.getAbsolutePath());
		//tomcat.addWebapp("/", base.getAbsolutePath());
		System.out.println(base.getAbsolutePath());
		try {
			tomcat.start();
			
			//初始化Spring环境
			AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext = new AnnotationConfigWebApplicationContext();
			annotationConfigWebApplicationContext.register(WebAppConfig.class);
			annotationConfigWebApplicationContext.refresh();		
			
			DispatcherServlet dispatcherServlet = new DispatcherServlet(annotationConfigWebApplicationContext);			
			Wrapper warpper = tomcat.addServlet("/", "app", dispatcherServlet);
			warpper.setLoadOnStartup(1);
			warpper.addMapping("/");			
			tomcat.getServer().await();
		} catch (LifecycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
