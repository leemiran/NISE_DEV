<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="pageIndex" id="pageIndex" value="1"/>
	<input type="hidden" id="p_action" name="p_action" value="go">
	<input type="hidden" id="p_company" name="p_company" value="${p_company}">
	<input type="hidden" id="p_search" name="p_search" value="${p_search}">
	<input type="hidden" id="p_searchtext" name="p_searchtext" value="${p_searchtext}">
	
	<!-- search wrap-->
	<div class="searchWrap">
		<div class="in" style="width:700px;">
			<strong class="shTit">ID</strong>
			<input type="text" name="p_searchtext_id" id="p_searchtext_id" value="${p_searchtext_id}"/>
			<strong class="shTit">성명</strong>
			<input type="text" name="p_searchtext_name" id="p_searchtext_name" value="${p_searchtext_name}"/>
			<a href="#none" class="btn_search" onclick="linkPage(1)"><span>검색</span></a>
		</div>
	</div>
	<!-- // search wrap -->
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="listPage()"><span>목록</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="60px" />
				<col width="100px" />
				<col width="100px" />				
				<col width="150px" />
				<col width="180px"/>
				<col width="180px"/>
				<col width="180px" />
				<col width="150px" />
				<col />				
				<col width="60px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">번호</th>
					<th scope="row">조회사 권한</th>
					<th scope="row">조회사 성명(ID)</th>
					<th scope="row">IP</th>
					<th scope="row">회원ID</th>
					<th scope="row">성명</th>
					<th scope="row">근무지</th>
					<th scope="row">조회시간</th>
					<th scope="row">URL</th>
					<th scope="row">활동</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td><c:out value="${result.seq}"/></td>
					<td><c:out value="${result.gadminnm}"/></td>
					<td><c:out value="${result.name}"/>(<c:out value="${result.userid}"/>)</td>
					<td><c:out value="${result.ip}"/></td>
					<td><c:out value="${result.targetUserid}"/></td>
					<td><c:out value="${result.targetName}"/></td>
					<td><c:out value="${result.userPath}"/></td>
					<td><c:out value="${result.logdate}"/></td>
					<td><c:out value="${result.targetUrl}"/></td>
					<td><c:out value="${result.logAction}"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	<!-- 페이징 시작 -->
	<div class="paging">
		<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="linkPage"/>
	</div>
	<!-- 페이징 끝 -->
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	function listPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mem/memberSearchList.do";
		frm.target = "_self";
		frm.submit();
	}

	function linkPage(idx){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.pageIndex.value = idx;
		frm.action = "/adm/cfg/mem/searchMemberLogList.do";
		frm.target = "_self";
		frm.submit();
	}
</script>