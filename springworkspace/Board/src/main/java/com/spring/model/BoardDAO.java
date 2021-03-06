package com.spring.model;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.spring.board.model.BoardVO;
import com.spring.board.model.CommentVO;
import com.spring.member.model.MemberVO;

// === #32. DAO 선언 === //
@Component
@Repository // ---> 이 안에 @Component 기능이 포함돼있어서 하나만 적어줘도 됨
public class BoardDAO implements InterBoardDAO {

	// === #33. 의존객체 주입하기(DI: Dependency Injection) === //
	
	// >>> 의존 객체 자동 주입(Automatic Dependency Injection)은
	//     스프링 컨테이너가 자동적으로 의존 대상 객체를 찾아서 해당 객체에 필요한 의존객체를 주입하는 것을 말한다. 
	//     단, 의존객체는 스프링 컨테이너속에 bean 으로 등록되어 있어야 한다. 

	//     의존 객체 자동 주입(Automatic Dependency Injection)방법 ★ 3가지 ★
	//     1. @Autowired ==> Spring Framework에서 지원하는 어노테이션이다. 
	//                       스프링 컨테이너에 담겨진 의존객체를 주입할때 타입을 찾아서 연결(의존객체주입)한다.
	
	//     2. @Resource  ==> Java 에서 지원하는 어노테이션이다.
	//                       스프링 컨테이너에 담겨진 의존객체를 주입할때 필드명(이름)을 찾아서 연결(의존객체주입)한다.
	
	//     3. @Inject    ==> Java 에서 지원하는 어노테이션이다.
    //                       스프링 컨테이너에 담겨진 의존객체를 주입할때 타입을 찾아서 연결(의존객체주입)한다.	
	
	
	/* ▲ 이렇게 선언 해줌으로써 root-context에 있는 sqlsession 객체가 
	  			sqlsession 변수에 들어옴 ▼ */
	@Resource // 타입이 여러개 일때는 @Resource를 사용
	private SqlSessionTemplate sqlsession;
	// bean중에 이름이 sqlsession인 객체를 찾음 (내거)
	
	@Resource 
	private SqlSessionTemplate sqlsession2;
	// bean중에 이름이 sqlsession2인 객체를 찾음 (상대방거)
	
	// new해왔던 기존 방식과는 다름.
	// Type에 따라 Spring 컨테이너가 알아서 root-context.xml에 생성된 org.mybatis.spring.SqlSessionTemplate의 Bean을 sqlsession에 주입시킨다. 
	// 그러므로 sqlsession은 null이 아니다.

	@Resource 
	private SqlSessionTemplate sqlsession3;
	
	
	@Override
	public int test_insert() {

		int n = sqlsession.insert("board.test_insert"); 
				// 				   ▲ insert문을 호출 (board.xml에 있는 namespace)
		return n;
	}
	
	@Override
	public int test_insert2() {
		
		int n = sqlsession2.insert("remote_board.test_insert"); 
		// 				   ▲ insert문을 호출 (board.xml에 있는 namespace)
		return n;
	}

	@Override
	public List<TestVO> test_select() {

		List<TestVO> testvoList = sqlsession.selectList("board.test_select"); // SQL문 호출함
		
		return testvoList;
	}

	@Override
	public List<TestVO> test_select2() {
		
		List<TestVO> testvoList = sqlsession2.selectList("remote_board.test_select"); // SQL문 호출함
		
		return testvoList;
	}

	@Override
	public int test_insert(HashMap<String, String> paraMap) {
		
		int n = sqlsession.insert("board.test_insertPm", paraMap);
		return n;

	}

	@Override
	public int ajaxtest_insert(HashMap<String, String> paraMap) {
		// 의존 객체 sqlsession
		int n = sqlsession.insert("board.ajaxtest_insert", paraMap);
								// ▲ mapper에 있는 namespace 
		return n;
	}

	
	
	@Override
	public List<TestVO> employees_select() {
		
		List<TestVO> empvoList = sqlsession3.selectList("employees.employees_select"); // SQL문 호출함
		
		return empvoList;
	}


	/* 	또는 HashMap으로 해보기 ↓
	@Override
	public List<HashMap<String, String>> employees_select() {
		
		List<HashMap<String, String>> empvoList = sqlsession3.employees_select("employees.employees_select"); // SQL문 호출함
		
		return empvoList;
	}

	*/
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////

	// === #38. 메인 페이지용 이미지 파일 가져오기 === //
	@Override
	public List<String> getImgfilenameList() {

		List<String> imgfilenameList = sqlsession.selectList("board.getImgfilenameList");
															// namespace명.id값
		return imgfilenameList;
	}
	
	// === #46. 로그인 처리하기 === // 
	@Override
	public MemberVO getLoginMember(HashMap<String, String> paraMap) {
		
		MemberVO loginuser = sqlsession.selectOne("board.getLoginMember", paraMap); // 한개만 얻어올땐 selectOne
		
		return loginuser;
	}
	
	// 마지막 로그인 한 날짜시간 변경(기록)하기
	@Override
	public void setLastLoginDate(HashMap<String, String> paraMap) {
		sqlsession.update("board.setLastLoginDate", paraMap);
	}

	
	// === #56. 글쓰기 (파일첨부가 없는 글쓰기) === //
	@Override
	public int add(BoardVO boardvo) {

		int n = sqlsession.insert("board.add", boardvo);
		
		return n;
	}

	
	// === #60. 페이징 처리를 안한 검색어가 없는 전체 글목록 보여주기 === // 
	@Override
	public List<BoardVO> getboardList() {

		List<BoardVO> boardList = sqlsession.selectList("board.getboardList");
		
		return boardList;
	}

	// === #64. 글 한개 보여주기 === //
	@Override
	public BoardVO getView(String seq) { // getView는 글 1개만 읽어옴
		
		BoardVO boardvo = sqlsession.selectOne("board.getView", seq);
		
		return boardvo;
	}

	// === #65. 글 조회수 1 증가하기 === //
	@Override
	public void setAddReadCount(String seq) {
		sqlsession.update("board.setAddReadCount", seq);
	}

	// === #74. 글 한개 수정하기 === // 
	@Override
	public int updateBoard(BoardVO boardvo) {

		int n = sqlsession.update("board.updateBoard", boardvo);
		
		return n;
	}


	// === #79. 글 삭제하기 === // 
	@Override
	public int deleteBoard(HashMap<String, String> paraMap) {
		int n = sqlsession.delete("board.deleteBoard", paraMap);
		
		return n;
	}

	// === AOP 에서 사용하는 것으로 회원에게 포인트를 주기 위한 것 === //
	@Override
	public void pointPlus(HashMap<String, String> paraMap) {

		sqlsession.update("board.pointPlus", paraMap);
		
	}
	
	// === #86. 댓글쓰기(tblComment 테이블에 insert) === 
	@Override
	public int addComment(CommentVO commentvo) {
		int n = sqlsession.insert("board.addComment", commentvo);
		return n;
	}

	// === #87. tblBoard 테이블에 commentCount 컬럼의 값을 1증가(update) === 
	@Override
	public int updateCommentCount(String parentSeq) {
		int n = sqlsession.update("board.updateCommentCount", parentSeq);
		return n;
	}

	// === #92. 원게시물에 딸린 댓글 보여주기 === // 
	@Override
	public List<CommentVO> getCommentList(String parentSeq) {
		List<CommentVO> commentList = sqlsession.selectList("board.getCommentList", parentSeq);
		return commentList;
	}

	// ==== #97. 딸린 댓글 삭제(딸린 댓글이 없을수도 있지만 실행) === //
	@Override
	public void deleteComment(HashMap<String, String> paraMap) {
		
		sqlsession.update("board.deleteComment", paraMap);
		
	}

	// === #102. 페이징 처리를 안한 검색어가 있는 전체 글목록 보여주기 === //
	@Override
	public List<BoardVO> boardListSearch(HashMap<String, String> paraMap) {
		List<BoardVO> boardList = sqlsession.selectList("board.boardListSearch", paraMap);
														/* namespace.id명 */
		return boardList;
	}

	
	// === #108. 검색어 입력시 자동글 완성하기 5 === //
	@Override
	public List<String> wordSearchShow(HashMap<String, String> paraMap) {
		List<String> wordList = sqlsession.selectList("board.wordSearchShow", paraMap);
		return wordList;
	}

	
	// ===#114. 총 게시물 건수 구하기 === //
	@Override
	public int getTotalCount(HashMap<String, String> paraMap) {
		int totalCount = sqlsession.selectOne("board.getTotalCount", paraMap);
		return totalCount;
	}

	
	// === #117. 페이징 처리한 글목록 가져오기 (검색이 있든지 없든지 모두 다 포함) === //
	@Override
	public List<BoardVO> boardListSearchWithPaging(HashMap<String, String> paraMap) {
		List<BoardVO> boardList = sqlsession.selectList("board.boardListSearchWithPaging", paraMap);
		return boardList;
	}

	// === #128. 원게시물에 딸린 댓글들을 페이징처리해서 조회해오기(Ajax 로 처리) === //
	@Override
	public List<CommentVO> getCommentListPaging(HashMap<String, String> paraMap) {
		List<CommentVO> commentList = sqlsession.selectList("board.getCommentListPaging", paraMap);
		return commentList;
	}

	// === #132. 원게시물에 딸린 댓글 getCommentTotalPage 알아오기(Ajax 로 처리) === //
	@Override
	public int getCommentTotalCount(HashMap<String, String> paraMap) {
		int totalCount = sqlsession.selectOne("board.getCommentTotalCount", paraMap);
		return totalCount;
	}

	
	// === #141. tblBoard 테이블에서 groupno 컬럼의 최대값 구하기 === //
	@Override
	public int getGroupnoMax() {
		int max = sqlsession.selectOne("board.getGroupnoMax");
		return max;
	}

	
	// === #152. 글쓰기 (파일첨부가 있는 글쓰기) === //
	@Override
	public int add_withFile(BoardVO boardvo) {
		int n = sqlsession.insert("board.add_withFile", boardvo);
		return n;
	}
	
		
}
