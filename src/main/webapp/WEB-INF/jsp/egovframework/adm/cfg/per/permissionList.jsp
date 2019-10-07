<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="p_padmin" name="p_padmin"/>
	<input type="hidden" id="p_gadmin" name="p_gadmin"/>
</form>
	<!-- // search wrap -->
	<div class="listTop">			
		<div class="btnR"><a href="#" class="btn01" onclick="insertPage()"><span>등록</span></a></div>
	</div>
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="100px" />
				<col width="250px"/>
				<col />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">권한코드</th>
					<th scope="row">권한명</th>
					<th scope="row">권한 사용용도</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<td><c:out value="${result.gadmin}"/></td>
					<td><a href="#none" onclick="updatePage('<c:out value="${result.padmin}"/>', '<c:out value="${result.gadmin}"/>')"><c:out value="${result.gadminnm}"/></a></td>
					<td><c:out value="${result.comments}"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">



	function updatePage(padmin, gadmin){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_padmin.value = padmin;
		frm.p_gadmin.value = gadmin;
		frm.action = "/adm/cfg/per/permissionUpdatePage.do";
		frm.submit();
	}
	
	function insertPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/per/permissionInsertPage.do";
		frm.submit();
	}

</script>