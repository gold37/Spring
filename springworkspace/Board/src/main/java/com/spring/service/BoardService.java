package com.spring.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.spring.model.InterBoardDAO;

// === #31. Service 선언 === //
@Component // 자동적으로 이 클래스 명의 첫글자가 소문자인 이름이 bean으로 올라감
@Service
// Service는 DB와 관련된 업무(insert, update, delete 등)를 수행함 --> DAO가 반드시 필요함
public class BoardService implements InterBoardService {

	// === #34. 의존객체 주입하기(DI: Dependency Injection) === //
	@Autowired
	private InterBoardDAO dao;
	// new해왔던 기존 방식과는 다름.
	// Type에 따라 Spring 컨테이너가 알아서 bean으로 등록된 com.spring.model.BoardDAO의 bean을 dao에 주입 
	// 그러므로 dao는 null이 아니다.

	
	@Override
	public int test_insert() {

		int n = dao.test_insert();
		dao.test_insert();
		return n;
	}
	
	
	

}
