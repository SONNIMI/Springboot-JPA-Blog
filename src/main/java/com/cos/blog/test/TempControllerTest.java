package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempControllerTest {
	
	@GetMapping("/temp/home")
	public String tempHome() {
		System.out.println("temp/home");
		//파일 리턴 기본경로 : src/main/resources/static 이 기본 경로이다.
		return "/home.html";
	}
	@GetMapping("/temp/jsp")
	public String tempJsp() {
		System.out.println("temp/jsp");
		//prefix : /WEB-INF/views/
		//suffix : .jsp 
		//풀네임 : /WEB-INF/views/test.jsp
		return "test";
	}
}
