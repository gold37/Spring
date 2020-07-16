package com.spring.model;

import java.util.HashMap;
import java.util.List;

import com.spring.board.model.BoardVO;
import com.spring.member.model.MemberVO;

public interface InterBoardDAO {

	// insert가 성공하면 1이 나오기 때문에 return 타입은 int
	int test_insert(); // spring_test1 테이블에 insert 하기
	int test_insert2(); // 상대방 spring_test1 테이블에 insert 하기
	
	List<TestVO> test_select(); // spring_test1 테이블을 select 하기
	List<TestVO> test_select2(); // 상대방 spring_test1 테이블을 select 하기
	
	int test_insert(HashMap<String, String> paraMap); // spring_test1 테이블에 insert 하기
	int ajaxtest_insert(HashMap<String, String> paraMap); // spring_test1 테이블에 insert 하기
	
	List<TestVO> employees_select();
	
	
	///////////////////////////////// 게시판 만들기 //////////////////////////////
	
	List<String> getImgfilenameList(); // 이미지 파일명 가져오기
	MemberVO getLoginMember(HashMap<String, String> paraMap); // 로그인 처리하기
	void setLastLoginDate(HashMap<String, String> paraMap); // 마지막 로그인 한 날짜시간 변경(기록)하기
	
	int add(BoardVO boardvo); // 글쓰기 (파일첨부가 없는 글쓰기)
	
	List<BoardVO> getboardList(); // 페이징 처리를 안한 검색어가 없는 전체 글목록 보여주기
	
	BoardVO getView(String seq); // 글 한개를 보여주기
	void setAddReadCount(String seq); // 글 조회수 1 증가하기
	
	int updateBoard(BoardVO boardvo); // 글 한개 수정하기
	
	int deleteBoard(HashMap<String, String> paraMap); // 글 삭제하기
	
}