<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<base target="_self">
<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}");
	parent.pageReload();
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈  width=580,height=190-->
<div id="popwrapper">
	<div class="popIn" style="height:170px">
    	<div class="tit_bg">
			<h2>과정분류 등록</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<input type="hidden" 	id="p_upperclass" 		name="p_upperclass"		value="<c:out value="${p_upperclass}"/>">
		<input type="hidden" 	id="p_middleclass" 		name="p_middleclass"	value="<c:out value="${p_middleclass}"/>">
		<input type="hidden" 	id="p_lowerclass" 		name="p_lowerclass"		value="<c:out value="${p_lowerclass}"/>">
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="120px" />
					<col width="" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">
							<c:set var="textName"/>
							<c:set var="classCode"/>
							<c:if test="${p_middleclass eq '000' and p_lowerclass eq '000'}"><c:set var="textName" value="대"/><c:set var="classCode" value="${p_upperclass}"/></c:if>
							<c:if test="${p_middleclass ne '000' and p_lowerclass eq '000'}"><c:set var="textName" value="중"/><c:set var="classCode" value="${p_middleclass}"/></c:if>
							<c:if test="${p_middleclass ne '000' and p_lowerclass ne '000'}"><c:set var="textName" value="소"/><c:set var="classCode" value="${p_lowerclass}"/></c:if>
							<c:out value="${textName}"/>분류코드
						</th>
						<td class="bold"><c:out value="${classCode}"/></td>
					</tr>
					<tr>
						<th scope="col"><c:out value="${textName}"/>분류코드명</th>
						<td class="bold"><input type="text" id="p_classname" name="p_classname" value="<c:out value="${p_classname}"/>" style="width:90%;ime-mode:active" onfocus="this.select"></td>
					</tr>
				</tbody>
			</table>
		</div>
		</form>
		<!-- // contents -->
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:updateData()"><span>수정</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="javascript:deleteData()"><span>삭제</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">
	function updateData(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.p_classname.value == "" ){
			alert("<c:out value="${textName}"/>분류코드명을 입력하세요.");
			frm.p_classname.focus();
			return;
		}
		frm.action = "/adm/cfg/ccm/courseClassificationUpdate.do";
		frm.submit();
	}

	function deleteData(){
		if( !confirm("정말 삭제하시겠습니까?") ){
			return;
		}
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.action = "/adm/cfg/ccm/courseClassificationDelete.do";
		frm.submit();
	}
</script>