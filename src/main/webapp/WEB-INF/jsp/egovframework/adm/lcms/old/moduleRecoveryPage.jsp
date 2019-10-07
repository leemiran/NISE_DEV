<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>Module 백업 관리</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
			<input type="hidden" name="subj" value="${subj}">
			<input type="hidden" name="pageIndex">
			<input type="hidden" name="groupSeq">
		</form>
		
		<!-- contents -->
		<div class="popCon" style="text-align: center;">
			<div class="tbList">
				<table summary="" cellspacing="0" width="100%">
		                <caption>목록</caption>
		                <colgroup>
						<col width="30px" />
						<col width="80px" />
						<col width="110px" />
					</colgroup>
					<thead>
						<tr>
							<th>백업순번</th>
							<th>백업날짜</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${moduleList}" var="result">
						<tr>
							<td>${result.groupSeq}</td>
							<td>${result.backupDate}</td>
							<td>
								<a href="#" class="pop_btn01" onclick="javascript:recoveryModule('${result.groupSeq}');return false;"><span>복구</span></a>
								<a href="#" class="pop_btn01" onclick="javascript:deleteModuleBackup('${result.groupSeq}');return false;"><span>백업삭제</span></a>
								<a href="#" class="pop_btn01" onclick="javascript:moduleBackupExcelDown('${result.groupSeq}');return false;"><span>백업엑셀다운</span></a>
							</td>
						</tr>
					</c:forEach>
					<c:if test="${empty moduleList}">
						<tr>
							<td colspan="3">백업 내용이 없습니다.</td>
						</tr>
					</c:if>
					</tbody>
				</table>
			</div>
			<!-- list table-->
			<!-- 페이징 시작 -->
			<div class="paging">
				<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="doLinkPage"/>
			</div>
			<!-- 페이징 끝 -->
		</div>
		<!-- // contents -->
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">
	function doLinkPage(pageIndex) {
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.action = "/adm/lcms/old/moduleRecoveryPage.do";
		frm.pageIndex.value = pageIndex;
		frm.target = "_self";
		frm.submit();
	}

	function recoveryModule(groupSeq) {
		if(!confirm('복구 하시겠습니까?')) return;
		
		$.ajax({
			url: '${pageContext.request.contextPath}/adm/lcms/old/recoveryModule.do'
			, type: 'post'
			, data: {
				groupSeq: groupSeq
				, subj: '${subj}'
			}
			, dataType: 'json'
			, success: function(result) {
				alert(result.resultMsg);
				opener.doPageReload();
			}
			, error: function(xhr, status, error) {
				console.log(status);
				console.log(error);   
			}
		});
	}
	
	function deleteModuleBackup(groupSeq) {
		if(!confirm('삭제 하시겠습니까?')) return;
		
		$.ajax({
			url: '${pageContext.request.contextPath}/adm/lcms/old/deleteModuleBackup.do'
			, type: 'post'
			, data: {
				groupSeq: groupSeq
				, subj: '${subj}'
			}
			, dataType: 'json'
			, success: function(result) {
				alert(result.resultMsg);
				location.reload();
			}
			, error: function(xhr, status, error) {
				console.log(status);
				console.log(error);   
			}
		});
	}
	
	function moduleBackupExcelDown(groupSeq) {
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.target = "_self";
		frm.action = "/adm/lcms/old/moduleBackupExcelDown.do";
		frm.groupSeq.value = groupSeq;
		frm.submit();
	}
</script>