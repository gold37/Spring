<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String ctxPath = request.getContextPath();
	//		/board
%>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">

	th,td {
		border: solid 1px gray;
		border-collapse: collapse;
	}
	
</style>
<script type="text/javascript" src="<%= ctxPath%>/resources/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" >
	$(document).ready(function(){
		
		func_viewInfo();
		
		$("#btnSubmit").click(function(){
			
			$.ajax({
				url:"/board/ajaxtest/insert.action", // 상대경로 또는 절대경로 적어주기. 여기에 데이터를 넣어줌	
				type:"POST",
				data: {"no":$("#no").val()
					  ,"name":$("#name").val()}, // 보내기  
				dataType: "JSON",
				success: function(json){ // 성공,실패 알아오기
					if(json.n == 1) {
						func_viewInfo();
					}
				},	
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
					  
			});
		});
		
	}); // end of $(document).ready(function(){} -----------------
			
	function func_viewInfo() {
		
		/* alert("Comming soon ~~~~~~"); */
		
		$.ajax({
			url:"/board/ajaxtest/select.action",
			type:"GET",
			dataType:"JSON",
			success:function(json){
				// 결과물 json으로 ▲ 받아오겠음
				var html = "<table>";
					html += "<tr>";
					html += "<th>No</th>";
					html += "<th>입력번호</th>";
					html += "<th>성명</th>";
					html += "<th>작성일자</th>";
					html += "</tr>";
					html += "</br>";
					
				$.each(json, function(index, item){ // 반복문
					html += "<tr>";
					html += "<td>"+(index+1)+"</td>";
					html += "<td>"+item.no+"</td>";
					html += "<td>"+item.name+"</td>";
					html += "<td>"+item.writeday+"</td>";
					html += "</tr>";
				}); 	
				
				html += "</table>";
				
				$("#view").html(html);
						
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
			
		});
		
	} // end of function func_viewInfo() ------------------------
</script>

</head>
<body>
	<h2>AJAX 연습</h2>
	<form>
		번호 : <input type="text" id="no" /><br/>
		성명 : <input type="text" id="name" /><br/>
		<button type="button" id="btnSubmit">확인</button>
		<button type="reset">취소</button> 
		<!-- reset은 form속에 들어가 있어야 적용됨 -->
	</form>
	
	<div id="view">
		<!-- 결과물 보여줄 곳 -->
	</div>

</body>
</html>