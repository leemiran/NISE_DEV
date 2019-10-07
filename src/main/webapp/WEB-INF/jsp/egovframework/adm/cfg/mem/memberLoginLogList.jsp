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
	<input type="hidden" id="p_key1" name="p_key1" value="${p_key1}">
	
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
				<col />
				<col width="180px"/>
				<col width="140px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">번호</th>
					<th scope="row">이름</th>
					<th scope="row">ID</th>
					<th scope="row">근무지</th>
					<th scope="row">IP</th>
					<th scope="row">접속 시간 시간</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="status">
				<tr>
					<%-- <td><c:out value="${result.seq}"/></td> --%>
					<td class="num"><c:out value="${(pageTotCnt+1)-(status.count+firstIndex)}"/></td>
					<td><c:out value="${result.name}"/></td>
					<td><c:out value="${result.userid}"/></td>
					<td><c:out value="${result.userPath}"/></td>
					<td><c:out value="${result.lgip}"/></td>
					<td><c:out value="${result.ldate}"/></td>
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
	function linkPage(index){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.pageIndex.value = index;
		frm.action = "/adm/cfg/mem/selectMemberLoginLogList.do";
		frm.target = "_self";
		frm.submit();
	}
</script>