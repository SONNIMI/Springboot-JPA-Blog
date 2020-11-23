package com.cos.blog.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// com.cos.blog 이하의 패키지를 스프링이 스캔한다 구조를 주의할 것 
//스프링이 com.cos.blog 패키지 이하를 스캔해서 모든 파일을 메모리에 new하는것이 아니라 특정 어노테이션이 붙어있는 클래스 파일들을 new해서(ioc) 스프링 컨테이너에 관리해준다.
@RestController
public class BlogControllerTest {
	
	@GetMapping("/test/hello")
	public String hello() {
		return "<h1>hello spring boot</h1>";
	}
}
