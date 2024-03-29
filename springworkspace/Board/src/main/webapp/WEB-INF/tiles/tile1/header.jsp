<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.net.InetAddress"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- ======= #27. tile1 중 header 페이지 만들기  ======= --%>
<%
	String ctxPath = request.getContextPath();

	// === #165. (웹채팅관련3) === 	
	// === 서버 IP 주소 알아오기   === (사용중인 IP주소가 유동IP 이라면 IP주소를 알아와야 한다.)
	InetAddress inet = InetAddress.getLocalHost(); 
	String serverIP = inet.getHostAddress();
	
	//System.out.println("serverIP : " + serverIP);
	// serverIP : 192.168.56.50
	
	// String serverIP = "192.168.50.65"; 만약에 사용중인 IP주소가 고정IP 이라면 IP주소를 직접입력해주면 된다.

	
	// === 서버 포트번호 알아오기   ===
	int portnumber = request.getServerPort();
	//System.out.println("portnumber : " + portnumber);
	// portnumber : 9090
	
	String serverName = "http://"+serverIP+":"+portnumber; 
	//System.out.println("serverName : " + serverName);
	//serverName : http://192.168.50.65:9090 
%>
<div align="center">
	<ul class="nav nav-tabs mynav">
		<li class="dropdown"><a class="dropdown-toggle"
			data-toggle="dropdown" href="#">Home <span class="caret"></span></a>
			<ul class="dropdown-menu">
				<li><a href="<%=ctxPath%>/index.action">Home</a></li>
				<li><a href="<%=ctxPath%>/deliciousStore.action">전국맛집</a></li>
				<li><a href="<%= serverName%><%=ctxPath%>/chatting/multichat.action">웹채팅</a></li>
			</ul></li>
		<li class="dropdown"><a class="dropdown-toggle"
			data-toggle="dropdown" href="#">게시판 <span class="caret"></span></a>
			<ul class="dropdown-menu">
				<li><a href="<%=ctxPath%>/list.action">목록보기</a></li>
				<%-- <c:if test="${sessionScope.loginuser != null}" > --%>
					<li><a href="<%=ctxPath%>/add.action">글쓰기</a></li>
				<%-- </c:if> --%>
				<li><a href="#">Submenu 1-3</a></li>
			</ul></li>
		<li class="dropdown"><a class="dropdown-toggle"
			data-toggle="dropdown" href="#">로그인 <span class="caret"></span></a>
			<ul class="dropdown-menu">
				<c:if test="${sessionScope.loginuser == null}">
				<li><a href="#">회원가입</a></li>
				<li><a href="<%=ctxPath%>/login.action">로그인</a></li>
				</c:if>
				
				<c:if test="${sessionScope.loginuser != null}">
				<li><a href="<%=ctxPath%>/myinfo.action">나의정보</a></li>
				<li><a href="<%=ctxPath%>/logout.action">로그아웃</a></li>
				</c:if>
			</ul></li>
		
		<!-- === #169. 제품등록(다중파일첨부)및 제품정보 메뉴 추가하기 === -->		
		<c:if test="${sessionScope.loginuser.gradelevel == 10 }">
		<li class="dropdown"><a class="dropdown-toggle"
			data-toggle="dropdown" href="#">제품등록(다중파일첨부) <span class="caret"></span></a>
			<ul class="dropdown-menu">
			    <li><a href="<%=ctxPath%>/product/addProduct.action">제품등록</a></li>
				<li><a href="<%=ctxPath%>/product/storeProduct.action">제품입고</a></li>
			</ul></li>
		</c:if>
		
		<li class="dropdown"><a class="dropdown-toggle"
			data-toggle="dropdown" href="#">인사관리 <span class="caret"></span></a>
			<ul class="dropdown-menu">
				<li><a href="<%=ctxPath%>/emp/empList.action">직원목록</a></li>
				<li><a href="<%=ctxPath%>/emp/chart.action">통계차트</a></li>
			</ul></li>
	
		<li class="dropdown"><a class="dropdown-toggle"
			data-toggle="dropdown" href="#">제품정보 <span class="caret"></span></a>
			<ul class="dropdown-menu">
				<li><a href="<%=ctxPath%>/product/listProduct.action">제품목록</a></li>
			</ul></li>
	
	
	<!-- === #49. 로그인이 성공되어지면 로그인되어진 사용자의 이메일 주소를 출력하기 === -->
	<c:if test="${sessionScope.loginuser != null}">
		<div style="float: right; margin-top: 0.5%; border: solid 0px red;">
		  <span style="color: navy; font-weight: bold; font-size: 10pt;">${sessionScope.loginuser.email}</span> 님 로그인중.. 
		</div>
	</c:if>
	
	</ul>
</div>