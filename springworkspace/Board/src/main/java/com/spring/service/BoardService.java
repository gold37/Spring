package com.spring.service;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.spring.board.model.BoardVO;
import com.spring.common.AES256;
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
	@Override
	public int delete(HashMap<String, String> paraMap) {
		int n = dao.deleteBoard(paraMap);
		return n;
	}



}