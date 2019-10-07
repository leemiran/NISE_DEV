<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}");
	opener.doPageList();
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn" style="height:195px">
    	<div class="tit_bg">
			<h2>Module 수정</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<input type="hidden" id="subj" 	 name="subj" 	value="${subj}"/>
		<input type="hidden" id="module" name="module" 	value="${module}"/>
		
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="20%" />
					<col width="" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">Module</th>
						<td class="bold">
							<c:out value="${module}"/>
						</td>
					</tr>
					<tr>
						<th scope="col">Module명</th>
						<td class="bold">
							<input type="text" id="moduleName" name="moduleName" class="ipt" style="width:98%;IME-MODE:active" value="<c:out value="${data.moduleName}"/>" onfocus="this.select()"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		</form>
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:doModuleUpdate()"><span>수정</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

	function doModuleUpdate(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.moduleName.value == "" ){
			alert("Module명을 입력하세요.");
			return;
		}
		frm.target = "_self";
		frm.action = "/adm/lcms/nct/lcmsModuleUpdate.do";
		frm.submit();
	}
</script>