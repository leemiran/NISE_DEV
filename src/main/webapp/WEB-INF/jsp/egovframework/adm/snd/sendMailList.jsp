<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>



<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>

	<input type = "hidden" name="p_content"    value = "" />
	
	<input type="hidden" name="pageIndex" 	id="pageIndex"	value="${pageIndex}">
	
		
<div id="popwrapper"><!-- 팝업 사이즈 800*650 -->
	<div class="con2">
		<div class="popCon">
			<!-- header -->	
			
			<input type="hidden" name="dataCreatetxt" id="dataCreatetxt" >
		<!-- contents -->
			
			<!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="80"/>
                    <col width="%"/>
                    <col width="%"/>   			
				</colgroup>
				<thead>
					<tr>
						<th scope="row">번호</th>
						<th scope="row">제목</th>
						<th scope="row">발송시작시간</th>
						<th scope="row">발송종료시각</th>
						<th scope="row">발송량</th>
						<th scope="row">성공</th>
						<th scope="row">실패</th>
					</tr>
				</thead>
				<tbody>
				
<c:forEach items="${list}" var="result" varStatus="status">
					<tr>
						<td class="num"><c:out value="${(pageTotCnt+1)-(status.count+firstIndex)}"/></td>
						<td onclick="popupContent('${result.idx}');" style="cursor: pointer;"><c:out value="${result.subject}"/></td>
						<td><c:out value="${result.sdate}"/></td>
						<td><c:out value="${result.edate}"/></td>
						<td><c:out value="${result.total}"/></td>
						<td><c:out value="${result.success}"/></td>
						<td><c:out value="${result.falsecnt}"/></td>
						
					</tr>
</c:forEach>			
				</tbody>
			</table>
    </div>
		<!-- list table-->
			
		<!-- // contents -->
		
		<!-- 페이징 시작 -->
	<div class="paging">
		<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="doLinkPage"/>
	</div>
	<!-- 페이징 끝 -->
		
		</div>
	</div>
</div>
</form>
<form id="popupForm" name="popupForm" method="post">
	<input type="hidden" name="idx"/>
</form>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>

	
<script type="text/javascript">

	

	var frm = eval('document.<c:out value="${gsPopForm}"/>');

	
	function doLinkPage(index) {
						
		var url = "";
		
		url = "/adm/snd/sendMailList.do";
				
		frm.action = url;
		
		frm.pageIndex.value = index;
		frm.target = "_self";
		frm.submit();
	}
	
	function popupContent(idx) {
		var targetName = 'popupContent';
		window.open("",targetName,"left=100,top=100,width=735,height=768,toolbar=no,menubar=no,status=yes,scrollbars=yes,resizable=yes");
		
		var popupForm = document.popupForm;
		popupForm.idx.value = idx;
		
		popupForm.target = targetName;
		popupForm.action = '/adm/snd/sendMailContent.do';
		popupForm.submit();
	}

</script>
	
	