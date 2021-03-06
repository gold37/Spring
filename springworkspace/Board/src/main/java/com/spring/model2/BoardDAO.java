package com.spring.model2;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

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
	
	
}
