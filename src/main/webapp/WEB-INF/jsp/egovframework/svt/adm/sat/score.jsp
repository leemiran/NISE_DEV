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
		<c:param name="selectViewType" value="SCORE">조회조건타입 : 타입별 세부조회조건</c:param>
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
		</colgroup>
		<thead>
			<tr>
				<th scope="row" style="text-align: center;">구분</th>
				<th scope="row" style="text-align: center;">계설시기</th>
				<th scope="row" style="text-align: center;">성적분포</th>
				<th scope="row" style="text-align: center;">학습자수</th>
				<th scope="row" style="text-align: center;">비율(%)</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty score}">
					<c:forEach items="${score}" var="result" varStatus="status">
						<c:if test="${result.groupingPoint == 0 and result.groupingScoreGubun == 0}">
							<tr>
								<td class="gubun" style="text-align: center;">${result.point}학점<br/>(${result.point*15}차시)</td>
								<td class="gubun${result.point}" style="text-align: center;">${p_search_from} ~ ${p_search_to}</td>
								<td style="text-align: center;">${result.scoreGubun}</td>
								<td style="text-align: center;"><fmt:formatNumber value="${result.studentCnt}" groupingUsed="true"/></td>
								<td style="text-align: center;"><fmt:formatNumber value="${result.ratio}"   pattern="#,###.##" groupingUsed="true"/>%</td>
							</tr>
						</c:if>
						<c:if test="${result.groupingPoint == 0 and result.groupingScoreGubun == 1}">
							<tr>
								<th colspan="3" style="text-align: center;">소계</th>
								<th style="text-align: center;"><fmt:formatNumber value="${result.studentCnt}" groupingUsed="true"/></th>
								<th style="text-align: center;"><fmt:formatNumber value="${result.ratio}"   pattern="#,###" groupingUsed="true"/>%</th>
							</tr>
						</c:if>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr><td colspan="5" style="text-align: center;">데이터가 존재하지 않습니다.</td></tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	<script>
		$("td[class^=gubun]").each(function () {
			var className = $(this).attr('class');
            var rows = $("." + className + ":contains('" + $(this).text() + "')");
            if (rows.length > 1) {
                rows.eq(0).attr("rowspan", rows.length);
                rows.not(":eq(0)").remove(); 
            } 
       	});
	</script>
</div>

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

	thisForm.action = "/adm/sat/score.do";
	thisForm.target = "_self";
	thisForm.submit();
}
</script>

<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>