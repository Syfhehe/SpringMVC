package com.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	@RequestMapping("/syf")
	public String test() {
		System.out.println("hahahahaha");
		return "index";
	}
}
