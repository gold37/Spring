package com.spring.model;

import java.util.HashMap;
import java.util.List;

import com.spring.board.model.BoardVO;
import com.spring.board.model.CommentVO;
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
	
	void pointPlus(HashMap<String, String> paraMap); // AOP 에서 사용하는 것으로 회원에게 포인트를 주기 위한 것
	
	int addComment(CommentVO commentvo); // 댓글쓰기(tblComment 테이블에 insert) 
	
	int updateCommentCount(String parentSeq); // tblBoard 테이블에 commentCount 컬럼의 값을 1증가(update)
	
	List<CommentVO> getCommentList(String parentSeq); // 원게시물에 딸린 댓글들을 조회해오는 것
	
	void deleteComment(HashMap<String, String> paraMap); // 딸린 댓글 삭제(딸린 댓글이 없을수도 있지만 실행)
	
	List<BoardVO> boardListSearch(HashMap<String, String> paraMap); // 페이징 처리를 안한 검색어가 있는 전체 글목록 보여주기  
	
	List<String> wordSearchShow(HashMap<String, String> paraMap); // 검색어 입력시 자동글 완성하기
	
	int getTotalCount(HashMap<String, String> paraMap); // 총 게시물 건수 구하기
	
	List<BoardVO> boardListSearchWithPaging(HashMap<String, String> paraMap); // 페이징 처리한 글목록 가져오기 (검색이 있든지 없든지 모두 다 포함)
	
	List<CommentVO> getCommentListPaging(HashMap<String, String> paraMap); // 원게시물에 딸린 댓글들을 페이징처리해서 조회해오기(Ajax 로 처리) 

	int getCommentTotalCount(HashMap<String, String> paraMap); // 원게시물에 딸린 댓글 getCommentTotalPage 알아오기(Ajax 로 처리)
	
	int getGroupnoMax(); // tblBoard 테이블에서 groupno 컬럼의 최대값 구하기
	
	int add_withFile(BoardVO boardvo); // 글쓰기 (파일첨부가 있는 글쓰기)
	
	
}
