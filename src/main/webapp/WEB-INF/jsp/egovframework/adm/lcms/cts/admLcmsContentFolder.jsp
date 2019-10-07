<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<%@page import="egovframework.com.cmm.service.Globals"%><script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg }">
	alert("${resultMsg}");
	opener.doPageReload();
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn" style="height:480px">
    	<div class="tit_bg">
			<h2>폴더생성</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<input type="hidden" id="path" name="path" value="${path}"/>
		
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="120px" />
					<col width="" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">현재경로</th>
						<td class="bold">
							<%=Globals.CONTENT_PATH%><c:out value="${path}" />
						</td>
					</tr>
					<tr>
						<th scope="col">폴더명</th>
						<td class="bold">
							<input type="text" id="folderName" name="folderName" style="width:98%;IME-MODE:disabled"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		</form>
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:doCreateFolder()"><span>폴더추가</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

	function doCreateFolder(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.folderName.value == "" ){
			alert("폴더명을 입력하세요.");
			return;
		}
		frm.target = "_self";
		frm.action = "/adm/lcms/cts/createContentFolder.do";
		frm.submit();
	}
</script>