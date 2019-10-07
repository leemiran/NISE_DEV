<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>

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
            
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType" value="EDU_OFFICE_DETAIL">조회조건타입 : 타입별 세부조회조건</c:param>
		<c:param name="selectParameter" value="">조회조건 추가 : admSearchParameter.jsp 추가</c:param>
	</c:import>
	
	<!-- detail wrap -->
    <div class="tbDetail">    
		<table summary="" cellspacing="0" width="100%">
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
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
		</colgroup>
		<thead>
			<tr>
				<th scope="row" style="text-align: center;" rowspan="2">구분</th>
				<th scope="row" style="text-align: center;" rowspan="2">기수</th>
				<th scope="row" style="text-align: center;" rowspan="2">종별</th>
				<th scope="row" style="text-align: center;" rowspan="2">과정명</th>
				<th scope="row" style="text-align: center;" rowspan="2">시간</th>
				<th scope="row" style="text-align: center;" rowspan="2">신청기간</th>
				<th scope="row" style="text-align: center;" rowspan="2">연수기간</th>
				<th scope="row" style="text-align: center;" rowspan="2">명단제출인원</th>
				<th scope="row" style="text-align: center;" rowspan="2">신청인원</th>
				<th scope="row" style="text-align: center;" rowspan="2">이수인원</th>
				<th scope="row" style="text-align: center;" rowspan="2">미이수인원</th>
				<th scope="row" style="text-align: center;" rowspan="2">이수율</th>
				<th scope="row" style="text-align: center;" colspan="10">신청인원분포</th>
			</tr>
			<tr>
				<th scope="row" style="text-align: center;">유치</th>
				<th scope="row" style="text-align: center;">초등</th>
				<th scope="row" style="text-align: center;">중등</th>
				<th scope="row" style="text-align: center;">초등<br/>(특수)</th>
				<th scope="row" style="text-align: center;">중등<br/>(특수)</th>
				<th scope="row" style="text-align: center;">사립<br/>유치원</th>
				<th scope="row" style="text-align: center;">교감</th>
				<th scope="row" style="text-align: center;">교장</th>
				<th scope="row" style="text-align: center;">교원<br/>전문직</th>
				<th scope="row" style="text-align: center;">기타</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty eduOfficeDetail}">
					<c:forEach items="${eduOfficeDetail}" var="result" varStatus="status">
						<c:if test="${result.groupingUpperclass == 0 and result.groupingSubj == 0 and result.groupingSubjseq == 0}">
							<tr>
								<td class="gubun" style="text-align: center;">${result.classname}</td>
								<td style="text-align: center;">${result.subjseq}</td>
								<td style="text-align: center;">${result.autoType}</td>
								<td style="text-align: left;">${result.subjnm}</td>
								<td style="text-align: center;">${result.edutimes}</td>
								<td style="text-align: center;">${result.propstart}~${result.propend}</td>
								<td style="text-align: center;">${result.edustart}~${result.eduend}</td>
								<td style="text-align: right;">
									<c:choose>
										<c:when test="${result.autoCnt > 0}">
											<fmt:formatNumber value="${result.autoCnt}" groupingUsed="true"/>
										</c:when>
										<c:otherwise>
											-
										</c:otherwise>
									</c:choose>
								</td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.studentCnt}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.isuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.isuN}" groupingUsed="true"/></td>
								<td style="text-align: right;">${fn2:toPercent(result.isuY, result.studentCnt)}</td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.job1}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.job2}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.job3}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.job4}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.job5}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.job6}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.job7}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.job8}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.job9}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.job10}" groupingUsed="true"/></td>
							</tr>
						</c:if>
						<c:if test="${result.groupingUpperclass == 0 and result.groupingSubj == 1 and result.groupingSubjseq == 1}">
							<tr>
								<td class="gubun" style="text-align: center;">${result.classname}</td>
								<th colspan="2" style="text-align: center;">소계</th>
								<th style="text-align: center;">${result.subjCnt}과정</th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.studentCnt}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.isuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.isuN}" groupingUsed="true"/></th>
								<th style="text-align: right;">${fn2:toPercent(result.isuY, result.studentCnt)}</th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job1}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job2}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job3}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job4}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job5}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job6}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job7}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job8}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job9}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job10}" groupingUsed="true"/></th>
							</tr>
						</c:if>
						<c:if test="${result.groupingUpperclass == 1 and result.groupingSubj == 1 and result.groupingSubjseq == 1}">
							<tr>
								<th colspan="3" style="text-align: center;">총계</th>
								<th style="text-align: center;">${result.subjCnt}과정</th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.studentCnt}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.isuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.isuN}" groupingUsed="true"/></th>
								<th style="text-align: right;">${fn2:toPercent(result.isuY, result.studentCnt)}</th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job1}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job2}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job3}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job4}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job5}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job6}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job7}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job8}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job9}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.job10}" groupingUsed="true"/></th>
							</tr>
						</c:if>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr><td colspan="22" style="text-align: center;">데이터가 존재하지 않습니다.</td></tr>
				</c:otherwise>
			</c:choose>
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

<form method="post" name="graphForm" id="graphForm" target="_blank">
	<input type="hidden" id="year" name="year" value="${ses_search_gyear}" />
	<input type="hidden" id="areaCode" name="areaCode" value="${search_area}" />
</form>

<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if($("#ses_search_gyear").val() == '')
	{	
		alert("연도를 선택하세요");
		return;
	}

	thisForm.action = "/adm/sat/eduOfficeDetail.do";
	thisForm.target = "_self";
	thisForm.submit();
}

function popupGraph() {
	var targetName = 'popupEduOfficeGraph';
	window.open("",targetName,"left=100,top=100,width=1024,height=768,toolbar=no,menubar=no,status=yes,scrollbars=yes,resizable=yes");
	
	$("#graphForm").attr({
		target: targetName,
		action: '/adm/sat/popupEduOfficeIsu.do'
	}).submit();
}

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