<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type="hidden" name="p_menu"			id="p_menu"/>
	<input type="hidden" name="p_systemgubun"	id="p_systemgubun"/>
	
	<!-- search wrap-->
	<div class="searchWrap">
		<div class="in">
			<strong class="shTit">메뉴명</strong>
			<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}"/>
			<a href="#none" class="btn_search" onclick="searchList()"><span>검색</span></a>
		</div>
	</div>
</form>
	<!-- // search wrap -->
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="100px" />
				<col width="250px"/>
				<col />
				<col width="60px"/>
				<col width="60px" />
				<col width="80px" />
				<col width="90px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">메뉴코드</th>
					<th scope="row">메뉴코드명</th>
					<th scope="row">관련 Program</th>
					<th scope="row">메뉴순서</th>
					<th scope="row">사용여부</th>
					<th scope="row">모듈갯수</th>
					<th scope="row">모듈리스트</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<c:set var="color" value="#FFFFFF"/>
				<c:if test="${result.levels eq 1 }"><c:set var="color" value="#E7E7E7"/></c:if>
				<tr bgcolor="<c:out value="${color}"/>">
					<td><c:out value="${result.menu}"/></td>
					<td class="left"><c:out value="${result.menunm}"/></td>
					<td class="left">
						<c:out value="${result.pgm}"/>
						<c:out value="${not empty result.para1  ? ''  : ''}"/><c:out value="${result.para1}"/>
						<c:out value="${not empty result.para2  ? '&' : ''}"/><c:out value="${result.para2}"/>
						<c:out value="${not empty result.para3  ? '&' : ''}"/><c:out value="${result.para3}"/>
						<c:out value="${not empty result.para4  ? '&' : ''}"/><c:out value="${result.para4}"/>
						<c:out value="${not empty result.para5  ? '&' : ''}"/><c:out value="${result.para5}"/>
						<c:out value="${not empty result.para6  ? '&' : ''}"/><c:out value="${result.para6}"/>
						<c:out value="${not empty result.para7  ? '&' : ''}"/><c:out value="${result.para7}"/>
						<c:out value="${not empty result.para8  ? '&' : ''}"/><c:out value="${result.para8}"/>
						<c:out value="${not empty result.para9  ? '&' : ''}"/><c:out value="${result.para9}"/>
						<c:out value="${not empty result.para10 ? '&' : ''}"/><c:out value="${result.para10}"/>
						<c:out value="${not empty result.para11 ? '&' : ''}"/><c:out value="${result.para11}"/>
						<c:out value="${not empty result.para12 ? '&' : ''}"/><c:out value="${result.para12}"/>
					</td>
					<td><c:out value="${result.orders}"/></td>
					<td><c:out value="${result.isdisplay == 'Y' ? '사용' : '미사용'}"/></td>
					<td><c:out value="${result.cnt}"/></td>
					<td>
						<div class="btnR">
						<a href="#none" class="btn01" onclick="move_menusub('<c:out value="${result.menu}"/>', '<c:out value="${result.systemgubun}"/>')">
							<span>모듈리스트</span>
						</a>
						</div>
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


	function move_menusub(menu, systemgubun){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_menu.value = menu;
		frm.p_systemgubun.value = systemgubun;
		frm.action = "/adm/cfg/mod/menuModuleSubList.do";
		frm.submit();
	}

	function searchList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mod/menuModuleList.do";
		frm.submit();
	}

</script>