package com.spring.board.aop;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.spring.common.MyUtil;
import com.spring.service.InterBoardService;

//=== #53. 공통관심사 클래스(Aspect 클래스) 생성 === //

/* LoginCheck랑  PointPlus 합침 */

@Aspect		// 공통관심사 클래스 객체로 등록
@Component	// bean으로 등록
public class BoardAOP {
	
	// ============ Before Advice 만들기 ============
	
	/*
	  주업무(글쓰기, 글수정, 댓글쓰기,...)를 실행하기 전 로그인을 해야함.
	  업무에 대한 보조업무(로그인) 객체로 로그인 여부를 체크하는 관심클래스(Aspect클래스)를 생성하여
	  포인트컷(주업무)과 어드바이스(보조업무)가 동작하도록 한다.
	 */

	// Pointcut(주업무) 생성 
	// Pointcut: 공통관심사를 필요로 하는 메소드
	//											↓패키지 끝명  ↓이걸로 시작하는 메소드 ↓파라미터 갯수 상관x
	@Pointcut("execution(public * com.spring..*Controller.requiredLogin_*(..) )")
	//						      ↑패키지 시작명↑갯수상관없이 아무거나 들어와도됨
	public void requireLogin() {}
	
	
	// Before Advice(공통관심사, 보조업무)를 구현
	@Before("requireLogin()")
	public void loginChk(JoinPoint joinPoint) { // 로그인 유무 검사 메소드 작성하기
		// JoinPoint: 포인트컷 되어진 주업무의 메소드
		
		// 로그인 유무를 확인하기 위해 request를 통해 session을 얻어와야함
		HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0]; // 주업무 메소드의 첫 번째 파라미터
		HttpServletResponse response = (HttpServletResponse) joinPoint.getArgs()[1]; // 주업무 메소드의 두 번째 파라미터
		
		HttpSession session = request.getSession();
		
		if(session.getAttribute("loginuser") == null) {
			String msg = "먼저 로그인 하세요!";
			String loc = request.getContextPath()+"/login.action";
			request.setAttribute("msg", msg);
			request.setAttribute("loc", loc);
			
			// >>> 로그인 성공 후 로그인 하기 전 페이지로 돌아가는 작업 만들기 <<< //
	        // === 현재 페이지의 주소(URL)알아오기 ===
			String url = MyUtil.getCurrentURL(request);	
			
			// "문자열"에서 "찾고자하는 문자열"이 없으면 false를 반환
			if(url.endsWith("?null")) {
				url = url.substring(0, url.indexOf("?"));
			}
			
			session.setAttribute("gobackURL", url); // 세션에 url 정보를 저장
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/msg.jsp");
			
			try {
				dispatcher.forward(request, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
			
		}
	}// end of public void loginChk(JoinPoint joinPoint) ------------



	
	// ============ After Advice 만들기 ============
	@Autowired
	InterBoardService service;
	
	// Pointcut(주업무) 생성
	// Pointcut: 공통관심사를 필요로 하는 메소드
	@Pointcut("execution(public * com.spring..*Controller.pointPlus_*(..) )")
	public void pointPlus() {}
	
	// After Advice(공통관심사,보조업무)
	@SuppressWarnings("unchecked")  // 앞으로는 노란줄 경고 표시 하지 않겠다는 뜻
	@After("pointPlus()")
	public void userPointPlus(JoinPoint joinPoint) {
		// JoinPoint joinPoint는 포인트컷 되어진 주업무의 메소드이다.
		
		HashMap<String, String> paraMap = (HashMap<String, String>) joinPoint.getArgs()[0];
		
		service.pointPlus(paraMap);
	}

	
	// ============ Around Advice 만들기 ============
	/*
	 	Before ---> 보조업무1
	 	주업무
	 	After ---> 보조업무2
	 	
	 	보조업무1 + 보조업무2를 실행하도록 해주는 것이 Around Advice 이다.
	 	
	 */
	// Pointcut(주업무) 생성 
	// Pointcut: 공통관심사를 필요로 하는 메소드
	// Pointcut 생성시 public은 생략이 가능하다. 접근제한자를 생략하면 public이 있는 것으로 간주
	// 왜냐하면 외부에서 특정 메소드에 접근을 해야 하므로 접근 제한자는 무조건 public이어야 하기 때문이다.
	/*@Pointcut("execution(public * com.spring..*Controller.*(..))")*/
	@Pointcut("execution(public * com.spring..*Controller.speed_*(..))")
	public void pcut() {}
	
	// After Advice(공통관심사,보조업무)를 구현한다.
	// 주업무를 실행해서 마치는데까지 걸리는 시간을 측정하는 것을 보조업무로 보겠다.
	@Around("pcut()")
		   /*주업무*/
	public Object speedMeasurement(ProceedingJoinPoint joinPoint) {
		/*
			before와 after는 주업무가 joinpoint인데,
			Around Advice에서는 ProceedingJoinPoint joinPoint가
			포인트컷(pcut) 되어진 주업무의 메소드이다.
		*/
		
		// 보조업무 1 (Before)
		// 주업무시작을 알려줘야함 (Around Advice) 반드시 주업무를 명기할것 !
		// 보조업무 2 (After)
		
		// 보조업무1 (주업무를 시작하기 전 시간 기록하기 ) --------------
		System.out.println("===> 주업무 하기전 시간 기록 시작 <===");
		long startTime = System.currentTimeMillis();
		// --------------------------------------------------
		
		
		Object returnObj = null;
		try {
			returnObj = joinPoint.proceed(); // 주업무 실행 ☆★☆★ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			/*					  ▲	String ModelAndView 둘 다 받아올 수 있는 것이 Object
									그래서 proceed 리턴타입이 Object임
			*/
			System.out.println("returnObj => " + returnObj);
			//		   -- joinPoint.proceed(); 메소드를 실행함으로
			//		      Pointcut 에 설정된 주업무를 처리해주는 메소드가 호출되어 실행된다.
			//		           지금은  * com.spring..*Controller.*(..) 이다.
			//		
			//		  Object org.aspectj.lang.ProceedingJoinPoint.proceed() throws Throwable
		    //        Throwable 클래스는 예외처리를 할 수 있는 최상위 클래스이다. 
			//		  Throwable 클래스의 자식 클래스가 Exception 과 Error 클래스이다.
			//	    
			//		    그리고 joinPoint.proceed()메소드의 리턴값은 Object 이다.
			//		    이를 통해 Aspect 로 연결된 Original Method(주업무 메소드, 지금은 *Controller 클래스의 모든 메소드임)의 리턴값을 형변환(캐스팅)을 통하여 받을수 있다.

			if(returnObj instanceof ModelAndView) {
				ModelAndView mav = (ModelAndView) returnObj;
				String viewName = mav.getViewName(); // 뷰 단이 어딘지 찍어봄	
				System.out.println("view단 => " + viewName);
			}
			else if(returnObj instanceof String) {
				String viewName = (String) returnObj; 
				System.out.println("view단 => " + viewName);
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			// 보조업무 2
			long finishTime = System.currentTimeMillis();
			System.out.println("===> 주업무가 끝난 시간 기록 시작 <===");
			
			System.out.println(">>> 주업무 실행소요시간: " + (finishTime - startTime) + "ms <<<" ); 
			System.out.println("");

		}
		
		return returnObj;
		
		// 주업무 메소드인 public * com.spring..*Controller.*(..)를 실행하여 얻은 returnObj 값(ModelAndView 또는 String)을 
		// 주업무 메소드인 public * com.spring..*Controller.*(..) 쪽으로 넘겨준다는 것이다.
		// 그런데 만약에 Around Advice 의 메소드 return 타입을 Object로 하지 않고 void 로 한다라면  return 값이 없는 것이다.
		// 여기서 중요한 것은 joinPoint.proceed(); 를 실행하는 주업무 메소드의 리턴 타입이 ModelAndView 또는 String 인데 return 값을 없게 해주면 
		// 주업무 메소드를 실행 후 view단 페이지는 없으므로 안 보이게 된다. 
		// 그러므로 return 값이 있도록 return 을 반드시 명기해 주어야만 한다.
		
		/*
		   Around Advice 에 해당하는 메소드에서 return 을 명기하지 않을 경우에는 
		   Pointcut 대상이 되는 메소드​가 실행하는 쪽으로 Pointcut 대상이 되는 메소드를 실행해서 얻은 값을 return 시켜주지 못하고  null 을 리턴시켜준다. 
		     그러므로 만약에 Pointcut 대상이 되는 메소드의 return 타입이 null 을 받을 수 있는 void 나 Object는 문제가 없으나,
		   null을 받을 수 없는 int, double, boolean 같은 경우에는 오류가 발생한다. 
		     그러므로 Around Advice 에 해당하는 메소드에서​는 return 타입을 Object 로 명기하고 return 을 명기해 주어야 한다. 
		   return 되어지는 값이 int, double, boolean​ 이더라도 auto unBoxing 해서 리턴시켜주기 때문에 오류발생이 없게 된다. 
		*/

	}
	
	
}
