<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>

<%@page import="egovframework.com.cmm.service.Globals"%>
<%@page import="egovframework.com.cmm.service.EgovProperties"%><script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/lcms/selectbox_move.js"></script>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="s_upperclass" 	id="s_upperclass" 	value="${s_upperclass}">
	<input type="hidden" name="s_middleclass" 	id="s_middleclass" 	value="${s_middleclass}">
	<input type="hidden" name="s_lowerclass" 	id="s_lowerclass" 	value="${s_lowerclass}">
	<input type="hidden" name="s_contenttype" 	id="s_contenttype" 	value="${s_contenttype}">
	<input type="hidden" name="s_subjnm" 		id="s_subjnm" 		value="${s_subjnm}">
	<input type="hidden" name="s_subj" 			id="s_subj" 		value="${s_subj}">
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="doPageList()"><span>목록</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbDetail">
		<table summary="" width="100%" >
			<colgroup>
				<col width="20%" />
				<col width="" />
			</colgroup>
			<tbody>
				<tr>
					<th scope="col">[과정코드] 과정명</th>
					<td class="bold">
						[<c:out value="${data.subj}"/>] ${data.subjnm }
					</td>
				</tr>
				<tr>
					<th scope="col">컨텐츠 위치</th>
					<td class="bold">
						<%=EgovProperties.getProperty("Globals.contentPath")%>${data.subj}
					</td>
				</tr>
				<tr>
					<th scope="col">컨텐츠 유형</th>
					<td class="bold">
						${data.contenttypenm}
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	<br/>
	<!-- list table-->
	<div class="tbDetail">
		<table summary="" width="100%" >
			<colgroup>
				<col width="20%" />
				<col width="" />
			</colgroup>
			<tbody>
				<tr>
					<th scope="col">교육구분</th>
					<td class="bold">이러닝</td>
					<th scope="col">과정구분</th>
					<td class="bold">
						<c:choose>
							<c:when test="${subject.subjGu eq 'J'}">JIT</c:when>
							<c:when test="${subject.subjGu eq 'M'}">안전교육</c:when>
							<c:when test="${subject.subjGu eq 'E'}">전사과정</c:when>
							<c:otherwise>일반</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th scope="col">과정분류</th>
					<td class="bold">
						${subject.upperclassnm} &gt; ${subject.middleclassnm} &gt; ${subject.lowerclassnm}
					</td>
					<th scope="col">보유년도</th>
					<td class="bold">
						최초확보: ${subject.firstdate}, 심사연월: ${subject.judgedate}, 최종변경: ${subject.getdate}
					</td>
				</tr>
				<tr>
					<th scope="col">컨텐츠등급</th>
					<td class="bold">${subject.contentgrade}</td>
					<th scope="col">제작일자</th>
					<td class="bold">${subject.crdate}</td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- list table-->
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	//목록으로
	function doPageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.target = "_self";
		frm.action = "/adm/lcms/old/oldContentList.do";
		frm.submit();
	}

</script>