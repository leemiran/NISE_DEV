<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type = "hidden" name="p_seq"     value = "" />
	
	
	<!-- 검색박스 시작-->
	<div class="searchWrap txtL">
		<div>		
			<ul class="datewrap">
				<li class="floatL">
					<select name="p_search" id="p_search">
						<option value="name"	<c:if test="${p_search eq 'name'}">selected</c:if>>이름</option>
						<option value="JUMINNUM"	<c:if test="${p_search eq 'JUMINNUM'}">selected</c:if>>생년월일</option>
						<option value="SOSOCK"	<c:if test="${p_search eq 'SOSOCK'}">selected</c:if>>소속</option>
					</select>
					<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}" size="50" onkeypress="fn_keyEvent('doPageList')" style="ime-mode:active;"/>
					<a href="#none" class="btn_search" onclick="doPageList()"><span>검색</span></a>
				</li>
			</ul>		
		</div>
	</div>
	<!-- 검색박스 끝 -->
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="doView('')"><span>등록</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>수험번호</th>
					<th>지역</th>
					<th>소속</th>
					<th>직명</th>
					<th>이름</th>
					<th>과정명</th>
					<th>이수번호</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td >${result.seq}</td>
					<td >${result.serno}</td>
					<td >${result.area}</td>
					<td >${result.sosock}</td>
					<td >${result.jickname}</td>
					<td >
					<a href="#none" onclick="doView('${result.seq}')">
					${result.name}
					</a>
					</td>
					<td class="left">
					<a href="#none" onclick="doView('${result.seq}')">
					${result.couname}
					</a>
					</td>
					<td >${result.compnumber}</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="20">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	<!-- 페이징 시작 -->
	<div class="paging">
		<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="doLinkPage"/>
	</div>
	<!-- 페이징 끝 -->
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	function doPageList() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/fin/finishOldList.do";
		frm.target = "_self";
		frm.submit();
	}

	function doLinkPage(index) {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/fin/finishOldList.do";
		frm.pageIndex.value = index;
		frm.target = "_self";
		frm.submit();
	}


	function doView(p_seq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_seq.value = p_seq;
		frm.action = "/adm/fin/finishOldView.do";
		frm.target = "_self";
		frm.submit();
	}
	
</script>