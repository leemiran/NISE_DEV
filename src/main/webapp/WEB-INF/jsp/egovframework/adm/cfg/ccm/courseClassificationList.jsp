<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" 	id="p_upperclass" 		name="p_upperclass">
	<input type="hidden" 	id="p_middleclass" 		name="p_middleclass">
	<input type="hidden" 	id="p_lowerclass" 		name="p_lowerclass">
	<input type="hidden" 	id="p_classname" 		name="p_classname">
</form>
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="insertPage()"><span>등록</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="5%"/>
				<col width="10%"/>
				<col width="20%" />
				<col width="10%"/>
				<col width="20%" />
				<col width="10%"/>
				<col width="20%" />
				<col width="5%" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">No</th>
					<th scope="row" colspan="2">대분류</th>
					<th scope="row" colspan="2">중분류</th>
					<th scope="row" colspan="2">소분류</th>
					<th scope="row" >사용유무</th>
				</tr>
			</thead>
			<tbody>
				<tr>
				<c:set var="cnt" value="1"/>
			<c:forEach items="${list}" var="result">
				<c:if test="${result.middleclass eq '000' and result.lowerclass eq '000'}">
					<td rowspan="<c:out value="${result.upperCnt}"/>"><c:out value="${cnt}"/><c:set var="cnt" value="${cnt+1}"/></td>
					<td rowspan="<c:out value="${result.upperCnt}"/>">
						<b>
						<a href="#" onclick="updatePage('<c:out value="${result.upperclass}"/>', '<c:out value="${result.middleclass}"/>', '<c:out value="${result.lowerclass}"/>', '<c:out value="${result.classname}"/>')"><c:out value="${result.upperclass}"/></a>
						</b>
					</td>
					<td rowspan="<c:out value="${result.upperCnt}"/>"><c:out value="${result.classname}"/></td>
					<c:if test="${result.upperCnt eq '0'}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td><c:out value="${result.useYn}"/></td>
					</tr>
					<tr>
					</c:if>
				</c:if>
				<c:if test="${ result.middleclass ne '000' and result.lowerclass eq '000' }">
					<td rowspan="<c:out value="${result.middleCnt}"/>">
						<b>
						<a href="#" onclick="updatePage('<c:out value="${result.upperclass}"/>', '<c:out value="${result.middleclass}"/>', '<c:out value="${result.lowerclass}"/>', '<c:out value="${result.classname}"/>')"><c:out value="${result.middleclass}"/></a>
						</b>
					</td>
					<td rowspan="<c:out value="${result.middleCnt}"/>"><c:out value="${result.classname}"/></td>
				</c:if>
				<c:if test="${ result.middleclass ne '000' and result.lowerclass ne '000'}">
					<td>
						<b>
						<a href="#" onclick="updatePage('<c:out value="${result.upperclass}"/>', '<c:out value="${result.middleclass}"/>', '<c:out value="${result.lowerclass}"/>', '<c:out value="${result.classname}"/>')"><c:out value="${result.lowerclass}"/></a>
						</b>
					</td>
					<td><c:out value="${result.classname}"/></td>
					<td><c:out value="${result.useYn}"/></td>
				</tr>
				<tr>
				</c:if>
				<c:if test="${ result.middleclass ne '000' and result.lowerclass eq '000' and result.middleCnt eq 0 }">
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
				</c:if>
			</c:forEach>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	function pageReload(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/ccm/courseClassificationList.do";
		frm.target = "_self";
		frm.submit();
	}
	
	function insertPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open( "", "insertPopup", "width=580,height=430");
		frm.action = "/adm/cfg/ccm/courseClassificationInsertPage.do";
		frm.target = "insertPopup";
		frm.submit();
	}

	function updatePage(upperclass, middleclass, lowerclass, classname){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_upperclass.value = upperclass;
		frm.p_middleclass.value = middleclass;
		frm.p_lowerclass.value = lowerclass;
		frm.p_classname.value = classname;
        window.open( "", "updatePopup", "width=580,height=190");
		frm.action = "/adm/cfg/ccm/courseClassificationUpdatePage.do";
		frm.target = "updatePopup";
		frm.submit();
	}
</script>