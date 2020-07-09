package com.spring.service;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.spring.model.InterBoardDAO;
import com.spring.model.TestVO;

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

		int n = dao.test_insert(); // 내거에 insert
		int m = dao.test_insert2(); // 상대방거에 insert
		
		return n*m;
	}


	@Override
	public HashMap<String, List<TestVO>> test_select() {
		
		List<TestVO> testvoList = dao.test_select(); // 내거에 select
		List<TestVO> testvoList2 = dao.test_select2(); // 상대방거 select
		
		HashMap<String, List<TestVO>> map = new HashMap<>();
		map.put("testvoList", testvoList);
		map.put("testvoList2", testvoList2);
		
		return map;
	}


   // Form에서 입력받은 값을 spring_test1 테이블에 insert 하기
   @Override
   public int test_insert(HashMap<String, String> paraMap) {
      
      int n = dao.test_insert(paraMap);

      return n;
   }


   // 
	@Override
	public int ajaxtest_insert(HashMap<String, String> paraMap) {
		// service의 의존객체는 dao. 얘한테 paraMap 넘겨줌.
		int n = dao.ajaxtest_insert(paraMap);
		
		return n;
	}


	@Override
	public List<TestVO> ajaxtest_select() {
		
		List<TestVO> testvoList = dao.test_select();
		
		return testvoList;
	}


	

}
