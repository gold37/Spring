package com.spring.service;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.board.model.BoardVO;
import com.spring.board.model.CommentVO;
import com.spring.common.AES256;
import com.spring.mail.GoogleMail;
import com.spring.member.model.MemberVO;
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
	
	// === #45. 양방향 암호화 알고리즘인 AES256을 사용하여 복호화하기 위한 클래스 (파라미터가 있는 생성자) 의존객체 주입하기(DI: Dependency Injection) === //
	@Autowired
	private AES256 aes;
	
	
	@Autowired
	private GoogleMail mail;

	
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


	@Override
	public List<TestVO> datatables_test() {

		List<TestVO> testvoList = dao.test_select();
		
		return testvoList;
	}


	@Override
	public List<TestVO> employees_test() {
		
		List<TestVO> empvoList = dao.employees_select();
		
		return empvoList;
	
	}


	
	// === #37. 메인 페이지용 이미지 파일 가져오기 === //
	@Override
	public List<String> getImgfilenameList() {
		
		List<String> imgfilenameList = dao.getImgfilenameList();
		
		return imgfilenameList;
	}


	// === #42. 로그인 처리하기  === //
	@Override
	public MemberVO getLoginMember(HashMap<String, String> paraMap) {
		
		MemberVO loginuser = dao.getLoginMember(paraMap);
		
		// === #48. aes 의존객체를 사용하여 로그인 된 사용자(loginuser)의 이메일값을 복호화하도록 한다. === //
		
		
		if(loginuser != null) {

			if(loginuser.getLastlogindategap() >= 12) {
				// 마지막으로 로그인 한 날짜가 현재일로 부터 1년(12개월)이 지났으면 해당 로그인 계정을 비활성화 시킨다.
				loginuser.setIdleStatus(true); // 비활성화
			}
			else {
				if(loginuser.getPwdchangegap() > 3) {
					// 마지막으로 암호를 변경한 날짜가 3개월이 지났으면
					loginuser.setRequirePwdChange(true);
				}
				// 마지막 로그인 한 날짜시간 변경(기록)하기
				dao.setLastLoginDate(paraMap);
				
				try {
					loginuser.setEmail(aes.decrypt(loginuser.getEmail())); // loginuser의 email을 복호화 하도록 한다.
				} catch (UnsupportedEncodingException | GeneralSecurityException e) {
					e.printStackTrace();
				} 
				
			}
		}
		return loginuser;
	}


	
	// === #55. 글쓰기 (파일첨부가 없는 글쓰기) === //
	@Override
	public int add(BoardVO boardvo) {

		// === #140. 원글쓰기인지, 답변 글쓰기인지 구분하기  === // -------------------------
		if(boardvo.getFk_seq() == null || boardvo.getFk_seq().trim().isEmpty()) {
			// 원글쓰기인지
			int groupno = dao.getGroupnoMax()+1;
			boardvo.setGroupno(String.valueOf(groupno));
		}
		//-------------------------------------------------------------------------
	
		// db에 보내주기
		int n = dao.add(boardvo);
		return n;
	}


	// === #59. 페이징 처리를 안한 검색어가 없는 전체 글목록 보여주기 === // 
	@Override
	public List<BoardVO> getboardList() {

		List<BoardVO> boardList = dao.getboardList();
		
		return boardList;
	}

	
	// === #63. 글 조회수 증가와함께 글 한개 조회하기 === //
	// 먼저, 로그인을 한 상태에서 다른 사람의 글을 조회했을때 (조회수 1증가)
	@Override
	public BoardVO getView(String seq, String userid) {
						// userid는 로그인을 한 상태라면 로그인한 사용자의 id이고, 아니면 null
		
	//	System.out.println("------------- userid : "+ userid);
		
		BoardVO boardvo = dao.getView(seq);
		
		if(boardvo != null && userid != null &&
		   !boardvo.getFk_userid().equals(userid)) {
			// 글 조회수 증가는 다른 사람의 글을 읽을때만 증가하도록 해야한다.
			// 로그인하지 않은 상태에서는 글 조회수 증가는 없다.
			
			dao.setAddReadCount(seq); // 글 조회수 1 증가하기
			boardvo = dao.getView(seq);
		}
		
		return boardvo;
	}


	// === #70. 글조회수 증가는 없고 단순히 글 한개 조회만을 해주는 것 === //
	@Override
	public BoardVO getViewWithNoAddCount(String seq) {
		
		BoardVO boardvo = dao.getView(seq);
		
		return boardvo;
	}


	// === #73. 글 한개 수정하기 === //
	@Override
	public int edit(BoardVO boardvo) {
		
		int n = dao.updateBoard(boardvo);
		
		return n;
	}


	// === #78. 글 삭제하기 === //
	/*
	@Override
	public int delete(HashMap<String, String> paraMap) {
		int n = dao.deleteBoard(paraMap);
		return n;
	}
	*/
	
	// 먼저, #78번을 주석처리 한 다음 아래와 같이 한다.
	// === #96. 1개 글 삭제하기 (딸린 댓글이 있는 경우 댓글도 동시에 삭제). 트랜잭션처리해야 함 === //
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public int delete(HashMap<String, String> paraMap) throws Throwable {
		
		int n = 0;
		
		dao.deleteComment(paraMap); // 딸린 댓글 삭제(딸린 댓글이 없을수도 있지만 실행)
		n = dao.deleteBoard(paraMap); // 1개 글을 삭제
		
		return n;
	}
	
	
	// === AOP 에서 사용하는 것으로 회원에게 포인트를 주기 위한 것 === // 
	@Override
	public void pointPlus(HashMap<String, String> paraMap) {
		dao.pointPlus(paraMap);
		
	}

	// === #85. 댓글쓰기 ===
	
	// tblComment 테이블에 insert 된 다음에 
	// tblBoard 테이블에 commentCount 컬럼이 1증가(update) 하도록 요청한다.
	// 즉, 2개이상의 DML 처리를 해야하므로 Transaction 처리를 해야 한다.
	// >>>>> 트랜잭션처리를 해야할 메소드에 @Transactional 어노테이션을 설정하면 된다. 
	// rollbackFor={Throwable.class} 은 롤백을 해야할 범위를 말하는데 Throwable.class 은 error 및 exception 을 포함한 최상위 루트이다. 즉, 해당 메소드 실행시 발생하는 모든 error 및 exception 에 대해서 롤백을 하겠다는 말이다.
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public int addComment(CommentVO commentvo) throws Throwable {
		
		int result = 0;
		int n = 0;
		
		n = dao.addComment(commentvo); // 댓글쓰기(tblComment 테이블에 insert) 
		if(n==1) {
			result = dao.updateCommentCount(commentvo.getParentSeq()); // tblBoard 테이블에 commentCount 컬럼의 값을 1증가(update) 
		}
		
		return result;
	}


	// === #91. 원게시물에 딸린 댓글 보여주기 === // 
	@Override
	public List<CommentVO> getCommentList(String parentSeq) {
		List<CommentVO> commentList = dao.getCommentList(parentSeq);
		return commentList;
	}


	// == #101. 페이징 처리를 안한 검색어가 있는 전체 글목록 보여주기 === //
	@Override
	public List<BoardVO> boardListSearch(HashMap<String, String> paraMap) {
		List<BoardVO> boardList = dao.boardListSearch(paraMap);
		return boardList;
	}


	// === #107. 검색어 입력시 자동글 완성하기 4 === //
	@Override
	public List<String> wordSearchShow(HashMap<String, String> paraMap) {
		List<String> wordList = dao.wordSearchShow(paraMap);
		return wordList;
	}


	// === 스프링 스케줄러 연습 1 === //
	//////////////////////////////////////////////////////////////////////////////////////////////
	/*
	스케줄은 3가지 종류  cron, fixedDelay, fixedRate 가 있다. 
	
	@Scheduled(cron="0 0 0 * * ?")
	cron 스케줄에 따라서 일정기간에 시작한다. 매일 자정마다 (00:00:00)에 실행한다.
	
	>>> cron 표기법 <<<
	
	문자열의 좌측부터 우측까지 아래처럼 의미가 부여되고 각 항목은 공백 문자로 구분한다.
	
	순서는 초 분 시 일 월 요일명 이다.
	----------------------------------------------------------------------------------------------------------------    
	의미             초               분              시             일                         월             요일명                                                                   년도
	----------------------------------------------------------------------------------------------------------------
	사용가능한	0~59     0~59     0~23      1~31           1~12      1~7 (1=>일요일, 2=>월요일, ... 7=>토요일)     1970 ~ 2099 
	값           - * /    - * /    - * /     - * ? / L W    - * /     - * ? / L #
	
	* 는 모든 수를 의미.
	
	? 는 해당 항목을 사용하지 않음.  
	일에서 ?를 사용하면 월중 날짜를 지정하지 않음. 요일명에서 ?를 사용하면 주중 요일을 지정하지 않음.
	
	- 는 기간을 설정. 시에서 10-12이면 10시, 11시, 12시에 동작함.
	분에서 57-59이면 57분, 58분, 59분에 동작함.
	
	, 는 특정 시간을 지정함. 요일명에서 2,4,6 은 월,수,금에만 동작함.
	
	/ 는 시작시간과 반복 간격 설정함. 초위치에 0/15로 설정하면 0초에 시작해서 15초 간격으로 동작함. 
	분위치에 5/10으로 설정하면 5분에 시작해서 10분 간격으로 동작함.
	
	L 는 마지막 기간에 동작하는 것으로 일과 요일명에서만 사용함. 일위치에 사용하면 해당월의 마지막 날에 동작함.
	         요일명에 사용하면 토요일에 동작함.
	
	W 는 가장 가까운 평일에 동작하는 것으로 일에서만 사용함.  일위치에 15W로 설정하면 15일이 토요일이면 가장 가까운 14일 금요일에 동작함.
	     일위치에 15W로 설정하고 15일이 일요일이면 16일에 동작함.
	     일위치에 15W로 설정하고 15일이 평일이면 15일에 동작함.
	  
	LW 는 L과 W의 조합.                             그달의 마지막 평일에 동작함.
	
	# 는 몇 번째 주와 요일 설정함. 요일명에서만 사용함.    요일명위치에 6#3이면 3번째 주 금요일에 동작함.
	   요일명위치에 4#2이면 2번째 주 수요일에 동작함.
	
	
	※ cron 스케줄 사용 예
	0 * * * * *             ==> 매 0초마다 실행(즉, 1분마다 실행함)
	
	* 0 * * * *             ==> 매 0분마다 실행(즉, 1시간마다 실행함)
	
	0 * 14 * * *            ==> 14시에 0분~59분까지 1분 마다 실행
	
	* 10,50 * * * *         ==> 매 10분, 50분 마다 실행
	, : 여러 값 지정 구분에 사용 
	
	0 0/10 14 * * *         ==> 14시 0분 부터 시작하여 10분 간격으로 실행(즉, 6번 실행함)
	/ : 초기값과 증가치 설정에 사용
	* 
	0 0/10 14,18 * * *      ==> 14시 0분 부터 시작하여 10분 간격으로 실행(6번 실행함) 그리고 
	==> 18시 0분 부터 시작하여 10분 간격으로 실행(6번 실행함)
	/ : 초기값과 증가치 설정에 사용 
	, : 여러 값 지정 구분에 사용 
	
	0 0 12 * * *            ==> 매일 12(정오)시에 실행
	0 15 10 * * *           ==> 매일 오전 10시 15분에 실행
	0 0 14 * * *            ==> 매일 14시에 실행
	
	0 0 0/6 * * *        ==> 매일 0시 6시 12시 18시 마다 실행
	- : 범위 지정에 사용  / : 초기값과 증가치 설정에 사용
	
	0 0/5 14-18 * * *    ==> 매일 14시 부터 18시에 시작해서 0분 부터 매 5분간격으로 실행
	/ : 증가치 설정에 사용
	
	0 0-5 14 * * *          ==> 매일 14시 0분 부터 시작해서 14시 5분까지 1분마다 실행   
	- : 범위 지정에 사용
	
	0 0 8 * * 2-6           ==> 평일 08:00 실행 (월,화,수,목,금)  
	
	0 0 10 * * 1,7          ==> 토,일 10:00 실행 (토,일) 
	
	0 0/5 14 * * ?          ==> 아무요일, 매월, 매일 14:00부터 14:05분까지 매분 0초 실행 (6번 실행됨)
	
	0 15 10 ? * 6L          ==> 매월 마지막 금요일 아무날의 10:15:00에 실행
	
	0 15 10 15 * ?          ==> 아무요일, 매월 15일 10:15:00에 실행 
	
	* /1 * * * *            ==> 매 1분마다 실행
	
	* /10 * * * *           ==> 매 10분마다 실행 
	
	
	>>> fixedDelay <<<
	이전에 실행된 task의 종료시간으로부터 정의된 시간만큼 지난 후 다음에 task를 실행함. 단위는 밀리초임.
	@Scheduled(fixedDelay=1000)
	
	>>> fixedRate <<<
	이전에 실행된 task의 시작 시간으로부터 정의된 시간만큼 지난 후 다음 task를 실행함. 단위는 밀리초임.
	@Scheduled(fixedRate=1000)
	
	*/
//	=== Spring Scheduler(스프링 스케줄러)를 사용한 email 발송하기 === //
//		매일 새벽 4시마다 고객이 예약한 이틀 전에 해당하는 고객들에게 (DAO에서 List<String>(이메일)   
	
	@Override
//	@Scheduled(cron="0 * * * * *")
	@Scheduled(cron="0 0 4 * * *")
	public void scheduleTest1() {
		//  ※ 주의 : 스케줄러로 사용되는 메소드는 반드시 파라미터가 없어야 한다 !!
		
		// === 현재시각 나타내기 === //
		Calendar currentDate = Calendar.getInstance(); // 현재날짜와 시간을 알아온다.
		SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		System.out.println("현재시각 -> "+df.format(currentDate.getTime()));
		
		// === 메일 발송하기 === //
		
		
	}


	// === #113. 총 게시물 건수 구하기 === //
	@Override
	public int getTotalCount(HashMap<String, String> paraMap) {
		int totalCount = dao.getTotalCount(paraMap);
		return totalCount;
	}


	// === #116. 페이징 처리한 글목록 가져오기 (검색이 있든지 없든지 모두 다 포함) === //
	@Override
	public List<BoardVO> boardListSearchWithPaging(HashMap<String, String> paraMap) {
		List<BoardVO> boardList = dao.boardListSearchWithPaging(paraMap);
		return boardList;
	}

	
	// === #127. 원게시물에 딸린 댓글들을 페이징처리해서 조회해오기(Ajax 로 처리) === //
	@Override
	public List<CommentVO> getCommentListPaging(HashMap<String, String> paraMap) {
		List<CommentVO> commentList = dao.getCommentListPaging(paraMap);
		return commentList;
	}

	
	// === #131. 원게시물에 딸린 댓글 getCommentTotalPage 알아오기(Ajax 로 처리) === //
	@Override
	public int getCommentTotalCount(HashMap<String, String> paraMap) {
		int totalCount = dao.getCommentTotalCount(paraMap);
		return totalCount;
	}


	// === #151. 글쓰기 (파일첨부가 있는 글쓰기) === // 
	@Override
	public int add_withFile(BoardVO boardvo) {

		// === 원글쓰기인지, 답변 글쓰기인지 구분하기  === // -------------------------
		if(boardvo.getFk_seq() == null || boardvo.getFk_seq().trim().isEmpty()) {
			// 원글쓰기인지
			int groupno = dao.getGroupnoMax()+1;
			boardvo.setGroupno(String.valueOf(groupno));
		}
		//-------------------------------------------------------------------------
	
		// db에 보내주기
		int n = dao.add_withFile(boardvo); // 첨부파일이 있는 경우
		return n;

	}

}
