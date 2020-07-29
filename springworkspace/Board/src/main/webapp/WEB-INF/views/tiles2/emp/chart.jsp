<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script src="https://code.highcharts.com/modules/export-data.js"></script>
<script src="https://code.highcharts.com/modules/accessibility.js"></script>


<style>
	table {
		width: 300px;
	}
	
	table, th, td {
		border: solid 1px gray;
	}
	
	th {
		text-align: center;	
	}
</style>

<div align="center">
	<h2>우리 회사 사원 통계 정보 차트</h2>

	<form name="searchFrm" style="margin: 20px 0 50px 0;">
		<select name="searchType" id="searchType" style="height: 25px;">
			<option value="">통계선택</option>
			<option value="deptname">부서별 인원통계</option>
			<option value="gender">성별 인원통계</option>
			<option value="deptnameGender">부서별 성별 인원통계</option>
		</select>
	</form>
	
	<div id="chart_container" style="width: 70%;"></div>
	<div id="table_container" style="width: 90%;"></div>
</div>

<script type="text/javascript">
	
	$(document).ready(function(){
		
		$("#searchType").change("change", function(){
			// select 값을 읽어옴
			func_choice($(this).val());
		});
		
	}); // end of $(document).ready(function(){})---------------------------

	
	function func_choice(searchTypeVal) {
		
		switch (searchTypeVal) {
		
		case "": // 통계선택 선택시
			$("#chart_container").empty();
			break;
			
		case "deptname": // 부서별 인원통계 선택시
			$.ajax({
			//	url:"/board/chart/deptnameJSON.action",
			//	url:"http://192.168.50.56:9090/board/chart/deptnameJSON.action",  ==> CORS Policy 정책 때문에 안됨
				url:"/board/chart/remote_genderJSON.action", // ==> 해결책
				dataType:"JSON",
				success:function(json) {
					$("#chart_container").empty();
					$("#table_container").empty();
		
					var resultArr = []; // 배열 선언
					
					for(var i=0; i<json.length; i++) {
						var obj;
						if(json[i].department_name == "Shipping") {
							obj = {name: json[i].department_name,
									// 배열의 요소 중 하나만 가져오기 i
								   y: Number(json[i].percentage),
									// 점유율
								   sliced: true,
								   selected: true};
						}
						else {
							obj = {name: json[i].department_name,
									// 배열의 요소 중 하나만 가져오기 i
									   y: Number(json[i].percentage)
									// 점유율
									};
						}
						resultArr.push(obj); // 배열속에 객체 넣기 push
					}
					
				   // -------------------------------------------------------------------
					Highcharts.chart('chart_container', {
					    chart: {
					        plotBackgroundColor: null,
					        plotBorderWidth: null,
					        plotShadow: false,
					        type: 'pie'
					    },
					    title: {
					        text: '지원이네 회사 부서별 인원통계 ♥'
					    },
					    tooltip: {
					        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
					    },
					    accessibility: {
					        point: {
					            valueSuffix: '%'
					        }
					    },
					    plotOptions: {
					        pie: {
					            allowPointSelect: true,
					            cursor: 'pointer',
					            dataLabels: {
					                enabled: true,
					                format: '<b>{point.name}</b>: {point.percentage:.1f} %'
					            }
					        }
					    },
					    series: [{
					        name: '인원비율',
					        colorByPoint: true,
					        data: resultArr
					    }]
					});
					 // -------------------------------------------------------------------
					 
					 html = "<table>";
					 html += "<tr>" +
					 			"<th>부서명</th>" +
					 			"<th>인원수</th>" +
					 			"<th>점유율</th>" +
					 		 "</tr>";
					 		 
					 $.each(json, function(index, item){
						 html += "<tr>" +
							 		 "<td>"+item.department_name+"</td>" +
							 		 "<td>"+item.cnt+"</td>" +
							 		 "<td>"+Number(item.percentage)+"</td>" +
						 		 "</tr>";
					 });		 
					 		 
					 html += "</table>";
					 
					 $("#table_container").html(html);
					 
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}

			});
			break;
			
		case "gender": // 성별 인원통계 선택시
			$.ajax({
				url:"/board/chart/genderJSON.action",
				dataType:"JSON",
				success:function(json) {
					$("#chart_container").empty();
					$("#table_container").empty();
		
					var resultArr = []; // 배열 선언
					
					for(var i=0; i<json.length; i++) {
						var obj;
						if(json[i].gender == "여") {
							obj = {name: json[i].gender,
									// 배열의 요소 중 하나만 가져오기 i
								   y: Number(json[i].percentage),
									// 점유율
								   sliced: true,
								   selected: true};
						}
						else {
							obj = {name: json[i].gender,
									// 배열의 요소 중 하나만 가져오기 i
									   y: Number(json[i].percentage)
									// 점유율
									};
						}
						resultArr.push(obj); // 배열속에 객체 넣기 push
					}
					
				   // -------------------------------------------------------------------
					Highcharts.chart('chart_container', {
					    chart: {
					        plotBackgroundColor: null,
					        plotBorderWidth: null,
					        plotShadow: false,
					        type: 'pie'
					    },
					    title: {
					        text: '지원이네 회사 성별 인원통계 ♥'
					    },
					    tooltip: {
					        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
					    },
					    accessibility: {
					        point: {
					            valueSuffix: '%'
					        }
					    },
					    plotOptions: {
					        pie: {
					            allowPointSelect: true,
					            cursor: 'pointer',
					            dataLabels: {
					                enabled: true,
					                format: '<b>{point.name}</b>: {point.percentage:.1f} %'
					            }
					        }
					    },
					    series: [{
					        name: '인원비율',
					        colorByPoint: true,
					        data: resultArr
					    }]
					});
					 // -------------------------------------------------------------------
					 
					 html = "<table>";
					 html += "<tr>" +
					 			"<th>성별</th>" +
					 			"<th>인원수</th>" +
					 			"<th>점유율</th>" +
					 		 "</tr>";
					 		 
					 $.each(json, function(index, item){
						 html += "<tr>" +
							 		 "<td>"+item.gender+"</td>" +
							 		 "<td>"+item.cnt+"</td>" +
							 		 "<td>"+item.percentage+"</td>" +
						 		 "</tr>";
					 });		 
					 		 
					 html += "</table>";
					 
					 $("#table_container").html(html);
					 
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}

			});
			break;
			
		case "deptnameGender": // 부서별 성별 인원통계 선택시
			
			break;

		} // end of switch ---------------------
		
	}
	
	
</script>
