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
			<strong class="shTit">재발급여부</strong>
			<select id="p_success" name="p_success">
				<option value="">ALL</option>
				<option value="Y" <c:if test="${p_success eq 'Y'}">selected</c:if>>성공</option>
				<option value="N" <c:if test="${p_success eq 'N'}">selected</c:if>>실패</option>
			</select>
			<strong class="shTit">ID</strong>
			<input type="text" name="p_userid" id="p_userid" value="${p_userid}"/>
			<a href="#none" class="btn_search" onclick="linkPage(1)"><span>검색</span></a>
		</div>
	</div>
	<!-- // search wrap -->
	
	<div class="listTop">
		재발급 성공여부 합계 >>
		성공 : <span style="color: red"><c:out value="${status.success}"/></span>&nbsp;&nbsp;
		실패 : <span style="color: red"><c:out value="${status.fail}"/></span>
		<div class="btnR"><a href="#" class="btn01" onclick="listPage()"><span>목록</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="60px" />
				<col />
				<col width="180px" />
				<col width="220px"/>
				<col width="140px" />
				<col width="150px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">번호</th>
					<th scope="row">재발급 시도자 구분</th>
					<th scope="row">관리자ID</th>
					<th scope="row">회원ID</th>
					<th scope="row">시도 시간</th>
					<th scope="row">재발급 성공여부</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td><c:out value="${result.seq}"/></td>
					<td><c:out value="${result.adminYn eq 'Y' ? '관리자' : '사용자'}"/></td>
					<td><c:out value="${result.adminId}"/></td>
					<td><c:out value="${result.userid}"/></td>
					<td><c:out value="${result.ldate}"/></td>
					<td><c:out value="${result.successYn eq 'Y' ? '성공' : '실패'}"/></td>
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
		frm.p_action = "";
		frm.target = "_self";
		frm.submit();
	}

	function linkPage(idx){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.pageIndex.value = idx;
		frm.action = "/adm/cfg/mem/reissueMemberPwdList.do";
		frm.target = "_self";
		frm.submit();
	}
</script>