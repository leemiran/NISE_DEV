<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<!--
 /**
  * @Class Name : yearContentsList.jsp
  * @Description : 운영현황 > 연도별 콘텐츠 보유 현황
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

<script type="text/javascript">      

		</script>
<style>
table.ex1 {width:97%; margin:0 auto; text-align:left; border-collapse:collapse; text-decoration: none; }
 .ex1 th, .ex1 td {padding:0px 10px;text-align:left;}
 .ex1 tbody tr td {background:#F3F5BB; text-align:left;}
 .ex1 tbody tr th {background:#F2F684; color:#1BA6B2; text-align:center; width:10%; border-right:1px solid #fff}

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
	<input type="hidden" name="searchYn"  value="Y">
	
	
            
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="BB_year"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->

<!-- detail wrap -->
    <div class="tbDetail" >
        <table summary="" cellspacing="0"  width="100%" >
                <caption>목록</caption>
                <colgroup>
                <col width="15%" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
                <col width="" />
            </colgroup>
            <thead>
					<tr >
						<th scope="row" style="text-align: center">구분</th>
						<th scope="row" style="text-align: center">콘텐츠명</th>
						<th scope="row" style="text-align: center">운영회수</th>
						<th scope="row" style="text-align: center">최초개발 연도</th>
						<th scope="row" style="text-align: center">최종리뉴얼 연도</th>
						<th scope="row" style="text-align: center">연수시간</th>
						<th scope="row" style="text-align: center">이용자(명)</th>
					</tr>
				</thead>
            <tbody>
				<c:forEach items="${list}" var="result" varStatus="status">
					<c:choose>
						<c:when test="${result.groupingUpperclass eq 0 and result.groupingSubj eq 0}">
							<tr>
								<td class="gubun" style="text-align: center;">${result.classname}</td>
								<td style="padding-left: 15px;">${result.subjnm}</td>
								<td style="text-align: center;">${result.subjseqCnt}</td>
								<td style="text-align: center;">${result.conyear}</td>
								<td style="text-align: center;">${result.renewalYear}</td>
								<td style="text-align: center;">${result.edutimes}</td>
								<td style="text-align: center;"><fmt:formatNumber value="${result.studentCnt}" groupingUsed="true"/></td>
							</tr>
						</c:when>
						<c:when test="${result.groupingUpperclass eq 0 and result.groupingSubj eq 1}">
							<tr style="background-color: #f5f5f5;">
								<td class="gubun" style="text-align: center;">${result.classname}</td>
								<td style="text-align: center;">소계</td>
								<td style="text-align: center;">${result.subjseqCnt}</td>
								<td style="text-align: center;"></td>
								<td style="text-align: center;"></td>
								<td style="text-align: center;"></td>
								<td style="text-align: center;"><fmt:formatNumber value="${result.studentCnt}" groupingUsed="true"/></td>
							</tr>
						</c:when>
						<c:when test="${result.groupingUpperclass eq 1 and result.groupingSubj eq 1}">
							<tr style="background-color: #e0e0e0;">
								<td colspan="2" style="text-align: center;">합계</td>
								<td style="text-align: center;"><fmt:formatNumber value="${result.subjseqCnt}" groupingUsed="true"/></td>
								<td></td>
								<td></td>
								<td></td>
								<td style="text-align: center;"><fmt:formatNumber value="${result.studentCnt}" groupingUsed="true"/></td>
							</tr>
						</c:when>
					</c:choose>
				</c:forEach>
			</tbody>
        </table>
		<script>
			$(".gubun").each(function () {
	            var rows = $(".gubun:contains('" + $(this).text() + "')");
	            if (rows.length > 1) {
	                rows.eq(0).attr("rowspan", rows.length);
	                rows.not(":eq(0)").remove(); 
	            } 
        	});
		</script>
    </div>
</form>
<!-- <div id="chart_div" style="width: 900px; height: 500px;"></div> -->
<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--
$(".floatL").hide();
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
//google.load("visualization", "1", {packages:["corechart"]});google.setOnLoadCallback(drawChart);
function drawChart() {  
	var dataArray = [ ['Element', '콘텐츠', { role: 'style' }, { role: 'annotation' } ]];
	var list = "${list}";
	var colorArray = new Array('#550000','#005500','#000055','#aa0000','#00aa00','#00aa00','#0000aa','#770000','#007700','#000077');
	var listArray = list.split("}, {");
	var cnt=0;
	if(listArray.length == 1)return;
	  for (var n =0; n < listArray.length; n++)
	  {
		  var map = mapCreate(listArray[n]);
		  
		  if(map.get('groupingUpperclass') == 0 && map.get('groupingSubj') == 0) {
	   		dataArray.push ([map.get("subjnm"), Number(map.get("studentCnt")) , colorArray[cnt], map.get("studentCnt")]); 
		  }
	   	if(cnt == 10)cnt = 0;
	   	cnt++;
	   }
	  var data = google.visualization.arrayToDataTable(dataArray);
	  /*
	var data = google.visualization.arrayToDataTable([
	                                                  ['Element', '콘텐츠', { role: 'style' }, { role: 'annotation' } ],
	                                                  ['장애학생 이애화 통합학급 운영'				, 3000, 'silver', '3000' ],
	                                                  ['일반학교에서 장애학생 지원'					, 1000, 'silver', '1000' ],
	                                                  ['치료교육 표시과목 변경 직무연수1'				, 1000, 'silver', '1000' ],
	                                                  ['특수교육 보조인력 역량강화 기초과정'		, 800, 'silver', '800' ]
	                                               ]);
	*/
	var view = new google.visualization.DataView(data);
    view.setColumns([0, 1,
                     { calc: "stringify",
                       sourceColumn: 1,
                       type: "string",
                       role: "annotation" },
                     2]);
    var pWidth = 1500;
    var wid = new String($(".tbDetail").css("width")).substr(0, new String($(".tbDetail").css("width")).indexOf("p"));
	pWidth = Number(wid)+100;
		var options = {    
								title: '연도별 콘텐츠 이용자 현황(그래프)',    
								hAxis: {title: '', titleTextStyle: {color: 'red'}}  ,
								width: pWidth,
								bar: {groupWidth: "70%"},
						        legend: { position: "none" }
							};  
		var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));  chart.draw(data, options);
		chart.draw(view, options);
		}    

/*************************************************
*java단에서 넘어온 값은 {'1','2'}형태의 데이터를 map형태로 만들어 줌
*
***************************************************/
function mapCreate(paramMap) {
	var mapSet = new JMap();
	var resultList = paramMap;
	resultList = resultList.replace("[","");
	resultList = resultList.replace("]","");
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

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {

	if($("#ses_search_gyear").val() == '')
	{	
		//alert("연도를 선택하세요");
		//return;
	}

	if($("#ses_search_grseq").val() == '')
	{	
		alert("교육기수를 선택하세요");
		return;
	}
	
	if($("#ses_search_subj").val() == '')
	{	
	//	alert("과정을 선택하세요");
	//	return;
	}
	
	

	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/sat/yearContentsList.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.target = "_self";
	thisForm.submit();
}
//-->
</script>
<c:if test="${not empty list}">
	<a href="#none" onclick="popupGraph();" class="btn01"><span>사용자추이</span></a>
	
	<div id="chartdiv" style="width: 100%; height: 600px; background-color: #FFFFFF;" ></div>
	<form method="post" name="graphForm" id="graphForm" target="_blank">
		<input type="hidden" id="year" name="year" value="${ses_search_gyear}" />
		<input type="hidden" id="subjnm" name="subjnm" value="${ses_search_subjnm}" />
	</form>
	<script type="text/javascript" src="${pageContext.request.contextPath}/script/amcharts/amcharts.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/script/amcharts/serial.js"></script>
	<script type="text/javascript">
	
	var graphColors = [
		'#FF0F00'
		, '#FF6600'
		, '#FF9E01'
		, '#FCD202'
		, '#F8FF01'
		, '#B0DE09'
		, '#04D215'
		, '#0D8ECF'
		, '#0D52D1'
		, '#2A0CD0'
		, '#8A0CCF'
		, '#CD0D74'
	];
	var amchartData = [];
	
	var list = "${list}";
	var listArray = list.split("}, {");
	
	var colorIdx = 0;
	for(var n = 0; n < listArray.length; n++) {
		var map = mapCreate(listArray[n]);
		
		if(map.get('groupingUpperclass') == 0 && map.get('groupingSubj') == 0) {
			// 값
			var data = {};
			data.category = map.get('subjnm');
			data.studentCnt = map.get('studentCnt');
			
			if(colorIdx > 11) colorIdx = 0;
			data.color = graphColors[colorIdx];
			
			amchartData.push(data);
			colorIdx ++;
		}
	}
	
	AmCharts.makeChart("chartdiv",
		{
			"type": "serial",
			"categoryField": "category",
			"maxSelectedTime": -10,
			"mouseWheelZoomEnabled": true,
			"categoryAxis": {
				"gridPosition": "start",
				"labelRotation": 45
			},
			"chartCursor": {
				"enabled": true
			},
			"chartScrollbar": {
				"enabled": true
			},
			"trendLines": [],
			"graphs": [
				{
					"lineAlpha": 0,
					"fillAlphas": 1,
					"id": "AmGraph-1",
					"title": "graph 1",
					"type": "column",
					"valueField": "studentCnt",
					"colorField": "color"
				}
			],
			"guides": [],
			"valueAxes": [
				{
					"id": "ValueAxis-1",
					"title": "이용자 수"
				}
			],
			"allLabels": [],
			"balloon": {},
			"titles": [
				{
					"id": "Title-1",
					"size": 15,
					"text": "연도별 콘텐츠 이용자 현황(그래프)"
				}
			],
			"dataProvider": amchartData
		}
	);
	
	function popupGraph() {
		var targetName = 'popupGraph';
		window.open("",targetName,"left=100,top=100,width=1024,height=768,toolbar=no,menubar=no,status=yes,scrollbars=yes,resizable=yes");
		
		$("#graphForm").attr({
			target: targetName,
			action: '/adm/sat/popupYearContents.do'
		}).submit();
	}
	</script>
</c:if>

<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
