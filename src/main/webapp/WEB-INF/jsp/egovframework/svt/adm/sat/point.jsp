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
		<c:param name="selectViewType" value="POINT">조회조건타입 : 타입별 세부조회조건</c:param>
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
		</colgroup>
		<thead>
			<tr>
				<th scope="row" style="text-align: center;">연도</th>
				<th scope="row" style="text-align: center;">학점</th>
				<th scope="row" style="text-align: center;">과정수</th>
				<th scope="row" style="text-align: center;">이수자(명)</th>
				<th scope="row" style="text-align: center;">수강료 수입<br/>(천원)</th>
				<th scope="row" style="text-align: center;">모집대상</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty point}">
					<c:forEach items="${point}" var="result" varStatus="status">
						<c:if test="${result.groupingYear == 0 and result.groupingPoint == 0}">
							<tr>
								<td class="gubun" style="text-align: center;">'${result.year}년</td>
								<td style="text-align: center;">${result.point}학점(${result.point*15}차시)</td>
								<td style="text-align: center;">${result.subjCnt}</td>
								<td style="text-align: center;">
									<fmt:formatNumber value="${result.isuCnt}" groupingUsed="true"/>
									<c:if test="${result.point eq 1}">
										<br/>
										(일반인 연수인원 포함)
									</c:if>
								</td>
								<c:if test="${status.first}">
									<td style="text-align: center;" rowspan="${fn:length(point) - 1}">
										※시·도<br/>교육청<br/>위탁연수비<br/>로<br/>운영(비공개)
									</td>
									<td style="text-align: center;" rowspan="${fn:length(point) - 1}">
										유·초·중등<br/>교원 및<br/>교육전문직<br/>, 학무모,<br/>관련인 등
									</td>
								</c:if>
							</tr>
						</c:if>
						<c:if test="${result.groupingYear == 0 and result.groupingPoint == 1}">
							<tr>
								<th class="gubun" style="text-align: center;">'${result.year}년</th>
								<th style="text-align: center;">소계</th>
								<th style="text-align: center;">${result.subjCnt}</th>
								<th style="text-align: center;"><fmt:formatNumber value="${result.isuCnt}" groupingUsed="true"/></th>
							</tr>
						</c:if>
						<c:if test="${result.groupingYear == 1 and result.groupingPoint == 1}">
							<tr>
								<th style="text-align: center;">총계</th>
								<th style="text-align: center;">-</th>
								<th style="text-align: center;">${result.subjCnt}</th>
								<th style="text-align: center;"><fmt:formatNumber value="${result.isuCnt}" groupingUsed="true"/></th>
								<th style="text-align: center;"></th>
								<th style="text-align: center;"></th>
							</tr>
						</c:if>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr><td colspan="6" style="text-align: center;">데이터가 존재하지 않습니다.</td></tr>
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

	thisForm.action = "/adm/sat/point.do";
	thisForm.target = "_self";
	thisForm.submit();
}
</script>

<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>