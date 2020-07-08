package com.spring.model;

import java.util.HashMap;
import java.util.List;

public interface InterBoardDAO {

	// insert가 성공하면 1이 나오기 때문에 return 타입은 int
	int test_insert(); // spring_test1 테이블에 insert 하기
	int test_insert2(); // 상대방 spring_test1 테이블에 insert 하기
	
	List<TestVO> test_select(); // spring_test1 테이블을 select 하기
	List<TestVO> test_select2(); // 상대방 spring_test1 테이블을 select 하기
	
	int test_insert(HashMap<String, String> paraMap);
	
}
