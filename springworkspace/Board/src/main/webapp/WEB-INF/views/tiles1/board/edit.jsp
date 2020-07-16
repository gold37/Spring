<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% String ctxPath = request.getContextPath(); %>
    
    
<style type="text/css">

table, th, td, input, textarea {border: solid gray 1px;}
	
	#table {border-collapse: collapse;
	 		width: 900px;
	 		}
	#table th, #table td{padding: 5px;}
	#table th{width: 120px; background-color: #DDDDDD;}
	#table td{width: 860px;}
	.long {width: 470px;}
	.short {width: 120px;}

</style>

<script type="text/javascript">

	$(document).ready(function(){
		
		
		// 완료 버튼 클릭시
		$("#btnUpdate").click(function(){
			
			// 글 제목 유효성 검사
			var subjectVal = $("#subject").val().trim();
			if(subjectVal == "") {
				alert("글 제목을 입력하세요");
				return;
			}
		
			// 글내용 유효성 검사
			var contentVal = $("#content").val().trim();
			if(contentVal == "") {
				alert("글 내용을 입력하세요");
				return;
			}
			
			// 글 암호 유효성 검사
			var pwVal = $("#pw").val().trim();
			if(pwVal == "") {
				alert("글 암호를 입력하세요");
				return;
			}
		
			// 폼(form) 전송(submit)하기
			var frm = document.editFrm;
			frm.method = "POST";
			frm.action = "<%= ctxPath%>/editEnd.action";
			frm.submit();
			
		});
		
	}); // end of $(document).ready(function()) ---------------------
	
	
</script>

<div style="padding-left: 10px;">
	<h1>글수정</h1>
	
	<!-- form 태그에 있는 name 값 + DB 테이블에 있는 컬럼값 + VO에 있는 값이 똑같아야함 !! 3박자가 맞아야 문제없이 잘 돌아간다 ~ -->
	<form name="editFrm">
		<table id="table">
			<tr>
				<th>성명</th>
				<td>
					<input type="hidden" name="seq" value="${boardvo.seq}" />
					${boardvo.name}
				</td>
			</tr>
			<tr>
				<th>제목</th>
				<td>
					<input type="text" name="subject" id="subject" class="long" value="${boardvo.subject}" />
				</td>
			</tr>
			<tr>
				<th>내용</th>
				<td>
					<textarea rows="10" cols="100" style="width: 95%; height: 412px;" name="content" id="content" >${boardvo.content}</textarea>
				</td>
			</tr>
			<tr>
				<th>글암호</th>
				<td>
					<input type="password" name="pw" id="pw" class="short" />
				</td>
			</tr>
		</table>
		
		<div style="margin: 20px;">
			<button type="button" id="btnUpdate">완료</button>
			<button type="button" onclick="javascript:history.back()">취소</button>
		</div>
	</form>
</div>