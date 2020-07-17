<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
	
table, th, td, input, textarea {
	border: solid gray 1px;
}

#table, #table2 {
	border-collapse: collapse;
 	width: 900px;
}

#table th, #table td{padding: 5px;}
#table th{width: 120px; background-color: #DDDDDD;}
#table td{width: 750px;}
.long {width: 470px;}
.short {width: 120px;}

.move {cursor: pointer;}
.moveColor {font-weight: bold;}

a {text-decoration: none !important;}


</style>

<script type="text/javascript">

	$(document).ready(function(){
		
		$(".move").hover(function(){
						$(this).addClass("moveColor");
					},
					function(){
						$(this).removeClass("moveColor");
					});
		
	});
	
	
</script>


<div style="">
	<h1>글 내용보기</h1>
	
	<table id="table" style="word-wrap: break-word; table-layout: fixted;">
		<tr>
			<th>글번호</th>
			<td>${boardvo.seq}</td>
		</tr>
		<tr>
			<th>성명</th>
			<td>${boardvo.name}</td>
		</tr>
		<tr>
			<th>제목</th>
			<td>${boardvo.subject}</td>
		</tr>
		<tr>
			<th>내용</th>
			<td>
				<p style="word-break: break-all;">${boardvo.content}</p>
				<%-- 
					style="word-break: break-all; 은 공백없는 긴영문일 경우 width 크기를 뚫고 나오는 것을 막는 것임. 
					그런데 style="word-break: break-all; 나 style="word-wrap: break-word; 은
			       	테이블태그의 <td>태그에는 안되고 <p> 나 <div> 태그안에서 적용되어지므로 <td>태그에서 적용하려면
			    	<table>태그속에 style="word-wrap: break-word; table-layout: fixed;" 을 주면 된다.
				--%>
			</td>
		</tr>
		<tr>
			<th>조회수</th>
			<td>${boardvo.readCount}</td>
		</tr>
		<tr>
			<th>작성일자</th>
			<td>${boardvo.regDate}</td>
		</tr>
	</table>
	
	<br/>
	
	<div style="margin-bottom: 1%;">이전글&nbsp;:&nbsp;<span class="move" onclick="javascript:location.href='view.action?seq=${boardvo.previousseq}'">${boardvo.previoussubject}</span></div>
	<div style="margin-bottom: 1%;">다음글&nbsp;:&nbsp;<span class="move" onclick="javascript:location.href='view.action?seq=${boardvo.nextseq}'">${boardvo.nextsubject}</span></div>
	
	<br/>
	
	<button type="button" onclick="javascript:location.href='list.action'" >목록보기</button>
	<button type="button" onclick="javascript:location.href='edit.action?seq=${boardvo.seq}'" >수정</button>
	<button type="button" onclick="javascript:location.href='delete.action?seq=${boardvo.seq}'" >삭제</button>
	
	
	<!-- === #83. 댓글쓰기 폼 추가 === -->
	<c:if test="${not empty sessionScope.loginuser}">
		<h3 style="margin-top: 50px;">댓글쓰기 및 보기</h3>
		<form name="addWriteFrm" style="margin-top: 20px;">
			      <input type="hidden" name="fk_userid" value="${sessionScope.loginuser.userid}" />
			성명 : <input type="text" name="name" value="${sessionScope.loginuser.name}" class="short" readonly />  
			&nbsp;&nbsp;
			댓글내용 : <input id="commentContent" type="text" name="content" class="long" /> 
			
			<!-- 댓글에 달리는 원게시물 글번호(즉, 댓글의 부모글 글번호) -->
			<input type="hidden" name="parentSeq" value="${boardvo.seq}" /> 
			
			<button id="btnComment" type="button" onclick="goAddWrite()">확인</button> 
			<button type="reset">취소</button> 
		</form>
	</c:if>
	<c:if test="${empty sessionScope.loginuser}">
		<h3 style="margin-top: 50px;">댓글보기</h3>
	</c:if>
	
	<!-- ===== #94. 댓글 내용 보여주기 ===== -->
	<table id="table2" style="margin-top: 2%; margin-bottom: 3%;">
		<thead>
		<tr>
		    <th style="width: 10%; text-align: center;">번호</th>
			<th style="width: 60%; text-align: center;">내용</th>
			<th style="width: 10%; text-align: center;">작성자</th>
			<th style="text-align: center;">작성일자</th>
		</tr>
		</thead>
		<tbody id="commentDisplay"></tbody>
	</table>
	
</div>