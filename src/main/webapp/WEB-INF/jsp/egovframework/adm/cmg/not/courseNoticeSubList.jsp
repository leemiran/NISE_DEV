<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_action" 	id="p_action">
	<input type="hidden" name="p_seq" 		id="p_seq"		value="">
	<input type="hidden" name="p_subj" 		id="p_subj"		value="${p_subj}">
	<input type="hidden" name="p_year" 		id="p_year"		value="${p_year}">
	<input type="hidden" name="p_subjseq" 	id="p_subjseq"	value="${p_subjseq}">
	<input type="hidden" name="p_subjnm" 	id="p_subjnm"	value="${p_subjnm}">
	<input type="hidden" name="p_subjseq2" 	id="p_subjseq2"	value="${p_subjseq2}">
	
	<!-- search wrap-->
	<div class="searchWrap">
		<div class="in" style="width:100%">
			<select name="search" id="search">
				<option value="title"  <c:if test="${search eq 'title'}">selected</c:if>>제목</option>
				<option value="name"   <c:if test="${search eq 'name'}">selected</c:if>>작성자</option>
				<option value="userid" <c:if test="${search eq 'name'}">selected</c:if>>아이디</option>
				<option value="ldate"  <c:if test="${search eq 'ldate'}">selected</c:if>>작성일자</option>
			</select>
			<input type="text" name="search_text" id="search_text" value="${search_text}" style="ime-mode:active" onkeypress="javascript:fn_keyEvent('doPageList')"/>
			<a href="#none" class="btn_search" onclick="doPageList()"><span>검색</span></a>
		</div>
	</div>
	<!-- // search wrap -->
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="parentListPage()"><span>목록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="insertPage()"><span>등록</span></a></div>
		<b>${p_subjnm} - ${p_subjseq2}기</b>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="40px" />
				<col width="70px" />
				<col />
				<col width="120px" />
				<col width="100px" />
				<col width="60px" />
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>구분</th>
					<th>제목</th>
					<th>작성자</th>
					<th>작성일</th>
					<th>조회</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${noticeList}" var="result">
				<tr>
					<td><c:out value="${result.seq}"/></td>
					<td><c:out value="${result.typesnm}"/></td>
					<td class="left">
						<a href="#none" onclick="viewPage('${result.seq}')"><c:out value="${result.title}"/></a>
					</td>
					<td><c:out value="${result.gadmin}"/></td>
					<td><c:out value="${result.addate}"/></td>
					<td><c:out value="${result.cnt}"/></td>
				</tr>
			</c:forEach>
			<c:if test="${empty noticeList}">
				<tr>
					<td colspan="6">조회된 내용이 없습니다.</td>
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

	/* ********************************************************
	 * 페이징처리 함수
	 ******************************************************** */
	 function doPageList() {
		 var frm = eval('document.<c:out value="${gsMainForm}"/>');
		 frm.action = "/adm/cmg/not/courseNoticeSubList.do";
		 frm.p_action.value = "go";
		 frm.target = "_self";
		 frm.submit();
	}

	function parentListPage(){
		 var frm = eval('document.<c:out value="${gsMainForm}"/>');
		 frm.action = "/adm/cmg/not/courseNoticeList.do";
		 frm.p_action.value = "go";
		 frm.target = "_self";
		 frm.submit();
	}

	function viewPage(seq){
		 var frm = eval('document.<c:out value="${gsMainForm}"/>');
		 frm.action = "/adm/cmg/not/courseNoticeView.do";
		 frm.p_seq.value = seq;
		 frm.target = "_self";
		 frm.submit();
	}

	function insertPage(){
		 var frm = eval('document.<c:out value="${gsMainForm}"/>');
		 frm.action = "/adm/cmg/not/courseNoticeInsertPage.do";
		 frm.target = "_self";
		 frm.submit();
	}

</script>