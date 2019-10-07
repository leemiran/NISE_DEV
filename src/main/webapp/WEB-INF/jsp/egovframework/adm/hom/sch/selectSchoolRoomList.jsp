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
	<input type="hidden" id="p_seq" name="p_seq">
	
	
	<!-- 검색박스 끝 -->
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="doView('')"><span>등록</span></a></div>
	</div>
        
		
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="4%" />
				<col width="20%" />
				<col width="40%" />
				<col width="%" />
				<col width="%" />
                   <col width="%" />										
			</colgroup>
			<thead>
				<tr>
					<th scope="row">No</th>
					<th scope="row">고사장명</th>
					<th scope="row">주소/위치</th>
					<th scope="row">전화</th>
					<th scope="row">사용여부</th>
					<th scope="row">관리</th>
				</tr>
			</thead>
			<tbody>
			
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td >${i.count}</td>
					<td class="left">
					<a href="#none" onclick="doView('${result.seq}')">
					${result.schoolNm}
					</a>
					</td>
					<td class="left">
					주소 : [${result.zipCd}] ${result.juso}
					<br>
					위치 : <a href="${result.areaMap}" target="_blank">${result.areaMap}</a>
					</td>
					<td>${result.phoneNo}</td>
					<td>${result.isuse}</td>
					<td ><a href="#none" onclick="doView('${result.seq}')" class="btn_search"><span>관리</span></a></td>
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

	var frm = eval('document.<c:out value="${gsMainForm}"/>');

	
	function doPageList() {
		frm.action = "/adm/hom/sch/selectSchoolRoomList.do";
		frm.target = "_self";
		frm.submit();
	}


	function doView(p_seq){
		frm.p_seq.value = p_seq;
		frm.action = "/adm/hom/sch/selectSchoolRoomView.do";
		frm.target = "_self";
		frm.submit();
	}

</script>