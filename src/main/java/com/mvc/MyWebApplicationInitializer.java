package com.mvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;

public class MyWebApplicationInitializer implements WebApplicationInitializer  {

	public void onStartup(ServletContext servletContext) throws ServletException {
		
		System.out.println("----------onStartup----------");
		
	}

}
