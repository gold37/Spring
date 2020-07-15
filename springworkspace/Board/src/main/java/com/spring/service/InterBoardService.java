package com.spring.service;

import java.util.HashMap;
import java.util.List;

import com.spring.board.model.BoardVO;
import com.spring.member.model.MemberVO;
import com.spring.model.TestVO;

public interface InterBoardService {

	int test_insert();
	
	HashMap<String, List<TestVO>> test_select();

	int test_insert(HashMap<String, String> paraMap);

	int ajaxtest_insert(HashMap<String, String> paraMap);

	List<TestVO> ajaxtest_select();

	List<TestVO> datatables_test();

	List<TestVO> employees_test();

	
	///////////////////////////////// 게시판 만들기 //////////////////////////////
	
	List<String> getImgfilenameList(); // 이미지 파일명을 가져옴

	MemberVO getLoginMember(HashMap<String, String> paraMap); // 로그인 처리하기

	int add(BoardVO boardvo); // 글쓰기 (파일첨부가 없는 글쓰기)

	List<BoardVO> getboardList(); // 페이징 처리를 안한 검색어가 없는 전체 글목록 보여주기

	BoardVO getView(String seq, String userid); // 글 조회수 증가와함께 글 한개 조회하기
												// 조회수 증가는 다른 사람의 글을 읽을때만 증가한다.
												// 로그인하지 않은 상태에서는 조회수 증가가 일어나지 않는다. 
					
	
}


