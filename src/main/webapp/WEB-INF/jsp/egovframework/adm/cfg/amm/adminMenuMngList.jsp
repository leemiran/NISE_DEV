<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="p_menu" 		name="p_menu"		value="<c:out value="${p_menu}"/>"/>
	<input type="hidden" id="p_levels" 		name="p_levels"		value="<c:out value="${p_levels}"/>"/>
	<input type="hidden" id="p_parent" 		name="p_parent"		value="<c:out value="${p_parent}"/>"/>
	<input type="hidden" id="p_upper" 		name="p_upper"		value=""/>
	<input type="hidden" id="p_oldupper" 	name="p_oldupper" 	value="<c:out value="${ not empty p_parent && p_oldupper != p_menu ? upperInfo.menu : ''}"/>"/>
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
	
	<div class="listTop">	
		<c:if test="${not empty p_parent && p_oldupper != p_menu}">
		<Strong>상위메뉴코드 : </Strong><a href="#none" onclick="move_list('<c:out value="${upperInfo.upper}"/>', '<c:out value="${upperInfo.parent}"/>', '<c:out value="${upperInfo.levels}"/>')"><Strong><c:out value="${upperInfo.menu}"/></Strong></a>
		<br/>
		<Strong>상위메뉴명 : </Strong>	<c:out value="${upperInfo.menunm}"/>
		</c:if>
	    <div class="btnR"><a href="#" class="btn01" onclick="authPage()"><span>메뉴별 권한설정</span></a></div>
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
				<col width="80px" />
				<col width="80px" />
				<col width="100px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">메뉴코드</th>
					<th scope="row">메뉴코드명</th>
					<th scope="row">관련 Program</th>
					<th scope="row">메뉴순서</th>
					<th scope="row">사용여부</th>
					<th scope="row">하위코드</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<td><c:out value="${result.menu}"/></td>
					<td class="left">
						<a href="#none" onclick="view('<c:out value="${result.menu}"/>', '<c:out value="${result.levels}"/>', '<c:out value="${result.parent}"/>', '<c:out value="${result.upper}"/>')">
							<c:out value="${result.menunm}"/>
						</a>
					</td>
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
					<td>
						<a href="#none" class="btn_search01" onclick="move_list('<c:out value="${result.menu}"/>', '<c:out value="${result.parent}"/>', '<c:out value="${result.levels + 1}"/>')">
							<span>하위코드</span>
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

	function authPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/aut/menuAuthUpdatePage.do";
		frm.submit();
	}

	function view(menu, levels, parent, upper){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_menu.value = menu;
		frm.p_levels.value = levels;
		frm.p_parent.value = parent;
		frm.p_upper.value = upper;
		frm.action = "/adm/cfg/amm/adminMenuMngView.do";
		frm.submit();
	}

	function move_list(menu, parent, levels){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_menu.value = menu;
		frm.p_levels.value = levels;
		frm.p_parent.value = parent;
		frm.action = "/adm/cfg/amm/adminMenuMngList.do";
		frm.submit();
	}

	function insertPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/amm/adminMenuMngInsertPage.do";
		frm.submit();
	}

	function searchList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/amm/adminMenuMngList.do";
		frm.submit();
	}
</script>