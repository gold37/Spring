package com.spring.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.board.model.BoardVO;
import com.spring.common.Sha256;
import com.spring.member.model.MemberVO;
import com.spring.model.TestVO;
import com.spring.service.InterBoardService;

/*
사용자 웹브라우저 요청(View)  ==> DispatcherServlet ==> @Controller 클래스 <==>> Service단(핵심업무로직단, business logic단) <==>> Model단[Repository](DAO, DTO) <==>> myBatis <==>> DB(오라클)           
(http://...  *.action)                                  |                                                                                                                              
 ↑                                                View Resolver
 |                                                      ↓
 |                                                View단(.jsp 또는 Bean명)
 -------------------------------------------------------| 

사용자(클라이언트)가 웹브라우저에서 http://localhost:9090/board/test_insert.action 을 실행하면
배치서술자인 web.xml 에 기술된 대로  org.springframework.web.servlet.DispatcherServlet 이 작동된다.
DispatcherServlet 은 bean 으로 등록된 객체중 controller 빈을 찾아서  URL값이 "/test_insert.action" 으로
매핑된 메소드를 실행시키게 된다.                                               
Service(서비스)단 객체를 업무 로직단(비지니스 로직단)이라고 부른다.
Service(서비스)단 객체가 하는 일은 Model단에서 작성된 데이터베이스 관련 여러 메소드들 중 관련있는것들만을 모아 모아서
하나의 트랜잭션 처리 작업이 이루어지도록 만들어주는 객체이다.
여기서 업무라는 것은 데이터베이스와 관련된 처리 업무를 말하는 것으로 Model 단에서 작성된 메소드를 말하는 것이다.
이 서비스 객체는 @Controller 단에서 넘겨받은 어떤 값을 가지고 Model 단에서 작성된 여러 메소드를 호출하여 실행되어지도록 해주는 것이다.
실행되어진 결과값을 @Controller 단으로 넘겨준다.
*/

// === #30. 컨트롤러 선언 === 
@Component
/* XML에서 빈을 만드는 대신에 클래스명 앞에 @Component 어노테이션을 적어주면 해당 클래스는 bean으로 자동 등록된다. 
     그리고 bean의 이름(첫글자는 소문자)은 해당 클래스명이 된다.
     여기서는 @Controller 를 사용하므로 @Component 기능이 이미 있으므로 @Component를 명기하지 않아도 BoardController 는 bean 으로 등록되어 스프링컨테이너가 자동적으로 관리해준다. 
*/

@Controller
public class BoardController {
	
	// === #35. 의존객체 주입하기(DI: Dependency Injection) ===
	// ※ 의존객체주입(DI : Dependency Injection)  ex) 휴대폰의 의존객체는 배터리. 배터리없이는 핸드폰 못함
	//  ==> 스프링 프레임워크는 객체를 관리해주는 컨테이너를 제공해주고 있다.
	//      스프링 컨테이너는 bean으로 등록되어진 BoardController 클래스 객체가 사용되어질때, 
	//      BoardController 클래스의 인스턴스 객체변수(의존객체)인 BoardService service 에 
	//      자동적으로 bean 으로 등록되어 생성되어진 BoardService service 객체를  
	//      BoardController 클래스의 인스턴스 변수 객체로 사용되어지게끔 넣어주는 것을 의존객체주입(DI : Dependency Injection)이라고 부른다. 
	//      이것이 바로 IoC(Inversion of Control == 제어의 역전) 인 것이다.
	//      즉, 개발자가 인스턴스 변수 객체를 필요에 의해 생성해주던 것에서 탈피하여 스프링은 컨테이너에 객체를 담아 두고, 
	//      필요할 때에 컨테이너로부터 객체를 가져와 사용할 수 있도록 하고 있다. 
	//      스프링은 객체의 생성 및 생명주기를 관리할 수 있는 기능을 제공하고 있으므로, 더이상 개발자에 의해 객체를 생성 및 소멸하도록 하지 않고
	//      객체 생성 및 관리를 스프링 프레임워크가 가지고 있는 객체 관리기능을 사용하므로 Inversion of Control == 제어의 역전 이라고 부른다.  
	//      그래서 스프링 컨테이너를 IoC 컨테이너라고도 부른다.
	
	//  IOC(Inversion of Control) 란 ?
	//  ==> 스프링 프레임워크는 사용하고자 하는 객체를 빈형태로 이미 만들어 두고서 컨테이너(Container)에 넣어둔후
	//      필요한 객체사용시 컨테이너(Container)에서 꺼내어 사용하도록 되어있다.
	//      이와 같이 객체 생성 및 소멸에 대한 제어권을 개발자가 하는것이 아니라 스프링 Container 가 하게됨으로써 
	//      객체에 대한 제어역할이 개발자에게서 스프링 Container로 넘어가게 됨을 뜻하는 의미가 제어의 역전 
	//      즉, IOC(Inversion of Control) 이라고 부른다.
	
	
	//  === 느슨한 결합 ===
	//      스프링 컨테이너가 BoardController 클래스 객체에서 BoardService 클래스 객체를 사용할 수 있도록 
	//      만들어주는 것을 "느슨한 결합" 이라고 부른다.
	//      느스한 결합은 BoardController 객체가 메모리에서 삭제되더라도 BoardService service 객체는 메모리에서 동시에 삭제되는 것이 아니라 남아 있다.
	
	// ===> 단단한 결합(개발자가 인스턴스 변수 객체를 필요에 의해서 생성해주던 것)
	// private InterBoardService service = new BoardService(); 
	// ===> BoardController 객체가 메모리에서 삭제 되어지면  BoardService service 객체는 멤버변수(필드)이므로 메모리에서 자동적으로 삭제되어진다.

	@Autowired
	private  InterBoardService service; // 의존 객체
	
	@RequestMapping(value="/test_insert.action")
	public String test_insert(HttpServletRequest request) {

		int n = service.test_insert();
		
		String message = "";
		
		if(n>0) {
			message = "데이터 입력 성공 !";
		}
		else {
			message = "데이터 입력 실패 !";
		}
		
		request.setAttribute("message", message);
		request.setAttribute("n", n);
		
		return "test_insert";
		//  	/WEB-INF/views/test_insert.jsp 페이지를 만들어야 함
	}
	
	@RequestMapping(value="/test_select.action")
	// 웹 브라우저에서 위의 url주소를 실행시키면 아래의 메소드가 실행됨
	public String test_select(HttpServletRequest request) {
							// request 저장소에 담아서 넘김
		
		HashMap<String, List<TestVO>> map = service.test_select();
		
		request.setAttribute("testvoList", map.get("testvoList"));
		request.setAttribute("testvoList2", map.get("testvoList2"));
		
		return "test/test_select"; 
		//  	/WEB-INF/views/test/test_select.jsp 페이지를 만들어야 함		
	}

	@RequestMapping(value="/test/test_form.action", method= {RequestMethod.GET})
	// 웹 브라우저에서 위의 url주소를 실행시키면 아래의 메소드가 실행됨
	public String test_form() {

		return "test/testForm";
		// 		/WEB-INF/views/test/testForm.jsp 페이지를 만들어야 함	
	}

	@RequestMapping(value="/test/test_formEnd.action") 
								/* URL에 대한 메소드를 명기하지않으면 get, post 둘 다 됨. 명기하면 그것만! */
	// 웹 브라우저에서 위의 url주소를 실행시키면 아래의 메소드가 실행됨
	public String test_formEnd(HttpServletRequest request) {
		
		String no = request.getParameter("no");
		String name = request.getParameter("name");
		
		HashMap<String, String> paraMap = new HashMap<>();
		paraMap.put("no", no);
		paraMap.put("name", name);
		
		int n = service.test_insert(paraMap);
		
		/*
		String message = "";
		
		if(n>0) {
			message = "데이터 입력 성공 !";
		}
		else {
			message = "데이터 입력 실패 !";
		}
		
		request.setAttribute("message", message);
		request.setAttribute("n", n);
		
		return "test_insert";
		*/
		
		
		if(n>0) {
			return "redirect:/test_select.action"; // 전송하면 성공 메세지로 안가고 바로 select 페이지로 감 
		}
		else {
			return "redirect:/test/test_form.action";
		}
	}
	
	
	
	@RequestMapping(value="/ajaxtest/ajaxtest_form.action", method= {RequestMethod.GET})
	// 웹 브라우저에서 위의 url주소를 실행시키면 아래의 메소드가 실행됨
	public String ajaxtest_form() {

		return "test/ajaxtestForm";
		// 		/WEB-INF/views/test/ajaxtestForm.jsp 페이지를 만들어야 함	
	}
	
	
	/*
	@RequestMapping(value="/ajaxtest/insert.action", method= {RequestMethod.POST})
	public String ajaxtestInsert(HttpServletRequest request) {
		
		String no = request.getParameter("no");
		String name = request.getParameter("name");
		// no와 name을 받아옴
		
		// map에 담음
		HashMap<String, String> paraMap = new HashMap<>();
		paraMap.put("no", no);
		paraMap.put("name", name);
		
		// 의존객체인 service에 넘김 ( 느슨한 결합, 제어의 역전 )
		int n = service.ajaxtest_insert(paraMap);
		// n은 성공유무를 알기 위한 변수(1이면 성공)
		
		// 결과물인 n을 JSON 형식으로 만들어주기
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		String json = jsonObj.toString();
		
		request.setAttribute("json", json);
		
		return "jsonview";
		// 		/WEB-INF/views/jsonview.jsp 파일을 생성해야 한다.
		
	}
	*/
	
	@ResponseBody
	@RequestMapping(value="/ajaxtest/insert.action", method= {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String ajaxtestInsert(HttpServletRequest request) {
		
		String no = request.getParameter("no");
		String name = request.getParameter("name");
		// no와 name을 받아옴
		
		// map에 담음
		HashMap<String, String> paraMap = new HashMap<>();
		paraMap.put("no", no);
		paraMap.put("name", name);
		
		// 의존객체인 service에 넘김 ( 느슨한 결합, 제어의 역전 )
		int n = service.ajaxtest_insert(paraMap);
		// n은 성공유무를 알기 위한 변수(1이면 성공)
		
		// 결과물인 n을 JSON 형식으로 만들어주기
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
		
	}
	
	
	/*
	@RequestMapping(value="/ajaxtest/select.action")
	public String ajaxtest_select(HttpServletRequest request) {
		
		List<TestVO> testvoList = service.ajaxtest_select();
		
		JSONArray jsonArr = new JSONArray();
		
		for(TestVO vo : testvoList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("no", vo.getNo());
			jsonObj.put("name", vo.getName());
			jsonObj.put("writeday", vo.getWriteday());
			
			jsonArr.put(jsonObj);
		}
		String json = jsonArr.toString();
		request.setAttribute("json", json);
		
		return "jsonview";
	}
	*/
	
	/*
    @ResponseBody 란?
	  메소드에 @ResponseBody Annotation이 되어 있으면 return 되는 값은 View 단 페이지를 통해서 출력되는 것이 아니라 
	 return 되어지는 값 그 자체를 웹브라우저에 바로 직접 쓰여지게 하는 것이다. 일반적으로 JSON 값을 Return 할때 많이 사용된다.  
	
	>>> 스프링에서 json 또는 gson을 사용한 ajax 구현시 데이터를 화면에 출력해 줄때 한글로 된 데이터가 '?'로 출력되어 한글이 깨지는 현상이 있다. 
               이것을 해결하는 방법은 @RequestMapping 어노테이션의 속성 중 produces="text/plain;charset=UTF-8" 를 사용하면 
               응답 페이지에 대한 UTF-8 인코딩이 가능하여 한글 깨짐을 방지 할 수 있다. <<< 
   */
	@ResponseBody
	@RequestMapping(value="/ajaxtest/select.action", produces="text/plain;charset=UTF-8")
														// 추가해줘야 JSON에서 한글 안깨짐
	public String ajaxtest_select(HttpServletRequest request) {
		
		List<TestVO> testvoList = service.ajaxtest_select();
		
		JSONArray jsonArr = new JSONArray();
		
		for(TestVO vo : testvoList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("no", vo.getNo());
			jsonObj.put("name", vo.getName());
			jsonObj.put("writeday", vo.getWriteday());
			
			jsonArr.put(jsonObj);
		}
		
		return jsonArr.toString(); // 웹페이지에 출력 될 값 (@ResponseBody 추가만 해주면 이제 request ~ 안해도 됨)
	}
	
	
	// == 데이터테이블즈(datatables) -- datatables 1.10.19 기반으로 작성  == // 
	@RequestMapping(value="/datatables_test.action")
	public String datatables_test(HttpServletRequest request) {
		
		List<TestVO> testvoList = service.datatables_test();
		
		request.setAttribute("testvoList", testvoList);
				
		return "test/datatables_test";
        //	   /WEB-INF/views/test/datatables_test.jsp 페이지를 만들어야 한다.
	}
	
	
	// == return 타입을 String 대신 ModelAndView를 사용해보기
	@RequestMapping(value="/datatables_test2.action")
	public ModelAndView datatables_test2(ModelAndView mav) {
		
		List<TestVO> testvoList = service.datatables_test();
		
		mav.addObject("testvoList", testvoList); // 뷰 단으로 보낼 데이터 
		// request.setAttribute("testvoList", testvoList); 와 같은 말
		
		mav.setViewName("test/datatables_test");// view 단의 파일이름 지정하기
		//		  	 /WEB-INF/views/test/datatables_test.jsp 페이지를 만들어야 한다.
		
		return mav;
	}
	
	
	@RequestMapping(value="/tiles1/datatables_test.action")
	public String datatables_test_tiles1(HttpServletRequest request) {
		
		List<TestVO> testvoList = service.datatables_test();
		
		request.setAttribute("testvoList", testvoList);
		
		return "test/datatables_test.tiles1";
		//	   /WEB-INF/views/tiles1/test/datatables_test.jsp 페이지를 만들어야 한다.
	}
	
	
	@RequestMapping(value="/tiles2/datatables_test.action")
	public String datatables_test_tiles2(HttpServletRequest request) {
		
		List<TestVO> testvoList = service.datatables_test();
		
		request.setAttribute("testvoList", testvoList);
		
		return "test/datatables_test.tiles2";
		//	   /WEB-INF/views/tiles2/test/datatables_test.jsp 페이지를 만들어야 한다.
	}

	
	
	
	@RequestMapping(value="/test/employees.action")
	public ModelAndView employees_select(ModelAndView mav) {
		
		List<TestVO> empvoList = service.employees_test();
		
		mav.addObject("empvoList", empvoList);
		
		mav.setViewName("test/employees.tiles1");
		//	   /WEB-INF/views/tiles1/test/employees.jsp 페이지를 만들어야 한다.
		
		return mav;
	}
	
	

	/*	또는 HashMap으로 해보기 ↓
	@RequestMapping(value="/test/employees.action")
	public ModelAndView employees_select(ModelAndView mav) {
		
		List<HashMap<String, String>> empvoList = service.employees_test();
		
		mav.addObject("empvoList", empvoList);
		
		mav.setViewName("test/employees.tiles1");
		//	   /WEB-INF/views/tiles1/test/employees.jsp 페이지를 만들어야 한다.
		
		return mav;
	}
	*/
	
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	
	
	// === #36. 메인 페이지 요청 === //
	@RequestMapping(value="/index.action")
	public ModelAndView index(ModelAndView mav) {
		
		List<String> imgfilenameList = service.getImgfilenameList();
		
		mav.addObject("imgfilenameList", imgfilenameList);
		mav.setViewName("main/index.tiles1"); // imgfilenameList정보를 넘겨줄 뷰단
		// 	/WEB-INF/views/tiles1/{1}/{2}.jsp 이러한 모양의 파일을 생성해야함(tiles-layout.xml에 정의돼있음)
		
		return mav;
	}
	
	
	// === #40. 로그인 폼 페이지 요청 === // 
	@RequestMapping(value="/login.action")
	public ModelAndView login(ModelAndView mav) {
		
		mav.setViewName("login/loginform.tiles1");
		//	 	/WEB-INF/views/tiles1/login/loginform.jsp 파일을 생성한다.
		
		return mav;
	}

	
	// === #41. 로그인 처리하기 === // 
	@RequestMapping(value="/loginEnd.action", method= {RequestMethod.POST})
	public ModelAndView loginEnd(HttpServletRequest request, ModelAndView mav) {
		
		String userid = request.getParameter("userid");
		String pwd = request.getParameter("pwd");
		
		HashMap<String, String> paraMap = new HashMap<>();
		paraMap.put("userid", userid);
		paraMap.put("pwd", Sha256.encrypt(pwd)); // 암호를 암호화해서 넣어줌
		
		MemberVO loginuser = service.getLoginMember(paraMap); // paraMap에 들어간 정보 DB에 보내기
		
		HttpSession session = request.getSession();
		
		if(loginuser == null) {
			
			String msg = "아이디 또는 암호가 틀립니다!";
			String loc = "javascript:history.back()";
			
			mav.addObject("msg", msg);
			mav.addObject("loc", loc);
			
			mav.setViewName("msg");
			// 	/WEB-INF/views/msg.jsp 파일을 생성한다.
		}
		else {
			if(loginuser.isIdleStatus() == true) {
				// 로그인을 한지 1년이 지나서 휴면상태에 빠진 경우
				String msg = "로그인한지 1년이 지나 휴면계정입니다. 관리자에게 문의 바랍니다.";
				String loc = "javascript:history.back()";
				
				mav.addObject("msg", msg);
				mav.addObject("loc", loc);
				
				mav.setViewName("msg");
				// 	/WEB-INF/views/msg.jsp 파일을 생성한다.
			}
			else {
				if(loginuser.isRequirePwdChange() == true) {
					// 암호를 최근 3개월 동안 변경하지 않은 경우 (로그인은 돼야함)
					session.setAttribute("loginuser", loginuser);
					
					String msg = "암호를 변경한지 3개월이 지났습니다. 보안을 위해 비밀번호를 변경해주세요.";
					String loc = request.getContextPath()+"/myinfo.action";
								// 	/board/myinfo.action
					
					mav.addObject("msg", msg);
					mav.addObject("loc", loc);
					
					mav.setViewName("msg");
				}
				
				else {
					// 로그인, 비번 둘 다 아무 이상이 없는 경우
					session.setAttribute("loginuser", loginuser);
					
					mav.setViewName("login/loginEnd.tiles1");
					//	/WEB-INF/views/tiles1/login/loginEnd.jsp 파일을 생성한다.
					
				}
			}
			
		}
		
		return mav;
	}
	
	// === 나의 정보 수정 페이지 === //
	@RequestMapping(value="/myinfo.action")
	public String myinfo() {
		return "login/myinfo.tiles1";
		//		/WEB-INF/views/tiles1/login/myinfo.jsp 파일을 생성한다.
	}
	
	
	// === #50. 로그아웃 처리하기 === //
	@RequestMapping(value="/logout.action")
	public ModelAndView logout(HttpServletRequest request, ModelAndView mav) {

		HttpSession session = request.getSession();
		session.invalidate(); // 로그인 돼있는 정보 없애기
		
		String msg = "로그아웃 되었습니다.";
		String loc = request.getContextPath()+"/index.action";
		
		mav.addObject("msg", msg);
		mav.addObject("loc", loc);
		
		mav.setViewName("msg");
		
		return mav;
		
	}
	
	
	// === #51. 게시판 글쓰기 폼페이지 요청 === //
	@RequestMapping(value="/add.action")
	public ModelAndView requiredLogin_add(ModelAndView mav) {
		
		mav.setViewName("board/add.tiles1");
		//		/WEB-INF/views/tiles1/board/add.jsp 파일을 생성한다.
		
		return mav;
	}
	
	
	// === #54. 게시판 글쓰기 완료 요청 === //
	@RequestMapping(value="/addEnd.action", method= {RequestMethod.POST})
	public String addEnd(BoardVO boardvo) {
		
		/*
			== 확인용 ==
		
		System.out.println("1. 글쓴이 아이디 : "+boardvo.getFk_userid());
		System.out.println("2. 글쓴이 이름 : "+boardvo.getName());
		System.out.println("3. 제목 : "+boardvo.getSubject());
		System.out.println("4. 내용 : "+boardvo.getContent());
		System.out.println("5. 글 비밀번호 : "+boardvo.getPw());
		 
		 */
		
		int n = service.add(boardvo);
		
		if(n==1) {
			return "redirect:/list.action";
		}
		else {
			return "redirect:/add.action";
		}
	}
	
	// === #58. 글목록 보기 페이지 요청 === // 
	@RequestMapping(value="/list.action")
	public ModelAndView list(HttpServletRequest request, ModelAndView mav) {
		
		// 페이징 처리를 안한 검색어가 없는 전체 글목록 보여주기
		List<BoardVO> boardList = service.getboardList();
		
		
		/////////////////////////////////////////////////////////////////				
		// === #69. 글조회수(readCount)증가 (DML문 update)는
		//          반드시 목록보기에 와서 해당 글제목을 클릭했을 경우에만 증가되고,
		//          웹브라우저에서 새로고침(F5)을 했을 경우에는 증가가 되지 않도록 해야 한다.
		//          이것을 하기 위해서는 session 을 사용하여 처리하면 된다.

		HttpSession session = request.getSession();
		session.setAttribute("readCountPermission", "yes");
		
		/*
		   session 에  "readCountPermission" 키값으로 저장된 value값은 "yes" 이다.
		   session 에  "readCountPermission" 키값에 해당하는 value값 "yes"를 얻으려면 
		      반드시 웹브라우저에서 주소창에 "/list.action" 이라고 입력해야만 얻어올 수 있다. 
		*/
		/////////////////////////////////////////////////////////////////
		
		mav.addObject("boardList", boardList);
		mav.setViewName("board/list.tiles1");

		return mav;
	}
	
	
	// === #62. 글 한개를 보여주는 페이지 요청 === // 
	@RequestMapping(value="/view.action")
	public ModelAndView view(HttpServletRequest request, ModelAndView mav) {
		
		// 조회하고자 하는 글 번호 받아오기
		String seq = request.getParameter("seq");
		
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

		String userid = null;
		
		if(loginuser != null) {
			userid = loginuser.getUserid(); // 로그인 한 user의 id를 담음 
		}
		
		// === #68. !!! 중요 !!! === //
	    //	     글1개를 보여주는 페이지 요청은 select 와 함께 
	    //     DML문(지금은 글조회수 증가인 update문)이 포함되어져 있다.
	    //     이럴경우 웹브라우저에서 페이지 새로고침(F5)을 했을때 DML문이 실행되어
	    //     매번 글조회수 증가가 발생한다.
	    //     그래서 우리는 웹브라우저에서 페이지 새로고침(F5)을 했을때는
	    //     단순히 select만 해주고 DML문(지금은 글조회수 증가인 update문)은 
	    //     실행하지 않도록 해주어야 한다. !!! === //
	
		BoardVO boardvo = null;
		
		// 위의 글목록보기 #69. 에서 session.setAttribute("readCountPermission", "yes"); 해두었다.
		if("yes".equals(session.getAttribute("readCountPermission"))) {
			// 글 목록보기를 클릭한 다음 특정글을 조회해온 경우
			
			// 조회수 올리기 (로그인된 상태에서 내가 쓴 글이 아닌 글을 눌렀을때만 count)
			boardvo = service.getView(seq, userid); // select(글 한개 가져오기) 와 update(조회수 올리기) 가 동시에 일어나야 함
			
			session.removeAttribute("readCountPermission"); // ☆★☆★ session에 저장된 readCountPermission 지우기
			
		}
		else {
			// 웹브라우저에서 새로고침(F5)을 클릭한 경우
			
			boardvo = service.getViewWithNoAddCount(seq);
			// 글조회수 증가는 없고 단순히 글 한개 조회만을 해주는 것
			
		}
		
		mav.addObject("boardvo", boardvo);
		mav.setViewName("board/view.tiles1");
		
		return mav;
	}
	
	
	// === #71. 글 수정 페이지 요청 === // 
	@RequestMapping(value="/edit.action")
	public ModelAndView requiredLogin_edit(HttpServletRequest request, ModelAndView mav) {

		String seq = request.getParameter("seq"); // 수정해야할 글 번호 가져옴
		
		BoardVO boardvo = service.getViewWithNoAddCount(seq); // 수정해야 할 글 한개 가져오기
		// 글 조회수 (readCount) 증가 없이 그냥 글 한개만 가져오는 것

		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
		
		if( !loginuser.getUserid().equals(boardvo.getFk_userid()) ) {
			String msg = "다른 사용자의 글은 수정이 불가합니다.";
			String loc = "javascript:history.back()";
			
			mav.addObject("msg", msg);
			mav.addObject("loc", loc);
			mav.setViewName("msg");
		}
		else {
			// 자신의 글을 수정할 경우
			// 가져온 한 개 글을 글 수정 폼이 있는 view 단으로 보내준다.
			mav.addObject("boardvo", boardvo);
			mav.setViewName("board/edit.tiles1");
		}
		return mav;
	}
	
	
	// === #72. 글수정 페이지 완료하기 === //
	@RequestMapping(value="/editEnd.action", method= {RequestMethod.POST})
	public ModelAndView editEnd(HttpServletRequest request, BoardVO boardvo, ModelAndView mav) {
		
		/* 글 수정을 하려면 원본글의 글 암호와 수정시 입력해준 암호가 일치할때만 글 수정이 가능하도록 해야한다. */
		int n = service.edit(boardvo);
		
		if(n==0) {
			mav.addObject("msg", "암호가 일치하지않아 글 수정이 불가합니다.");
		}
		else {
			mav.addObject("msg", "글 수정 완료");
		}
		
		mav.addObject("loc", request.getContextPath()+"/view.action?seq="+boardvo.getSeq());
		mav.setViewName("msg");
		
		return mav;
	}
	
	
	// === #76. 글삭제 요청하기 === //
	@RequestMapping(value="/delete.action")
	public ModelAndView delete(HttpServletRequest request, ModelAndView mav) {

		String seq = request.getParameter("seq"); // 삭제해야 할 글 번호를 받아온다.
		
		BoardVO boardvo = service.getViewWithNoAddCount(seq);

		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

		if( !loginuser.getUserid().equals(boardvo.getFk_userid()) ) {
			String msg = "다른 사용자의 글은 삭제가 불가합니다.";
			String loc = "javascript:history.back()";
			
			mav.addObject("msg", msg);
			mav.addObject("loc", loc);
			mav.setViewName("msg");
		}
		else {
			// 자신의 글을 삭제할 경우
			// 가져온 한 개 글을 글 삭제 폼이 있는 view 단으로 보내준다.
			mav.addObject("seq", seq);
			mav.setViewName("board/delete.tiles1");
		}
		return mav;
	}
	
	
	// === #77. 글삭제 하기 === //
	@RequestMapping(value="/deleteEnd.action", method= {RequestMethod.POST}) 
	public ModelAndView deleteEnd(HttpServletRequest request, ModelAndView mav) {
		
		String seq = request.getParameter("seq");
		String pw = request.getParameter("pw");
		
		HashMap<String, String> paraMap = new HashMap<>();
		paraMap.put("seq", seq);
		paraMap.put("pw", pw);

		int n = service.delete(paraMap);

		if(n==0) {
			mav.addObject("msg", "암호가 일치하지않아 삭제가 불가합니다.");
			mav.addObject("loc", request.getContextPath()+"/view.action?seq="+seq);
		}
		else {
			mav.addObject("msg", "삭제 완료");
			mav.addObject("loc", request.getContextPath()+"/list.action");
		}
		
		mav.setViewName("msg");
		
		return mav;
	}
	
	
}