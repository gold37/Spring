package com.spring.service;

import java.util.HashMap;
import java.util.List;

import com.spring.board.model.BoardVO;
import com.spring.board.model.CommentVO;
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

	BoardVO getViewWithNoAddCount(String seq); // 글조회수 증가는 없고 단순히 글 한개 조회만을 해주는 것

	int edit(BoardVO boardvo); // 글 한개 수정하기

	int delete(HashMap<String, String> paraMap) throws Throwable; // 글 삭제하기

	void pointPlus(HashMap<String, String> paraMap); // AOP 에서 사용하는 것으로 회원에게 포인트를 주기 위한 것

	int addComment(CommentVO commentvo) throws Throwable; // 댓글쓰기 

	List<CommentVO> getCommentList(String parentSeq); // 원게시물에 딸린 댓글들을 조회해오는 것

	List<BoardVO> boardListSearch(HashMap<String, String> paraMap); // 페이징 처리를 안한 검색어가 있는 전체 글목록 보여주기 


}


