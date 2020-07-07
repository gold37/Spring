package com.spring.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

// === #32. DAO 선언 === //
@Component
@Repository // ---> 이 안에 @Component 기능이 포함돼있어서 하나만 적어줘도 됨
public class BoardDAO implements InterBoardDAO {

	// === #33. 의존객체 주입하기(DI: Dependency Injection) === //
	@Autowired 
	/* ▲ 이렇게 선언 해줌으로써 root-context에 있는 sqlsession 객체가 
	  			sqlsession 변수에 들어옴 ▼ */
	private SqlSessionTemplate sqlsession;
	// new해왔던 기존 방식과는 다름.
	// Type에 따라 Spring 컨테이너가 알아서 root-context.xml에 생성된 org.mybatis.spring.SqlSessionTemplate의 Bean을 sqlsession에 주입시킨다. 
	// 그러므로 sqlsession은 null이 아니다.
	
	@Override
	public int test_insert() {

		int n = sqlsession.insert("board.test_insert"); 
				// 				   ▲ insert문을 호출 (board.xml에 있는 namespace)
		return n;
	}

	
	
}
