<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% 
	String ctxPath = request.getContextPath();
%>



<link rel="stylesheet" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css" type="text/css"/>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>

<script type="text/javascript">


	$(document).ready(function(){
		
		$("#exampleTbl").DataTable({
			
			"dom": '<"top"lf>rt<"bottom"ip>',  // 페이지바가 우측하단에 위치, 검색이 우측상단에 위치하는 것임.  
	         //   "dom": '<"top"lp>rt<"bottom"if>',  // 페이지바가 우측상단에 위치, 검색이 우측하단에 위치하는 것임.  
	        "paging"    : true,                // 우측하단에 페이지바를 보여줄것인지 아닌지를 결정하는 것
         // "paging"    : false,               // 우측하단에 페이지바를 보여줄것인지 아닌지를 결정하는 것 
            "ordering"  : false,                // th에 나오는 값에 따라서 오름차순, 내림차순 정렬을 할것인지 말것인지를 결정하는 것  
            "info"      : true,                // 좌측하단에 보여지는 Showing 1 to 10 of 22 entries 를 보여줄것인지 말것인지를 결정하는 것       
            "filter"    : true,                // 우측상단에 보여지는 검색을 보여줄것인지 말것인지를 결정하는 것       
            "lengthChange"   : false,           // 좌측상단에 보여지는 한페이지당 행의갯수를 결정하는 것  
            "order"     : [[2, "asc"],[0, "desc"]], // 기본 컬럼 정렬 설정  숫자 0은 첫번째 컬럼을 말하며, "asc" 또는 "desc"를 설정할 수 있다. 
            "stateSave" : false,
         // "stateSave" : true, 
            /*
                               클라이언트 사이드 렌더링의 경우 새고로침 시 페이징 현재 상태가 저장되지 않으나
                  stateSave를 true로 활성화할 경우 새로고침 시에도 페이징 되어진 현재 상태가 저장되어 그대로 보여지게 된다.
                  stateSave를 false로 활성화할 경우 새로고침 시에는 초기화 되어 처음의 상태로 보여지게 된다.
            */
            "pagingType" : "full_numbers",  // "numbers" , "simple" , "simple_numbers" , "full" , "full_numbers" , "first_last_numbers" 
         //   "scrollX" : false,   // x 축 스크롤을 활성화 할지의 여부 
         //   "scrollY" : "250px", // y 축의 스크롤의 크기 
         //   "scrollCollapse" : true // y 축의 스크롤을 활성화 여부. 당연히 y축의 스크롤이 활성화 되면 paging이 안 일어나게 할 수도 있으므로  그렇게 하려면 "paging" 을 false로 바꿀수도 있다.  
	         
	         
			
		});
		
	});
	

</script>


</head>
<body>
   <h3>내 오라클 서버의 정보</h3>
   <table id="exampleTbl" class="table-striped"> 
   <thead>
      <tr>
         <th>번호</th>
         <th>사원번호</th>
         <th>이름</th>
         <th>연봉</th>
         <th>성별</th>
         <th>나이</th>
      </tr>
   </thead>
   <tbody>
      <c:forEach var="emp" items="${empList}" varStatus="status">
         <tr>
            <td>${status.count}</td>
            <td>${emp.EMPLOYEE_ID}</td>
            <td>${emp.ENAME}</td>
            <td>${emp.YEARPAY}</td>
            <td>${emp.GENDER}</td>
            <td>${emp.AGE}</td>
         </tr>
      </c:forEach>
   </tbody>
   </table>

</body>
</html>