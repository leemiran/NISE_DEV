<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<!--
 /**
  * @Class Name : yearMemberList.jsp
  * @Description : 운영현황 > 연도별 회원 현황
  *
  * @  수정일        	   수정자                 수정내용
  * @ -------      ---------    ---------------------------
  * @ 2014.12.23    유상도       			최초 생성
  */ 
  --> 
<jsp:scriptlet>
pageContext.setAttribute("cr", "\r");
pageContext.setAttribute("lf", "\n");
pageContext.setAttribute("crlf", "\r\n");
</jsp:scriptlet>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>


<style>
table.ex1 {width:97%; margin:0 auto; text-align:left; border-collapse:collapse; text-decoration: none; }
 .ex1 th, .ex1 td {padding:0px 10px;text-align:left;}
 .ex1 tbody tr td {background:#F3F5BB; text-align:left;}
 .ex1 tbody tr th {background:#F2F684; color:#1BA6B2; text-align:center; width:10%; border-right:1px solid #fff}
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
		<c:param name="selectViewType"      value="AA_member"			>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
<div class="listTop">			
                총 회원수 : <fmt:formatNumber value="${memberCnt}" groupingUsed="true"/> 명
		</div>
<!-- detail wrap -->
    <div class="tbDetail">    
        <table summary="" cellspacing="0"  width="100%" id="address">
                <caption>목록</caption>
                <colgroup>
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />     
                <col width="" />           
            </colgroup>
            <thead>
					<tr >
						<th scope="row" style="text-align: center;">서울</th>
						<th scope="row" style="text-align: center;">부산</th>
						<th scope="row" style="text-align: center;">대구</th>
						<th scope="row" style="text-align: center;">인천</th>
						<th scope="row" style="text-align: center;">광주</th>
						<th scope="row" style="text-align: center;">대전</th>
						<th scope="row" style="text-align: center;">울산</th>
						<th scope="row" style="text-align: center;">경기</th>
						<th scope="row" style="text-align: center;">강원</th>
						<th scope="row" style="text-align: center;">충청북도</th>
						<th scope="row" style="text-align: center;">충청남도</th>
						<th scope="row" style="text-align: center;">전라북도</th>
						<th scope="row" style="text-align: center;">전라남도</th>
						<th scope="row" style="text-align: center;">경상북도</th>
						<th scope="row" style="text-align: center;">경상남도</th>
						<th scope="row" style="text-align: center;">제주</th>
						<th scope="row" style="text-align: center;">국립</th>
						<th scope="row" style="text-align: center;">세종</th>
						<th scope="row" style="text-align: center;">복지관외</th>
					</tr>
				</thead>
            <tbody id="yearlist">
                <c:forEach items="${resultList}" var="result" varStatus="status">
                	<tr  onclick="chart('<c:out value="${result}"/>');">
                	    
	                    <td  style="text-align: right;"><input type="hidden" id="dept" value="${result}"/><fmt:formatNumber value="${result.deptcd00001}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00002}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00003}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00004}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00005}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00006}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00007}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00008}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00009}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00010}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00011}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00012}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00013}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00014}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00015}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00016}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00017}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00018}" groupingUsed="true"/></td>
	                    <td  style="text-align: right;"><fmt:formatNumber value="${result.deptcd00019}" groupingUsed="true"/></td>
                	</tr>
                </c:forEach>
            </tbody>
        </table>
        
        <table summary="" cellspacing="0"  width="100%" id="sex">
                <caption>목록</caption>
                <colgroup>
                <col width="" />
                <col width="" />
            </colgroup>
            <thead>
					<tr >
						<th scope="row" style="text-align: center;">남성</th>
						<th scope="row" style="text-align: center;">여성</th>
					</tr>
				</thead>
            <tbody id="yearlist">
                <c:forEach items="${resultList}" var="result" varStatus="status">
                	<tr  onclick="chart('<c:out value="${result}"/>');">
	                    <td  style="text-align: center;"><input type="hidden" id="dept" value="${result}"/><fmt:formatNumber value="${result.sexF}" groupingUsed="true"/></td>
	                    <td  style="text-align: center;"><fmt:formatNumber value="${result.sexM}" groupingUsed="true"/></td>
                	</tr>
                </c:forEach>
            </tbody>
        </table>
        
        <table summary="" cellspacing="0"  width="100%" id="upperclass">
                <caption>목록</caption>
                <colgroup>
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
            </colgroup>
            <thead>
					<tr >
						<th scope="row" style="text-align: center;">교원</th>
						<th scope="row" style="text-align: center;">보조</th>
						<th scope="row" style="text-align: center;">교육전문직</th>
						<th scope="row" style="text-align: center;">일반</th>
					</tr>
				</thead>
            <tbody id="yearlist">
                <c:forEach items="${resultList}" var="result" varStatus="status">
                	<tr  onclick="chart('<c:out value="${result}"/>');">
	                     <td  style="text-align: center;"><input type="hidden" id="dept" value="${result}"/><fmt:formatNumber value="${result.empGubunT}" groupingUsed="true"/></td>
	                    <td  style="text-align: center;"><fmt:formatNumber value="${result.empGubunE}" groupingUsed="true"/></td>
	                    <td  style="text-align: center;"><fmt:formatNumber value="${result.empGubunR}" groupingUsed="true"/></td>
	                    <td  style="text-align: center;"><fmt:formatNumber value="${result.empGubunW}" groupingUsed="true"/></td>
                	</tr>
                </c:forEach>
            </tbody>
        </table>
        
        <table summary="" cellspacing="0"  width="100%" id="history">
                <caption>목록</caption>
                <colgroup>
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
            </colgroup>
            <thead>
					<tr >
						<th scope="row" style="text-align: center;">1년~5년</th>
						<th scope="row" style="text-align: center;">6년~10년</th>
						<th scope="row" style="text-align: center;">11년~15년</th>
						<th scope="row" style="text-align: center;">16년~20년</th>
						<th scope="row" style="text-align: center;">20년 이상</th>
					</tr>
				</thead>
            <tbody id="yearlist">
                <c:forEach items="${resultList}" var="result" varStatus="status">
                	<tr  onclick="chart('<c:out value="${result}"/>');">
	                    <td  style="text-align: center;"><fmt:formatNumber value="${result.history00001}" groupingUsed="true"/></td>
	                    <td  style="text-align: center;"><fmt:formatNumber value="${result.history00002}" groupingUsed="true"/></td>
	                    <td  style="text-align: center;"><fmt:formatNumber value="${result.history00003}" groupingUsed="true"/></td>
	                    <td  style="text-align: center;"><fmt:formatNumber value="${result.history00004}" groupingUsed="true"/></td>
	                    <td  style="text-align: center;"><fmt:formatNumber value="${result.history00005}" groupingUsed="true"/></td>
                	</tr>
                </c:forEach>
            </tbody>
        </table>
        
        <table summary="" cellspacing="0"  width="100%" id="ischarge">
                <caption>목록</caption>
                <colgroup>
                <col width="" />
                <col width="" />
                <col width="" />
            </colgroup>
            <thead>
					<tr >
						<th scope="row" style="text-align: center;">일반</th>
						<th scope="row" style="text-align: center;">특별</th>
						<th scope="row" style="text-align: center;">무료</th>
					</tr>
				</thead>
            <tbody id="yearlist">
                <c:forEach items="${resultList}" var="result" varStatus="status">
                	<tr  onclick="chart('<c:out value="${result}"/>');">
	                     <td  style="text-align: center;"><input type="hidden" id="dept" value="${result}"/><fmt:formatNumber value="${result.ischargeC}" groupingUsed="true"/></td>
	                    <td  style="text-align: center;"><fmt:formatNumber value="${result.ischargeS}" groupingUsed="true"/></td>
	                    <td  style="text-align: center;"><fmt:formatNumber value="${result.ischargeF}" groupingUsed="true"/></td>
                	</tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
		
<div id="chart_div" style="width: 100%; height: 500px;"></div>
<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--
if($("#search_att option:selected").val() == "sex"){
	$("#sex").show();
	$("#address").hide();
	$("#upperclass").hide();
	$("#history").hide();
	$("#ischarge").hide();
}else if($("#search_att option:selected").val() == "address"){
	$("#sex").hide();
	$("#address").show();
	$("#upperclass").hide();
	$("#history").hide();
	$("#ischarge").hide();
}else if($("#search_att option:selected").val() == "upperclass"){
	$("#sex").hide();
	$("#address").hide();
	$("#upperclass").show();
	$("#history").hide();
	$("#ischarge").hide();
}else if($("#search_att option:selected").val() == "history"){
	$("#sex").hide();
	$("#address").hide();
	$("#upperclass").hide();
	$("#history").show();
	$("#ischarge").hide();
}else if($("#search_att option:selected").val() == "ischarge"){
	$("#sex").hide();
	$("#address").hide();
	$("#upperclass").hide();
	$("#history").hide();
	$("#ischarge").show();
}

var pData = null;
var pName = "";

$("#ses_search_gyear option:eq(0)").remove();
//$("#ses_search_att option:eq(0)").remove();
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
if(true){
	google.load("visualization", "1", {packages:["corechart"]});google.setOnLoadCallback(drawChart);
}
$(document).ready(function(){
	chart($("#ses_search_att").val());
});
function drawChart() {  
	var data = google.visualization.arrayToDataTable([
	                                                  ['Element', '회원', { role: 'style' }, { role: 'annotation' } ]
	                                               ]);
	
	if(pData != null && pData != ""){
		data = pData;
	}else{
	}
	var view = new google.visualization.DataView(data);
    view.setColumns([0, 1,
                     { calc: "stringify",
                       sourceColumn: 1,
                       type: "string",
                       role: "annotation" },
                     2]);
    var pWidth = 1500;
    var wid = new String($(".tbDetail").css("width")).split(".");
	pWidth = Number(wid[0])+100;
		var options = {    
								title: pName,    
								hAxis: {title: $("#ses_search_gyear option:selected").val()+"년", titleTextStyle: {color: 'black'}}  ,
								width: pWidth,
								fontSize : 12,
								bar: {groupWidth: "70%"},
						        legend: { position: "none" }

							};  
		var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));  chart.draw(data, options);
		chart.draw(view, options);

		}    
		
function chart(param){
	var valParam = param;
	if(valParam == null || valParam == ""){	
		valParam = $("#dept").val();
	}else{
		
	}
	var att = $("#search_att option:selected").val();
	var attNm = $("#search_att option:selected").text();
	var map = mapCreate(valParam);
	pName = attNm;
	if(att == "address"){
	pData = google.visualization.arrayToDataTable([
	                                                  ['Element', '회원', { role: 'style' }, { role: 'annotation' } ],
	                                                  ['서울', Number(map.get("deptcd00001"))		, '#550000', map.get("deptcd00001") ],
	                                                  ['부산', Number(map.get("deptcd00002"))		, '#005500', map.get("deptcd00002") ],
	                                                  ['대구', Number(map.get("deptcd00003"))		, '#000055', map.get("deptcd00003") ],
	                                                  ['인천', Number(map.get("deptcd00004"))		, '#aa0000', map.get("deptcd00004") ],
	                                                  ['광주', Number(map.get("deptcd00005"))		, '#00aa00', map.get("deptcd00005") ],
	                                                  ['대전', Number(map.get("deptcd00006"))		, '#0000aa', map.get("deptcd00006") ],	                                                  
	                                                  ['울산', Number(map.get("deptcd00007"))		, '#550000', map.get("deptcd00007") ],
	                                                  ['경기', Number(map.get("deptcd00008"))		, '#005500', map.get("deptcd00008") ],
	                                                  ['강원', Number(map.get("deptcd00009"))		, '#000055', map.get("deptcd00009") ],
	                                                  ['충청북도', Number(map.get("deptcd00010"))	, '#aa0000', map.get("deptcd00010") ],
	                                                  ['충청남도', Number(map.get("deptcd00011"))	, '#00aa00', map.get("deptcd00011") ],
	                                                  ['전라북도', Number(map.get("deptcd00012"))	, '#0000aa', map.get("deptcd00012") ],
	                                                  ['전라남도', Number(map.get("deptcd00013"))	, '#550000', map.get("deptcd00013") ],
	                                                  ['경상북도', Number(map.get("deptcd00014"))	, '#005500', map.get("deptcd00014") ],
	                                                  ['경상남도', Number(map.get("deptcd00015"))	, '#000055', map.get("deptcd00015") ],
	                                                  ['제주', Number(map.get("deptcd00016"))		, '#aa0000', map.get("deptcd00016") ],
	                                                  ['국립', Number(map.get("deptcd00017"))		, '#00aa00', map.get("deptcd00017") ],
	                                                  ['세종', Number(map.get("deptcd00018"))		, '#0000aa', map.get("deptcd00018") ],
	                                                  ['복지관외', Number(map.get("deptcd00019"))	, '#550000', map.get("deptcd00019") ]
	                                               ]);
	}else if(att == "sex"){
		pData = google.visualization.arrayToDataTable([
		                                                  ['Element', '회원', { role: 'style' }, { role: 'annotation' } ],
		                                                  ['남성', Number(map.get("sexF"))		, '#555500', map.get("sexF") ],
		                                                  ['여성', Number(map.get("sexM"))		, '#005555', map.get("sexM") ]
		                                               ]);
	}else if(att == "upperclass"){
		pData = google.visualization.arrayToDataTable([
		                                                  ['Element', '회원', { role: 'style' }, { role: 'annotation' } ],
		                                                  ['교원', Number(map.get("empGubunT"))		, '#555500', Number(map.get("empGubunT")) ],
		                                                  ['보조', Number(map.get("empGubunE"))		, '#005555', Number(map.get("empGubunE")) ],
		                                                  ['일반', Number(map.get("empGubunW"))		, '#555500', Number(map.get("empGubunW")) ],
		                                                  ['교육전문직', Number(map.get("empGubunR"))		, '#005555', Number(map.get("empGubunR")) ]
		                                               ]);
	}else if(att == "history"){
		pData = google.visualization.arrayToDataTable([
		                                                  ['Element', '회원', { role: 'style' }, { role: 'annotation' } ],
		                                                  ['1년~5년'		, Number(map.get("history00001"))		, '#555500', Number(map.get("history00001")) ],
		                                                  ['6년~10년'	, Number(map.get("history00002"))		, '#005555', Number(map.get("history00002")) ],
		                                                  ['11년~15년'	, Number(map.get("history00003"))		, '#550055', Number(map.get("history00003")) ],
		                                                  ['16년~20년'	, Number(map.get("history00004"))		, '#055505', Number(map.get("history00004")) ],
		                                                  ['20년 이상'	, Number(map.get("history00005"))		, '#050555', Number(map.get("history00005")) ]
		                                               ]);
	}
	else if(att == "ischarge"){
		pData = google.visualization.arrayToDataTable([
		                                                  ['Element'	, '회원', { role: 'style' }, { role: 'annotation' } ],
		                                                  ['일반'		, Number(map.get("ischargeC"))		, '#555500', Number(map.get("ischargeC")) ],
		                                                  ['특별'		, Number(map.get("ischargeS"))		, '#005555', Number(map.get("ischargeS")) ],
		                                                  ['무료'		, Number(map.get("ischargeF"))		, '#550055', Number(map.get("ischargeF")) ]
		                                               ]);
	}
	drawChart();
}		
//선택한 문제번호 모으기
function chkExamNum(frm) {
	var v_chkcnt = 0;
	var v_chknum = "";
	
	if (frm.all['p_checks']) {
          
		if (frm.p_checks != null && frm.p_checks.length > 0) {
			for (i=0; i<frm.p_checks.length; i++) {
				if (frm.p_checks[i].checked == true) {

					v_chkcnt += 1;
					v_chknum += frm.p_checks[i].value + ",";
				}
			}
			v_chknum = v_chknum.substring(0,v_chknum.length-1);
		} else if ( frm.p_checks != null ) {
			if (frm.p_checks.checked) {

				v_chkcnt = 1;
				v_chknum = frm.p_checks.value;
			}
		}
		if (v_chkcnt==0) {
			alert("문제를 선택하세요");
			return "-1";           
		}
	} else {
		alert("선택할 문제가 없습니다.");
		return "-1";
	}
	return v_chknum;
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

/* ********************************************************
* 정렬처리 함수
******************************************************** */
function doOderList(column) {
	
	thisForm.search_orderType.value = (thisForm.search_orderType.value == "DESC") ? "ASC" : "DESC";
	thisForm.search_orderColumn.value = column;
	thisForm.action = "/adm/exm/examQuestList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}



//등록 / 수정
function doView(p_subj, p_examnum) {
	if($("#ses_search_gyear").val() == '')
	{	
		alert("연도를 선택하세요");
		return;
	}

	if($("#ses_search_grseq").val() == '')
	{	
		alert("교육기수를 선택하세요");
		return;
	}
	
	if($("#ses_search_subj").val() == '')
	{	
		alert("과정을 선택하세요");
		return;
	}

	
     farwindow = window.open("", "examQuestViewPopWindow", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 800, height = 700, top=0, left=0");
     thisForm.p_subj.value = $("#ses_search_subj").val();
     thisForm.p_examnum.value = p_examnum;
     thisForm.target = "examQuestViewPopWindow"
     thisForm.action = "/adm/exm/examQuestView.do";
 	 thisForm.submit();

     farwindow.window.focus();
}




//문제 미리보기
function previewQuestion() {

	var frm = thisForm;
	
	var v_chknum = chkExamNum(frm);
	if (v_chknum != "-1") {
		window.open("", "popExamQuestionPreview", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes, resizable=yes,copyhistory=no, width=800, height=600, top=0, left=0");
	
	  	frm.target = "popExamQuestionPreview";
	  	frm.action = "/adm/exm/examQuestPreview.do";
	  	frm.p_subj.value = $("#ses_search_subj").val();
	  	frm.p_chknum.value = v_chknum;
		frm.submit();
	}
}


//폴 추가 
function insertPool() {
	if($("#ses_search_gyear").val() == '')
	{	
		alert("연도를 선택하세요");
		return;
	}

	if($("#ses_search_grseq").val() == '')
	{	
		alert("교육기수를 선택하세요");
		return;
	}
	
	if($("#ses_search_subj").val() == '')
	{	
		alert("과정을 선택하세요");
		return;
	}

	
    //farwindow = window.open("", "examQuestPoolListPopWindow", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 1017, height = 700, top=0, left=0");
    thisForm.p_subj.value = $("#ses_search_subj").val();
    
    //thisForm.target = "examQuestPoolListPopWindow"
    thisForm.action = "/adm/exm/examQuestPoolList.do";
	thisForm.submit();

    //farwindow.window.focus();
}


	

//파일로 추가
function insertFileToDB() {
		if($("#ses_search_gyear").val() == '')
		{	
			alert("연도를 선택하세요");
			return;
		}
	
		if($("#ses_search_grseq").val() == '')
		{	
			alert("교육기수를 선택하세요");
			return;
		}
		
		if($("#ses_search_subj").val() == '')
		{	
			alert("과정을 선택하세요");
			return;
		}
	
		farwindow = window.open("", "examQuestFileUploadPopWindow", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 830, height = 600, top=0, left=0");
		
		thisForm.p_subj.value = $("#ses_search_subj").val();
		thisForm.action = "/adm/exm/examQuestFileUpload.do";
		thisForm.target = "examQuestFileUploadPopWindow"
		thisForm.submit();
	
}



	 
function whenAllSelect() {
	if(thisForm.all['p_checks']) {
		if (thisForm.p_checks.length > 0) {
			for (i=0; i<thisForm.p_checks.length; i++) {
				thisForm.p_checks[i].checked = true;
			}
		} else {
			thisForm.p_checks.checked = true;
		}
	}
}

function whenAllSelectCancel() {
	if(thisForm.all['p_checks']) {
		if (thisForm.p_checks.length > 0) {
			for (i=0; i<thisForm.p_checks.length; i++) {
				thisForm.p_checks[i].checked = false;
			}
		} else {
			thisForm.p_checks.checked = false;
		}
	}
}

function chkeckall(){
    if(thisForm.p_chkeckall.checked){
      whenAllSelect();
    }
    else{
      whenAllSelectCancel();
    }
}



//문제 삭제하기
function deleteQuestion() {

	var frm = thisForm;
	
	var v_chknum = chkExamNum(frm);

	if (v_chknum != "-1") {

	    if(!confirm("정말 삭제하시겠습니까?\n이미 사용되어진 문제는 삭제되지 않습니다.")) {
	        return;
	    }
	
	  	
	  	frm.p_subj.value = $("#ses_search_subj").val();
	  	frm.p_chknum.value = v_chknum;
		frm.p_process.value = "checkDelete";
		frm.target = "_self";
	  	frm.action = "/adm/exm/examQuestAction.do";
		frm.submit();
	}
}

/* ********************************************************
 * 엑셀다운로드 함수
 ******************************************************** */
function whenXlsDownLoad() {
	thisForm.action = "/adm/stu/totalScoreMemberExcelDown.do";
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

function numberCom(param){
	if(param.length > 3){
		var returnParam="";
		var cnt=0;
		for(var i = param.length-1 ; i >= 0 ; i--){
			cnt ++;
			if((cnt % 3) == 0 && i != 0){
				returnParam = ","+param[i]+returnParam;
			}else{
				returnParam = param[i]+returnParam;
			}
		}
		return returnParam;
	}else{
		return param;
	}
}

//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
