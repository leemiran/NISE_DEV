<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}");
	opener.doSelectBrowser();
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn" style="height:480px">
    	<div class="tit_bg">
			<h2><c:out value="${type == 'ORG' ? '주차정보' : '강정보'}"/> 수정</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" enctype="multipart/form-data">
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="120px" />
					<col width="" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col"><c:out value="${type == 'ORG' ? '주차' : '강'}"/>명</th>
						<td class="bold">
							<input type="text" value="<c:out value="${attributeNm}"/>" id="attributeNm" name="attributeNm" onfocus="this.select()"/>
							<input type="hidden" id="attributeId" name="attributeId" value="<c:out value="${attributeId}"/>" />
							<input type="hidden" id="type" name="type" value="<c:out value="${type}"/>" />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		</form>
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:doAttributeUpdate()"><span>수정</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">
	window.focus();
	function doAttributeUpdate(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.attributeNm.value == "" ){
			alert("<c:out value="${type == 'ORG' ? '주차' : '강'}"/>명을 입력하세요.");
			frm.attributeNm.focus();
			return;
		}
		frm.target = "_self";
		frm.action = "/adm/lcms/sco/editAttribute.do";
		frm.submit();
	}
</script>