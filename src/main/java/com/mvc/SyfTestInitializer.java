package com.mvc;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import com.mvc.impl.SyfInterface;

@HandlesTypes(SyfInterface.class)
public class SyfTestInitializer implements ServletContainerInitializer{

	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		System.out.println("---------------ServletContainerInitializer--------------");		
		for(Class<?> aClass : c) {
			System.out.println(aClass);
		}
	}

}
