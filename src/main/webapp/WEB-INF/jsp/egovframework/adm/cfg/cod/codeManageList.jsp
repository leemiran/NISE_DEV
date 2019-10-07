<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="p_gubun" name="p_gubun">
	<input type="hidden" id="p_levels" name="p_levels">
</form>
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="insertPage()"><span>등록</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="150px"/>
				<col />
				<col width="120px" />
				<col width="120px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">대분류코드</th>
					<th scope="row">대분류코드명</th>
					<th scope="row">코드자동등록여부</th>
					<th scope="row">하위소분류코드</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<td><c:out value="${result.gubun}"/></td>
					<td class="left">
						<a href="#none" onclick="view('<c:out value="${result.gubun}"/>')">
						<c:out value="${result.gubunnm}"/>
						</a>
					</td>
					<td><c:out value="${result.issystem == 'N' ? '자동' : '수동'}"/>등록</td>
					<td>
						<a href="#" class="btn01" onclick="subList('<c:out value="${result.gubun}"/>')">
							<span>하위소분류코드</span>
						</a>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	function view(gubun){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_gubun.value = gubun;
		frm.action = "/adm/cfg/cod/codeManageView.do"
		frm.submit();
	}

	function subList(gubun){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_gubun.value = gubun;
		frm.p_levels.value = 1;
		frm.action = "/adm/cfg/cod/codeManageSubList.do"
		frm.submit();
	}

	function insertPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/cod/codeManageInsertPage.do"
		frm.submit();
	}
</script>