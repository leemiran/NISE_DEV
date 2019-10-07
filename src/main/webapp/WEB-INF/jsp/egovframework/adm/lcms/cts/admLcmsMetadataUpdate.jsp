<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg }">
	alert("${resultMsg}");
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn" >
    	<div class="tit_bg">
			<h2>콘텐츠 목록</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<input type="text" id="objType" name="objType" value="${objType}"/>
		<input type="hidden" id="objSeq" name="objSeq" value="${objSeq}"/>
		<input type="hidden" id="subj" name="subj" value="${subj}"/>
		
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:doLcmsMetadataUpdate()"><span>수정</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="javascript:window.close()"><span>취소</span></a></li>
		</ul>
		<!-- // button -->
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
	                <col width="15%" />
	                <col width="35%" />
	                <col width="30%" />
	                <col width="10%" />
					<c:if test="${contentType != '07'}">
	                <col width="10%" />
					</c:if>
				</colgroup>
				<thead>
					<tr>
						<th scope="row">이름</th>
						<th scope="row">경로</th>
						<th scope="row">값</th>
					</tr>
				</thead>
				<tbody>
	            <c:forEach items="${metaList}" var="result">
					<tr>
						<td class="subject"><c:out value="${result.elementTitle}"/></td>
						<td class="subject"><c:out value="${result.elementPath}"/>/<c:out value="${result.elementName}"/></td>
						<td>
							<c:if test="${result.readonly == 'N'}">
							<c:out value="${result.elementVal}"/>
							<input type="hidden" id="elementVal" name="elementVal" value="${result.elementVal}"/>
							</c:if>
							<c:if test="${result.readonly != 'N'}">
							<input type="text" class="input" style="width:95%" id="elementVal" name="elementVal" value="${result.elementVal}"/>
							</c:if>
							<input type="hidden" id="metadataSeq" name="metadataSeq" value="${result.metadataSeq}"/>
							<input type="hidden" id="elementTitle" name="elementTitle" value="${result.elementTitle}"/>
							<input type="hidden" id="elementPath" name="elementPath" value="${result.elementPath}"/>
							<input type="hidden" id="elementName" name="elementName" value="${result.elementName}"/>
							<input type="hidden" id="elementSeq" name="elementSeq" value="${result.elementSeq}"/>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		</form>
		<iframe id="hiddenFrame" name="hiddenFrame" src="" width="0" height="0" ></iframe>
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:doLcmsMetadataUpdate()"><span>수정</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="javascript:window.close()"><span>취소</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

	function doLcmsMetadataUpdate(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.target = "_self";
		frm.action = "/adm/lcms/cts/LcmsMetadataUpdate.do";
		frm.submit();
	}
</script>