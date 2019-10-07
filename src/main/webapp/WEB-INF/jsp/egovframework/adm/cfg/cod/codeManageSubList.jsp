<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="p_gubun" 	name="p_gubun" value="<c:out value="${p_gubun}"/>">
	<input type="hidden" id="p_code"  	name="p_code" >
	<input type="hidden" id="p_levels"  name="p_levels" value="<c:out value="${p_levels}"/>" >
	<input type="hidden" id="p_upper"   name="p_upper" value="<c:out value="${p_upper}"/>">
	<!-- search wrap-->
	<div class="searchWrap">
		<div class="in">
			<strong class="shTit">소분류코드명</strong>
			<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}"/>
			<a href="#none" class="btn_search" onclick="searchList()"><span>검색</span></a>
		</div>
	</div>
	<!-- // search wrap -->
</form>
	<div class="listTop">	
		<b>대분류코드 : </b><c:out value="${view.gubun}"/>&nbsp;&nbsp;&nbsp;
		<b>대분류코드명 : </b><c:out value="${view.gubunnm}"/>
		<div class="btnR"><a href="#" class="btn01" onclick="mainList()"><span>대분류코드</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="insertPage()"><span>등록</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="200px"/>
				<col />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">소분류코드</th>
					<th scope="row">소분류코드명</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<td><c:out value="${result.code}"/></td>
					<td class="left">
						<a href="#none" onclick="view('<c:out value="${result.code}"/>', '<c:out value="${result.levels}"/>')">
						<c:out value="${result.codenm}"/>
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
	function mainList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/cod/codeManageList.do"
		frm.submit();
	}
	
	function view(code, levels){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_code.value = code;
		frm.p_levels.value = levels;
		frm.action = "/adm/cfg/cod/codeManageSubView.do"
		frm.submit();
	}

	function insertPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/cod/codeManageSubInsertPage.do"
		frm.submit();
	}
</script>