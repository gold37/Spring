<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String ctxPath = request.getContextPath();
%>    

<script type="text/javascript">
	var loginuser = "${sessionScope.loginuser}";
	var gobackURL = "${requestScope.gobackURL}";
	
	
	if(loginuser != null && (gobackURL != null && gobackURL != "")) {
		// 돌아갈 페이지가 있다면
		alert("${sessionScope.loginuser.name}님 로그인 성공");
		location.href="<%= ctxPath%>/"+gobackURL; 
		// 돌아갈 페이지
	}
	else if (loginuser != null && (gobackURL == null || gobackURL == "")) {
		alert("${sessionScope.loginuser.name}님 로그인 성공");
		location.href="<%= ctxPath%>/index.action";
								  // 시작페이지
	}
		
</script>
    