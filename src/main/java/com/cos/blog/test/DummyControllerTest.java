package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;


// html파일이 아니라 data를 리턴해주는 controller = RestController
@RestController
public class DummyControllerTest {
	
	@Autowired //의존성 주입 
	private UserRepository userRepository;
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
			return "삭제되었습니다. id: "+id;
		}catch(EmptyResultDataAccessException e) { //Exception으로 해도 상관없다
			return "삭제에 실패하였습니다 해당 id는 DB에 없습니다.";
		}
	}
	
	//save 함수는 id를 전달하지 않으면 insert를 해주고
	//save 함수는 id를 전달하면 해당 id에 대한 데이터가 있으면 update 없으면 insert를 한다
	//email,password 수정
	@Transactional //함수 종료시 자동 commit이 됨 *더티체킹을 이용한 업데이트
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) {
		System.out.println("id: "+id);
		System.out.println("password: "+requestUser.getPassword());
		System.out.println("email: "+requestUser.getEmail());
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		//userRepository.save(user);
		
		return user;
	}
	
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	};
	
	//한 페이지당 2건의 데이터를 리턴 ex)http://localhost:8000/blog/dummy/user/page?page=1
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size = 2,sort = "id",direction = Sort.Direction.DESC)Pageable pageable){
		Page<User> pagingUsers = userRepository.findAll(pageable);
		List<User> users = pagingUsers.getContent();
		return users;
	}
	
	//{id}주소로 파라미터를 전달 받을 수 있음
	//http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		/* 람다식
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("해당 사용자는 없습니다");
		});*/
		
		//user/4를 찾으면 user가 null이라서 오류 발생 그래서 Optional로 user객체를 감싸서 가져오니 null인지 확인해서 return한다 -> orElseGet() ,orElseThrow() 사용
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 유저는 없습니다. id:"+id);
			}
		});
		// user 객체 = 자바 오브젝트 웹브라우저는 자바 오브젝트를 변환하지 못하기때문에 json으로 변환(Gson 라이브러리)해서 response한다.
		// 스프링부트 = MessageConverter가 응답시 자동으로 작동한다. 만약 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서 json으로 변환 후 브라우저에게 돌려준다.
		return user;
	};
	
	//http://localhost:8000/blog/dummy/join (요청)
	//http 의 body에 username,password,email 데이터를 가지고 (요청) 
	@PostMapping("/dummy/join")
	public String join(User user) {
		System.out.println("username: "+user.getId());
		System.out.println("username: "+user.getUsername());
		System.out.println("password: "+user.getPassword());
		System.out.println("email: "+user.getEmail());
		System.out.println("username: "+user.getRole());
		System.out.println("password: "+user.getPassword());
		System.out.println("email: "+user.getCreateDate());
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	};
	
}
