<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type="hidden" id="p_subj" name="p_subj">
	
	
	<c:if test="${sessionScope.gadmin ne 'Q1'}">	
		<!-- 검색박스 시작-->
		<c:import url="/com/sch/admSearchBars.do">
			<c:param name="selectViewType"      value="AA"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		</c:import>
		<!-- 검색박스 끝 -->
	</c:if>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="250px" />
				<col />
			</colgroup>
			<thead>
				<tr>
					<th>과정분류</th>
					<th>과정명</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td >${result.classname}</td>
					<td class="left" ><a href="#none" onclick="subList('${result.subj}')">${result.subjnm} [${result.subj}]</a></td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="2">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	/* ********************************************************
	 * 페이징처리 함수
	 ******************************************************** */
	function doPageList() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/prt/practiceStudyList.do";
		frm.target = "_self";
		frm.submit();
	}
	
	function subList(subj) {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/prt/practiceStudySubList.do";
		frm.p_subj.value = subj;
		frm.target = "_self";
		frm.submit();
	}
	
</script>