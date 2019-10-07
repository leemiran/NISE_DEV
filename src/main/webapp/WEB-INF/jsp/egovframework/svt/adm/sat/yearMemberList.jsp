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
	<%-- <div class="listTop">			
		총 회원수 : <fmt:formatNumber value="${memberCnt}" groupingUsed="true"/> 명
	</div> --%>
	<!-- detail wrap -->
    <div class="tbList">    
		<table summary="" cellspacing="0" width="100%">
			<caption>목록</caption>
			<colgroup>
				<col width="20%" />
				<col width="40%" />
				<col width="40%" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">구분</th>
					<th scope="row">회원수</th>
					<th scope="row">비율(%)</th>
				</tr>
			</thead>
			<tbody>
				<c:set var="totSum" value="0.00000"></c:set>
				<c:forEach items="${resultList}" var="result" varStatus="status">
					<c:set var="totSum">${result.cnt + totSum}</c:set>
				</c:forEach>

				<c:forEach items="${resultList}" var="result" varStatus="status">
					<c:set var="vcnt" value="0.00000"></c:set>
					<c:set var="vcnt" value="${vcnt + result.cnt}"></c:set>
					<tr>
						<td>${result.empGubun}</td>
						<td class="name"><fmt:formatNumber value="${vcnt}" type="number" /></td>
						<td><fmt:formatNumber value="${vcnt/totSum*100.00}" type="number" pattern="###.##" />%</td>
					</tr>
				</c:forEach>

				<tr class="bg">
					<td><strong>합계</strong></td>
					<td class="point"><strong><fmt:formatNumber value="${totSum}" type="number" /></strong></td>
					<td>100.00%</td>
				</tr>
			</tbody>
		</table>
	</div>
</form>
		
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
