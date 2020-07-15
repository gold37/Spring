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
		
		
		// 쓰기 버튼 클릭시
		$("#btnWrite").click(function(){
			
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
			var frm = document.addFrm;
			frm.method = "POST";
			frm.action = "<%= ctxPath%>/addEnd.action";
			frm.submit();
			
		});
		
	}); // end of $(document).ready(function()) ---------------------
	
	
	function goPrint(title) {
		
		var sw = screen.width; // 화면의 가로길이
		var sh = screen.height; // 화면의 세로길이
		
		var popw = 800; // 팝업창 가로길이
		var poph = 600; // 팝업창 세로길이
		
		var xpos = (sw-popw)/2; // 화면 중앙에 팝업창 띄우기
		var ypos = (sh-popw)/2; // 화면 중앙에 팝업창 띄우기
		
		var popWin = window.open("","print", "width="+popw+", height="+poph+", top="+ypos+", left="+xpos+", status=yes, scrollbars=yes"); 
		// 윈도우 팝업창 띄우기
		
		popWin.document.open(); // 윈도우 팝업창에 내용물을 넣을 수 있도록 열어주기
	//	popWin.document.write(popContent); // 윈도우 팝업창에 내용물 넣기
		
		// 윈도우 팝업창에 내용 입력하기
		popWin.document.write("<html><head><style type='text/css'>*{color:purple;}</style><body onload='window.print()'>");
		popWin.document.write(document.getElementById("subject").value);
		popWin.document.write("<br/><pre>굿모닝")
		popWin.document.write("</pre></body></html>")
	
		popWin.document.close(); // 윈도우 팝업창 문서 닫기
		
		popWin.print(); // 윈도우 팝업창에 대한 인쇄창을 띄우고 인쇄를 하던가 
		popWin.close(); // 취소를 누르면 윈도우 팝업창을 닫음
		
	} // end of function goPrint(title) ------------------

</script>

<div style="padding-left: 10px;">
	<h1>글쓰기</h1>
	
	<!-- form 태그에 있는 name 값 + DB 테이블에 있는 컬럼값 + VO에 있는 값이 똑같아야함 !! 3박자가 맞아야 문제없이 잘 돌아간다 ~ -->
	<form name="addFrm">
		<table id="table">
			<tr>
				<th>성명</th>
				<td>
					<input type="hidden" name="fk_userid" value="${sessionScope.loginuser.userid}" />
					<input type="text" name="name" value="${sessionScope.loginuser.name}" class="short" readonly/>
				</td>
			</tr>
			<tr>
				<th>제목</th>
				<td>
					<input type="text" name="subject" id="subject" class="long" />
				</td>
			</tr>
			<tr>
				<th>내용</th>
				<td>
					<textarea rows="10" cols="100" style="width: 95%; height: 412px;" name="content" id="content" ></textarea>
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
			<button type="button" id="btnWrite">쓰기</button>
			<button type="button" onclick="javascript:history.back()">취소</button>
			<button type="button" onclick="goPrint()">인쇄</button>
		</div>
	</form>
</div>