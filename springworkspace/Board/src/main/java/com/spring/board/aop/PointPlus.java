package com.spring.board.aop;

import java.util.HashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.service.InterBoardService;

// #53. 공통관심사 클래스(Aspect 클래스) 생성
@Aspect // 공통관심사 클래스 객체로 등록
@Component // bean으로 등록
public class PointPlus {
	/*
	 * 주업무(글쓰기, 글수정, 댓글쓰기,...)를 실행하기 전 로그인을 해야함. 업무에 대한 보조업무(로그인) 객체로 로그인 여부를 체크하는
	 * 관심클래스(Aspect클래스)를 생성하여 포인트컷(주업무)과 어드바이스(보조업무)가 동작하도록 한다.
	 */
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

	
}