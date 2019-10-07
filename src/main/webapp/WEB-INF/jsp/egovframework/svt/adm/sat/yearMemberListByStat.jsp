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
				<th scope="row" style="text-align: center;">활동상태</th>
				<th scope="row" style="text-align: center;">구분</th>
				<th scope="row" style="text-align: center;">${ses_search_gyear}</th>
				<th scope="row" style="text-align: center;">${ses_search_gyear - 1}</th>
				<th scope="row" style="text-align: center;">${ses_search_gyear - 2}</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${resultList}" var="result" varStatus="status">
				<tr>
					<c:if test="${status.first}">
						<th rowspan="${fn:length(resultList)}" style="text-align: center;">신규</th>
					</c:if>
					<c:if test="${result.grouping eq 0}">
						<td>
							<c:choose>
								<c:when test="${'T' eq result.empGubun}">교원</c:when>
								<c:when test="${'E' eq result.empGubun}">보조인력</c:when>
								<c:when test="${'R' eq result.empGubun}">교원전문직</c:when>
								<c:when test="${'P' eq result.empGubun}">일반회원</c:when>
								<c:when test="${'O' eq result.empGubun}">공무원</c:when>
								<c:otherwise>기타</c:otherwise>
							</c:choose>
						</td>
						<td style="text-align: right;"><fmt:formatNumber value="${result.joinThisYear}" groupingUsed="true"/></td>
						<td style="text-align: right;"><fmt:formatNumber value="${result.joinYearAgo}" groupingUsed="true"/></td>
						<td style="text-align: right;"><fmt:formatNumber value="${result.joinYearsAgo}" groupingUsed="true"/></td>
					</c:if>
					<c:if test="${result.grouping eq 1}">
						<th>계</th>
						<th style="text-align: right;"><fmt:formatNumber value="${result.joinThisYear}" groupingUsed="true"/></th>
						<th style="text-align: right;"><fmt:formatNumber value="${result.joinYearAgo}" groupingUsed="true"/></th>
						<th style="text-align: right;"><fmt:formatNumber value="${result.joinYearsAgo}" groupingUsed="true"/></th>
					</c:if>
				</tr>
			</c:forEach>
			<c:forEach items="${resultList}" var="result" varStatus="status">
				<tr>
					<c:if test="${status.first}">
						<th rowspan="${fn:length(resultList)}" style="text-align: center;">휴면</th>
					</c:if>
					<c:if test="${result.grouping eq 0}">
						<td>
							<c:choose>
								<c:when test="${'T' eq result.empGubun}">교원</c:when>
								<c:when test="${'E' eq result.empGubun}">보조인력</c:when>
								<c:when test="${'R' eq result.empGubun}">교원전문직</c:when>
								<c:when test="${'P' eq result.empGubun}">일반회원</c:when>
								<c:when test="${'O' eq result.empGubun}">공무원</c:when>
								<c:otherwise>기타</c:otherwise>
							</c:choose>
						</td>
						<td style="text-align: right;"><fmt:formatNumber value="${result.dormantYThisYear}" groupingUsed="true"/> (<fmt:formatNumber value="${result.dormantNThisYear}" groupingUsed="true"/>)</td>
						<td style="text-align: right;"><fmt:formatNumber value="${result.dormantYYearAgo}" groupingUsed="true"/> (<fmt:formatNumber value="${result.dormantNYearAgo}" groupingUsed="true"/>)</td>
						<td style="text-align: right;"><fmt:formatNumber value="${result.dormantYYearsAgo}" groupingUsed="true"/> (<fmt:formatNumber value="${result.dormantNYearsAgo}" groupingUsed="true"/>)</td>
					</c:if>
					<c:if test="${result.grouping eq 1}">
						<th>계</th>
						<th style="text-align: right;"><fmt:formatNumber value="${result.dormantYThisYear}" groupingUsed="true"/> (<fmt:formatNumber value="${result.dormantNThisYear}" groupingUsed="true"/>)</th>
						<th style="text-align: right;"><fmt:formatNumber value="${result.dormantYYearAgo}" groupingUsed="true"/> (<fmt:formatNumber value="${result.dormantNYearAgo}" groupingUsed="true"/>)</th>
						<th style="text-align: right;"><fmt:formatNumber value="${result.dormantYYearsAgo}" groupingUsed="true"/> (<fmt:formatNumber value="${result.dormantNYearsAgo}" groupingUsed="true"/>)</th>
					</c:if>
				</tr>
			</c:forEach>
			<c:forEach items="${resultList}" var="result" varStatus="status">
				<tr>
					<c:if test="${status.first}">
						<th rowspan="${fn:length(resultList)}" style="text-align: center;">탈퇴</th>
					</c:if>
					<c:if test="${result.grouping eq 0}">
						<td>
							<c:choose>
								<c:when test="${'T' eq result.empGubun}">교원</c:when>
								<c:when test="${'E' eq result.empGubun}">보조인력</c:when>
								<c:when test="${'R' eq result.empGubun}">교원전문직</c:when>
								<c:when test="${'P' eq result.empGubun}">일반회원</c:when>
								<c:when test="${'O' eq result.empGubun}">공무원</c:when>
								<c:otherwise>기타</c:otherwise>
							</c:choose>
						</td>
						<td style="text-align: right;"><fmt:formatNumber value="${result.retireYThisYear}" groupingUsed="true"/> (<fmt:formatNumber value="${result.retireNThisYear}" groupingUsed="true"/>)</td>
						<td style="text-align: right;"><fmt:formatNumber value="${result.retireYYearAgo}" groupingUsed="true"/> (<fmt:formatNumber value="${result.retireNYearAgo}" groupingUsed="true"/>)</td>
						<td style="text-align: right;"><fmt:formatNumber value="${result.retireYYearsAgo}" groupingUsed="true"/> (<fmt:formatNumber value="${result.retireNYearsAgo}" groupingUsed="true"/>)</td>
					</c:if>
					<c:if test="${result.grouping eq 1}">
						<th>계</th>
						<th style="text-align: right;"><fmt:formatNumber value="${result.retireYThisYear}" groupingUsed="true"/> (<fmt:formatNumber value="${result.retireNThisYear}" groupingUsed="true"/>)</th>
						<th style="text-align: right;"><fmt:formatNumber value="${result.retireYYearAgo}" groupingUsed="true"/> (<fmt:formatNumber value="${result.retireNYearAgo}" groupingUsed="true"/>)</th>
						<th style="text-align: right;"><fmt:formatNumber value="${result.retireYYearsAgo}" groupingUsed="true"/> (<fmt:formatNumber value="${result.retireNYearsAgo}" groupingUsed="true"/>)</th>
					</c:if>
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

$(document).ready(function(){
	$("#search_att option:selected").val('${search_att}');
	attChange();
});

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
</script>

<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
