<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
</script>
<style>
.tbDetail th, .tbDetail td {
    padding: 5px 5px 5px 5px;
}
</style>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  			name="pageIndex"				value="${pageIndex}"/>
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">

 	<input type="hidden" name="p_process" value="">
	<input type="hidden" name="p_action"  value="">
	<input type="hidden" name="p_grcode"  value="">
	<input type="hidden" name="p_subj"  value="">
	<input type="hidden" name="p_examnum"  value="">
	<input type="hidden" name="p_examtype"  value="">
	<input type="hidden" name="p_chknum"  value="">
            
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType" value="AA_member">조회조건타입 : 타입별 세부조회조건</c:param>
		<c:param name="selectParameter" value="">조회조건 추가 : admSearchParameter.jsp 추가</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
	<div class="listTop">			
		총 회원수 : <fmt:formatNumber value="${memberCnt}" groupingUsed="true"/> 명
	</div>
	<!-- detail wrap -->
    <div class="tbDetail">    
		<table summary="" cellspacing="0"  width="100%">
			<caption>목록</caption>
			<colgroup>
			<col width="" />
			<col width="" />
			<col width="" />     
			<col width="" />           
			<col width="" />           
		</colgroup>
		<thead>
			<tr>
				<th scope="row" style="text-align: center;">1년~5년</th>
				<th scope="row" style="text-align: center;">6년~10년</th>
				<th scope="row" style="text-align: center;">11년~15년</th>
				<th scope="row" style="text-align: center;">16년~20년</th>
				<th scope="row" style="text-align: center;">20년 이상</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${resultList}" var="result" varStatus="status">
				<tr>
					<td style="text-align: center;"><fmt:formatNumber value="${result.code1}" groupingUsed="true"/></td>
					<td style="text-align: center;"><fmt:formatNumber value="${result.code2}" groupingUsed="true"/></td>
					<td style="text-align: center;"><fmt:formatNumber value="${result.code3}" groupingUsed="true"/></td>
					<td style="text-align: center;"><fmt:formatNumber value="${result.code4}" groupingUsed="true"/></td>
					<td style="text-align: center;"><fmt:formatNumber value="${result.code5}" groupingUsed="true"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
		
<div id="chart_div" style="width: 100%; height: 500px;"></div>
<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
$("#ses_search_gyear option:eq(0)").remove();

google.load("visualization", "1", {packages:["corechart"]});
google.setOnLoadCallback(drawChart);

$(document).ready(function(){
	$("#search_att option:selected").val('${search_att}');
	
	chart($("#ses_search_att").val());
	attChange();
});
var pData = null;
function drawChart(data) {  
	var data = google.visualization.arrayToDataTable([
		['Element', '회원', {role: 'style'}, {role: 'annotation'}]
	]);
	
	if(pData != null && pData != "") {
		data = pData;
	}
	var view = new google.visualization.DataView(data);
    view.setColumns([
    	0
    	, 1
    	, { calc: "stringify",
			sourceColumn: 1,
			type: "string",
			role: "annotation"}
    	, 2
    ]);
    
    var pWidth = 1500;
    var wid = new String($(".tbDetail").css("width")).split(".");
	pWidth = Number(wid[0]) + 100;
	
	var options = {
		title: $("#search_att option:selected").text()
		, hAxis: {title: $("#ses_search_gyear option:selected").val()+"년", titleTextStyle: {color: 'black'}}
		, width: pWidth
		, fontSize: 12
		, bar: {groupWidth: "70%"}
		, legend: {position: "none"}
	};
	var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));  chart.draw(data, options);
	chart.draw(view, options);
}
function chart() {
	var valParam = '${resultList}';
	var map = mapCreate(valParam);
	pData = google.visualization.arrayToDataTable([
		['Element', '회원', { role: 'style' }, { role: 'annotation' } ],
		['1년~5년', Number(map.get("code1")), '#550000', map.get("code1") ],
		['6년~10년', Number(map.get("code2")), '#005500', map.get("code2") ],
		['11년~15년', Number(map.get("code3")), '#000055', map.get("code3") ],
		['16년~20년', Number(map.get("code4")), '#aa0000', map.get("code4") ],
		['20년 이상', Number(map.get("code5"))	, '#00aa00', map.get("code5") ],
	]);
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if($("#ses_search_gyear").val() == '')
	{	
		alert("연도를 선택하세요");
		return;
	}

	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/sat/yearMemberList.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.target = "_self";
	thisForm.submit();
}

/*************************************************
*java단에서 넘어온 값은 {'1','2'}형태의 데이터를 map형태로 만들어 줌
*
***************************************************/
function mapCreate(paramMap) {
	var mapSet = new JMap();
	var resultList = paramMap;
    resultList = resultList.replace("{","");
    resultList = resultList.replace("}","");
    resultList = resultList.replace("[","");
    resultList = resultList.replace("]","");
    var TreeList = resultList.split(",");	
	for (var j = 0; j < TreeList.length; j++) {	
			var pam = TreeList[j].split("=");
			if( pam[1] != null &&  pam[1] != "" && pam[1] != "null"){
				mapSet.put(pam[0].trim(), pam[1]);			
			}else{
				mapSet.put(pam[0].trim(), "");	
			}
	}	
	return mapSet;
}
</script>

<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
