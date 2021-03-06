<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% String ctxPath = request.getContextPath(); %>

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
		
		//goReadComment(); // 페이징 처리 안한 댓글 읽어오기 
	      goViewComment("1"); //페이징 처리한 댓글 읽어오기
		
	}); // end of $(document).ready(function(){})----------------
	
	
	
	// === 댓글쓰기 === //
	function goAddWrite() {
		var frm = document.addWriteFrm;
		var contentVal = frm.content.value.trim();
		if(contentVal=="") {
			alert("댓글 내용을 입력하세요!!");
			return;
		}
		
		var form_data = $("form[name=addWriteFrm]").serialize();
		
		$.ajax({
			url:"<%= request.getContextPath()%>/addComment.action",
			data:form_data,
			type:"POST",
			dataType:"JSON",
			success:function(json){
				if(json.n == 1) {
				//	goReadComment(); // 페이징 처리 안한 댓글 읽어오기 
				}
				else {
					alert("댓글쓰기 실패!!");
				}
				
				frm.content.value = "";
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
		
	}// end of function goAddWrite()----------------------
	
	
	// === 페이징 처리 안한 댓글 읽어오기  === //
	function goReadComment() {
		$.ajax({
			url:"<%= request.getContextPath()%>/readComment.action",
			data:{"parentSeq":"${boardvo.seq}"},
			dataType:"JSON",
			success:function(json){
				var html = "";
				if(json.length > 0) {
					$.each(json, function(index, item){
						html += "<tr>";
						html += "<td style='text-align: center;'>"+(index+1)+"</td>";
						html += "<td>"+item.content+"</td>";
						html += "<td style='text-align: center;'>"+item.name+"</td>";
						html += "<td style='text-align: center;'>"+item.regDate+"</td>";
						html += "</tr>";
					});
				}
				else {
					html += "<tr>";
					html += "<td colspan='4' style='text-align: center;'>댓글이 없습니다.</td>";
					html += "</tr>";
				}

				$("#commentDisplay").html(html);
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});	
	
	}// end of function goReadComment()--------------------

	
	
	// === #125. Ajax로 불러온 댓글내용을 페이징 처리하기  === //
	function goViewComment(currentShowPageNo) {
		$.ajax({
			url:"<%= request.getContextPath()%>/commentList.action",
			data:{"parentSeq":"${boardvo.seq}",
				  "currentShowPageNo":currentShowPageNo}, // 몇페이지를 보고싶은지
			dataType:"JSON",
			success:function(json){
				var html = "";
				if(json.length > 0) {
					$.each(json, function(index, item){
						html += "<tr>";
						html += "<td style='text-align: center;'>"+(index+1)+"</td>";
						html += "<td>"+item.content+"</td>";
						html += "<td style='text-align: center;'>"+item.name+"</td>";
						html += "<td style='text-align: center;'>"+item.regDate+"</td>";
						html += "</tr>";
					});
				}
				else {
					html += "<tr>";
					html += "<td colspan='4' style='text-align: center;'>댓글이 없습니다.</td>";
					html += "</tr>";
				}
				
				$("#commentDisplay").html(html);
				
				// 페이지바 함수 호출
				makeCommentPageBar(currentShowPageNo);
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});	
		
	}// end of function goReadComment()--------------------
	
	
	// === 댓글내용 페이지바 Ajax로 만들기 === // 
	function makeCommentPageBar(currentShowPageNo) {
		
		$.ajax({
			url:"<%= request.getContextPath()%>/getCommentTotalPage.action",
			data:{"parentSeq":"${boardvo.seq}",
				  "sizePerPage":"5"},
			type:"GET",
			dataType:"JSON",
			success:function(json) {
			//	console.log("전체 페이지 수 : " + json.totalPage);
				
				if(json.totalPage > 0) {
					// 댓글이 있는 경우
					
					var totalPage = json.totalPage;
					
					var pageBarHTML = "<ul style='list-style: none;'>";
					
					var blockSize = 10;
					
					var loop = 1;
					
					if(typeof currentShowPageNo == "string") {
						// 문자열이라면 뺄셈할 수 없기 때문에 if문으로 string인지 검사
						currentShowPageNo = Number(currentShowPageNo);
					}
					var pageNo = Math.floor( (currentShowPageNo - 1)/blockSize )* blockSize + 1;
							// 			(2-1)10		1/10	==> Math.floor(0.1)	 ==> 0
							//			(11-1)/10	10/10	==> Math.floor(1)	 ==> 1
							//			(12-1)/10	11/10	==> Math.floor(1.1)	 ==> 1
							
					
					// === [이전] 만들기 ===
					if(pageNo != 1) {
						
						pageBarHTML += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='javascript:goViewComment(\""+(pageNo-1)+"\")'>[이전]</a></li>";
					}
					
					while (!(loop > blockSize || pageNo > totalPage )) {

						
						if(pageNo == currentShowPageNo) {
							pageBarHTML += "<li style='display:inline-block; width:30px; font-size: 12pt; border: solid 1px solid; color: red; padding: 2px 4px;'>" + pageNo + "</li>";
						}
						else {
							pageBarHTML += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='javascript:goViewComment(\""+pageNo+"\")'>"+pageNo+"</a></li>";
						}
						
						loop ++;
						pageNo ++;
						
					} // end of while ----------------------------
					
					
					// === [다음] 만들기 ===
					if( !(pageNo > totalPage) ) {
					
						pageBarHTML += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='javascript:goViewComment(\""+pageNo+"\")'>[다음]</a></li>";		
					
					}
					
					pageBarHTML += "</ul>";
					
					$("#pageBar").html(pageBarHTML);
					pageBarHTML = "";
							
				}
				else {
					// 댓글이 없는 경우
					$("#pageBar").empty();
				}
			
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		
		});
		
	} // end of function makeCommentPageBar(currentShowPageNo) --------------- 
	
	
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
		
		<!-- === #158. 첨부파일 이름 및 파일크기를 보여주고 첨부파일 다운로드 하기  -->
		<tr>
			<th>첨부파일</th>
			<td>
				<c:if test="${sessionScope.loginuser != null }">
					<a href="<%= request.getContextPath()%>/download.action?seq=${boardvo.seq}"> ${boardvo.orgFilename} </a>
				</c:if>
				
				<c:if test="${sessionScope.loginuser == null }">
					${boardvo.orgFilename}
				</c:if>
			</td>
		</tr>
		
		<tr>
			<th>파일크기(bytes)</th>
			<td>
				${boardvo.fileSize}
			</td>
		</tr>

	</table>
	
	<br/>
	
	<div style="margin-bottom: 1%;">이전글&nbsp;:&nbsp;<span class="move" onclick="javascript:location.href='view.action?seq=${boardvo.previousseq}'">${boardvo.previoussubject}</span></div>
	<div style="margin-bottom: 1%;">다음글&nbsp;:&nbsp;<span class="move" onclick="javascript:location.href='view.action?seq=${boardvo.nextseq}'">${boardvo.nextsubject}</span></div>
	
	<br/>
	
	<button type="button" onclick="javascript:location.href='list.action'" >전체목록보기</button>
	
	<!-- === #124. === -->
	<button type="button" onclick="javascript:location.href='${gobackURL}'" >목록보기</button>
	
	<button type="button" onclick="javascript:location.href='edit.action?seq=${boardvo.seq}'" >수정</button>
	<button type="button" onclick="javascript:location.href='delete.action?seq=${boardvo.seq}'" >삭제</button>
	
	<!-- === #136. 어떤 글에 대한 답변 글쓰기는 로그인 된 회원의 gradelevel 값이 10인 회원만 가능하도록 함  === -->
	<c:if test="${sessionScope.loginuser.gradelevel == 10}"> 
		<button type="button" onclick="javascript:location.href='<%= ctxPath%>/add.action?fk_seq=${boardvo.seq}&groupno=${boardvo.groupno}&depthno=${boardvo.depthno}'" >답변 글쓰기</button>
	</c:if>


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
	
	<!-- === #134. 댓글 페이지바 === -->
	<div id="pageBar" style="padding-left: 100px; margin: 25px auto; width: 70%;"></div>
	
</div>