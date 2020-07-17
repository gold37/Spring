package com.spring.board.aop;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import com.spring.common.MyUtil;

// === #53. 공통관심사 클래스(Aspect 클래스) 생성 === //

@Aspect		// 공통관심사 클래스 객체로 등록
@Component	// bean으로 등록
public class LoginCheck {
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
	public void checkLogin(JoinPoint joinPoint) {
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
	}
}