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
	<input type = "hidden" name = "p_faqcategory" value = "">
	
	
	
	
	<!-- 검색박스 시작-->
	<div class="searchWrap txtL">
	
		<div>		
			<ul class="datewrap">
				<li class="floatL">
					<strong class="shTit">FAQ 카테고리명 검색 :</strong>
					<input type="text" name="search_faqcategorynm" id="search_faqcategorynm" value="${search_faqcategorynm}" size="50" onkeypress="fn_keyEvent('doPageList')" style="ime-mode:active;"/>
					<a href="#none" class="btn_search" onclick="doPageList()"><span>검색</span></a>
				</li>
			</ul>		
		</div>
	</div>
	<!-- 검색박스 끝 -->
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="doEdit('')"><span>등록</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="150px" />
				<col />
				<col width="150px" />
				<col width="80px" />
			</colgroup>
			<thead>
				<tr>
					<th>FAQ 카테고리코드</th>
					<th>FAQ 카테고리명</th>
					<th>FAQ 개수</th>
					<th>수정</th>
				</tr>
			</thead>
			<tbody>
			
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td >${result.faqcategory}</td>
					<td class="left">
					<a href="#none" onclick="doView('${result.faqcategory}')">
					${result.faqcategorynm}
					</a>
					</td>
					<td class="num">${result.catecount}개</td>
					<td ><a href="#none" onclick="doEdit('${result.faqcategory}')" class="btn_search"><span>관 리</span></a></td>
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
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	function doPageList() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/hom/faq/selectCategoryList.do";
		frm.target = "_self";
		frm.submit();
	}


	function doView(p_faqcategory){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_faqcategory.value = p_faqcategory;
		frm.action = "/adm/hom/faq/selectFaqList.do";
		frm.target = "_self";
		frm.submit();
	}


	function doEdit(p_faqcategory){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_faqcategory.value = p_faqcategory;
		frm.action = "/adm/hom/faq/selectCategoryView.do";
		frm.target = "_self";
		frm.submit();
	}
	
</script>