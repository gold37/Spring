<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
   
<% String ctxPath = request.getContextPath(); %>
    
<style type="text/css">
	table, th, td {border: solid 1px gray;}

    #table {width: 970px; border-collapse: collapse;}
    #table th, #table td {padding: 5px;}
    #table th {background-color: #DDD;}
     
    .subjectStyle {font-weight: bold;
                   color: navy;
                   cursor: pointer;} 
</style>

<script type="text/javascript">

	$(document).ready(function(){
		
		$(".subject").bind("mouseover", function(event){
			var $target = $(event.target);
			$target.addClass("subjectStyle");
		});
		
		$(".subject").bind("mouseout", function(event){
			var $target = $(event.target);
			$target.removeClass("subjectStyle");
		});
		
		$("#searchWord").keydown(function(event) {
			 if(event.keyCode == 13) {
				 // 엔터를 했을 경우
				 goSearch();
			 }
		 });
		
		// 검색시 검색조건 및 검색어 값 유지시키기 
		if(${paraMap != null}) {
			$("#searchType").val("${paraMap.searchType}");
			$("#searchWord").val("${paraMap.searchWord}");
		}
		
		
		<%-- === #105. 검색어 입력시 자동글 완성하기 2 === --%>
		$("#displayList").hide(); // 관련 검색어 창 뜨는거 일단 숨기기
		
		$("#searchWord").keyup(function(){
			
			var wordLength = $(this).val().length; // 검색어의 길이 알아오기
			
			if(wordLength == 0) {
				$("#displayList").hide();
			}
			else {
				$.ajax({
					url:"<%= request.getContextPath()%>/wordSearchShow.action",
					type:"GET",
					data:{searchType:$("#searchType").val(),
						/* ▲ getParameter해옴	▲ id의 value값 */
						  searchWord:$("#searchWord").val()},
					dataType:"JSON",
					success:function(json) {
						
						<%-- === #110. 검색어 입력시 자동글 완성하기 7 === --%>
						
						if(json.length > 0) {
							// 검색된 데이터가 있는 경우
							var html = "";
							
							$.each(json, function(entryIndex, item){
								var word = item.word;
								
								var index = word.toLowerCase().indexOf($("#searchWord").val().toLowerCase());
								/* 			글자.다 소문자로 바꿔주기.몇번째 위치하는지 알아오기(찾고자하는 글자) */
								
							//	console.log("index:" + index); // 검색어와 연관된 자동글 인덱스 값 콘솔창에 찍어보기
							
								var len = $("#searchWord").val().length;
								
								var result = "";
							
							//	console.log(word.substr(0, index)); // 연관된 글 검색어 앞까지의 문장 찍어보기
								
							//	console.log(word.substr(index)); // 입력한 단어부터 끝까지
							
							//	console.log(word.substr(index, len)); // 내가 쓴 글씨만 찍기
							
							//	console.log(word.substr(index+len)); // 검색어 뒤부터 끝까지 찍기
							
							    result += "<span style='color:blue;'>"+word.substr(0,index)+"</span><span style='color:red;'>"+word.substr(index,len)+"</span><span style='color:blue;'>"+word.substr(index+len)+"</span>"; 
								
							    html += "<span style='cursor:pointer;' class='result'>"+result+"</span><br/>"; // 다음줄로
							});
							
							$("#displayList").html(html); // 보여주기
							
							$("#displayList").show(); // 검색어가 있을때만 관련 검색어 창 보여줌
						}
						else {
							// 검색된 데이터가 없는 경우
							$("#displayList").hide();
						}
						
					},
				    error: function(request, status, error){
						alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
					}
					
					/* 
					http://localhost:9090/board/wordSearchShow.action?searchType=name&searchWord='지'
					이 경로로 들어가면 [{"word":"무지개"},{"word":"지원지원"}] 가 나오는걸 확인할 수 있음
					 */		
					
				});
				
				$("#displayList").show();	
			}
			
		}); // end of $("#searchWord").keyup(function(){})--------------
		
		<%-- === #111. 검색어 입력시 자동글 완성하기 8 === --%>
		$(document).on("click", ".result", function(){
			var word = $(this).text(); // 여러개 관련 검색어 중 하나 선택하면 그 문장만 잡아옴
		//	alert(word); 
			$("#searchWord").val(word); // 관련 검색어 선택하면 검색창에 올라오게함
			$("#displayList").hide(); // 그리고 남은 관련 검색어들 숨김
			goSearch();
		});
		
		
	 });// end of $(document).ready(function(){})-------------------
 
	 
	 function goView(seq) {
		 
		<%--  location.href = "<%= ctxPath%>/view.action?seq="+seq; --%>

		// === #122.
		// 페이징 처리되어진 후 특정글제목을 클릭하여 상세내용을 본 이후
		// 사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해
		// 현재 페이지 주소를 뷰단으로 넘겨준다.
		var frm = document.goViewFrm;
		frm.seq.value = seq;
		
		frm.method = "GET";
		frm.action = "view.action";
		frm.submit();
		 
	 } // end of function goView(seq) -------------------
	 
	 
	 function goSearch() {
		 var frm = document.searchFrm;
			 frm.method = "GET";
			 frm.action = "<%= request.getContextPath()%>/list.action";
			 frm.submit();
		 
	 } // end of function goSearch() -------------------
	 
</script>
	
<div style="padding-left: 3%;">
	<h2 style="margin-bottom: 30px;">글목록</h2>
	
	<table id="table">
		<tr>
			<th style="width: 70px;  text-align: center;">글번호</th>
			<th style="width: 360px; text-align: center;">제목</th>
			<th style="width: 70px;  text-align: center;">성명</th>
			<th style="width: 150px; text-align: center;">날짜</th>
			<th style="width: 70px;  text-align: center;">조회수</th>
			
			<%-- ===#155. 첨부파일이 있는지 없는지 이미지로 보여준다. === --%>
			<th style="width: 70px;  text-align: center;">첨부파일</th>
		</tr>	
		<c:forEach var="boardvo" items="${boardList}" varStatus="status">
			<tr>
				<td align="center">${boardvo.seq}</td>
				<td align="left">
				
				<%-- 댓글쓰기가 없는 게시판 START --%>
				<%-- <span class="subject" onclick="goView('${boardvo.seq}')">${boardvo.subject}</span> --%>
				<%-- 댓글쓰기가 없는 게시판 END --%>

				
				<%-- 댓글쓰기가 있는 게시판 START --%>
				<%-- 
				<c:if test="${boardvo.commentCount > 0}">
					<span class="subject" onclick="goView('${boardvo.seq}')">${boardvo.subject}&nbsp;<span style="vertical-align: super;">[<span style="color:red; font-size: 9pt; font-weight: bold;">${boardvo.commentCount}</span>]</span></span>
				</c:if>
				
				<c:if test="${boardvo.commentCount == 0}">
					<span class="subject" onclick="goView('${boardvo.seq}')">${boardvo.subject}</span>
				</c:if> 
				<%-- 댓글쓰기가 있는 게시판 END --%>
				
				
				<%-- 댓글쓰기 및 답변형 게시판 START --%>
				<!-- 답변글이 아닌 원글인 경우 -->
				<c:if test="${boardvo.depthno == 0}">
					<c:if test="${boardvo.commentCount > 0}">
						<span class="subject" onclick="goView('${boardvo.seq}')">${boardvo.subject}&nbsp;<span style="vertical-align: super;">[<span style="color:red; font-size: 9pt; font-weight: bold;">${boardvo.commentCount}</span>]</span></span>
					</c:if>
					<c:if test="${boardvo.commentCount == 0}">
						<span class="subject" onclick="goView('${boardvo.seq}')">${boardvo.subject}</span>
					</c:if> 
				</c:if>
				<!-- 답변글인 경우 -->
				<c:if test="${boardvo.depthno > 0}">
					<c:if test="${boardvo.commentCount > 0}">
						<span class="subject" onclick="goView('${boardvo.seq}')"><span style="color: red; font-weight: bold; padding-left: ${boardvo.depthno*20}px">┗Re&nbsp;</span>${boardvo.subject}&nbsp;<span style="vertical-align: super;">[<span style="color:red; font-size: 9pt; font-weight: bold;">${boardvo.commentCount}</span>]</span></span>
					</c:if>
					<c:if test="${boardvo.commentCount == 0}">
						<span class="subject" onclick="goView('${boardvo.seq}')"><span style="color: red; font-weight: bold; padding-left: ${boardvo.depthno*20}px">┗Re&nbsp;</span>${boardvo.subject}</span>
					</c:if> 
				</c:if>
				<%-- 댓글쓰기 및 답변형 게시판 END --%>
				
				</td>
				<td align="center">${boardvo.name}</td>
				<td align="center">${boardvo.regDate}</td>
				<td align="center">${boardvo.readCount}</td>
				
				<%-- === #156. 첨부파일이 있는지 없는지 이미지로 보여준다. === --%>
				<td align="center">
					<c:if test="${not empty boardvo.fileName}">
						<img src="<%= request.getContextPath() %>/resources/images/disk.gif" />
					</c:if>
				</td>
		</c:forEach>
	</table>
	
	
	<%-- === #120. 페이지바 보여주기 ===  --%>
	<div align="center" style="width: 70%; margin: 10px auto;">
		${pageBar}
	</div>
	
	
	<%-- === #99. 글검색 폼 추가하기 : 글제목, 글쓴이로 검색을 하도록 한다. === --%> 
	<form name="searchFrm" style="margin-top: 20px;">
		<select name="searchType" id="searchType" style="height: 26px;">
			<option value="subject">글제목</option>
			<option value="name">글쓴이</option>
		</select>
		<input type="text" name="searchWord" id="searchWord" size="40" autocomplete="off" /> 
		<button type="button" onclick="goSearch()">검색</button>
	</form>
	
	
	<%-- === #104. 검색어 입력시 자동글 완성하기 1 === --%>
	<div id="displayList" style="border: solid 1px gray; width: 318px; height: 100px; margin-left: 70px; margin-top: -1px; overflow: auto;">
	</div>
	
	
	<%-- === #122 === --%>	
	<form name="goViewFrm">
		<input type="hidden" name="seq" />
		<input type="hidden" name="gobackURL" value="${gobackURL}" />
	</form>
	
	
</div>		