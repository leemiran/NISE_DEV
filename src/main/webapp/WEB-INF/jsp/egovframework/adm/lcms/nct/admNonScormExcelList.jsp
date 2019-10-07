<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}");
	opener.parent.doPageList();
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
		<input type="hidden" name="subj" value="${subj}">
		<input type="hidden" name="contentType" value="${contentType}">
		<input type="hidden" name="pageIndex" value="${pageIndex}">
		
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:doExcelInsert()"><span>등록</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="javascript:doCancelFile()"><span>취소</span></a></li>
		</ul>
		<!-- // button -->
		<!-- contents -->
		<div class="popCon">
			<div id="loadingBars" style="top:300; left:150; width: 400; z-index: 0; position: absolute; margin: 0px; padding-left:120px; display: none;" align="center">
				<span style="width: 300px; border: 5px solid red; background-color: #FFFFFF; text-align: center; font-weight: bold; padding:10px;">
				콘텐츠를 등록 중입니다.  잠시만 기다려 주세요.
				<img src="/images/loading.gif"/>
				</span>
			</div>
<c:if test="${contentType == 'C'}">				
			<table summary="" width="100%" class="popTb">
				<colgroup>
	                <col width="15%" />
	                <col width="30%" />
	                <col width="40%" />
	                <col width="15%" />
				</colgroup>
				<thead>
					<tr>
						<th scope="row">구분</th>
						<th scope="row">제목</th>
						<th scope="row">시작위치</th>
						<th scope="row">총페이지수</th>
					</tr>
				</thead>
				<tbody>
	            <c:forEach items="${list}" var="result">
	            	<tr <c:if test="${result.parameter1 == '00'}">style="background-color:#EEE7D3"</c:if>>
	            		<td><c:out value="${result.parameter1 == '00' ? 'Module' : 'Lesson'}"/></td>
	            		<td class="left">
	            			<c:if test="${result.parameter1 != '00'}">&nbsp;&nbsp;</c:if>
	            			<c:if test="${result.parameter3 == '3'}">&nbsp;&nbsp;-</c:if>
	            			<c:out value="${result.parameter5}"/>
	            		</td>
	            		<td class="left">
	            			<c:if test="${result.parameter7 != 'NA' && result.parameter7 != ''}">
	            			<c:out value="${SubDir}"/>/docs/<c:out value="${result.parameter0}"/>/<c:out value="${result.parameter7}"/>
	            			</c:if>
	            		</td>
	            		<td><c:out value="${result.parameter8}"/></td>
	            	</tr>
	            </c:forEach>
				</tbody>
			</table>
</c:if>
<c:if test="${contentType != 'C'}">
			<table summary="" width="100%" class="popTb">
				<colgroup>
	                <col width="5%" />
	                <col width="30%" />
	                <col width="5%" />
	                <col width="20%" />
	                <c:if test="${contentType != 'P'}">
	                <col width="40%" />
	                </c:if>
	                <c:if test="${contentType == 'P'}">
	                <col width="30%" />
	                <col width="10%" />
	                </c:if>
				</colgroup>
				<thead>
					<tr>
						<th scope="row">Module</th>
						<th scope="row">Module명</th>
						<th scope="row">Lesson</th>
						<th scope="row">Lesson명</th>
						<th scope="row">시작위치</th>
		                <c:if test="${contentType == 'P'}">
						<th scope="row">교육시간</th>
		                </c:if>
					</tr>
				</thead>
				<tbody>
	            <c:set var="oldModule" value=""/>
	            <c:forEach items="${list}" var="result">
	            	<tr>
	            		<c:if test="${oldModule != result.parameter0}">
	            		<td rowspan="<c:out value="${result.merge}"/>"><c:out value="${result.parameter0}"/></td>
	            		<td rowspan="<c:out value="${result.merge}"/>" class="subject"><c:out value="${result.parameter1}"/></td>
			            <c:set var="oldModule" value="${result.parameter0}"/>
	            		</c:if>
	            		<td><c:out value="${result.parameter2}"/></td>
	            		<td class="left"><c:out value="${result.parameter3}"/></td>
	            		<td class="left"><c:out value="${result.parameter4}"/></td>
		                <c:if test="${contentType == 'P'}">
	            		<td class="left"><c:out value="${result.parameter5}"/></td>
		                </c:if>
	            	</tr>
	            </c:forEach>
				</tbody>
			</table>
</c:if>
		</div>
		<!-- // contents -->
		</form>
		<iframe id="hiddenFrame" name="hiddenFrame" src="" width="0" height="0" ></iframe>
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:doExcelInsert()"><span>등록</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="javascript:doCancelFile()"><span>취소</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

	var tempTime = "";
	function winResize(){
	    var Dwidth = parseInt(document.body.scrollWidth);
	    var Dheight = parseInt(document.body.scrollHeight);
	    var divEl = document.createElement("div");
	    
		divEl.style.position = "absolute";
		divEl.style.left = "0px";
		divEl.style.top = "0px";
		divEl.style.width = "100%";
		divEl.style.height = "100%";
		document.body.appendChild(divEl);
		
	    var tmpWidth = Dwidth-divEl.offsetWidth;
	
	    var status = false;
		window.document.body.scroll = "auto";
		if( document.body.clientHeight > 369 ){
			window.resizeBy(0,0);
			status = true;
		}
		if( document.body.clientWidth < 700 ){
		    window.resizeBy(700, 220);
		}
		document.body.removeChild(divEl);
	}
	
	winResize();

	function doExcelInsert(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		document.getElementById("loadingBars").style.display = "";
		var insertURL = "<c:out value="${gsDomainContext}"/>/adm/lcms/nct/LcmsExcelInsert.do"; //scorm : default
		if( frm.contentType.value == "C" ){ //중공교
			insertURL = "<c:out value="${gsDomainContext}"/>/adm/lcms/nct/LcmsCotiExcelInsert.do";
		}
		frm.target = "_self";
		frm.action = insertURL;
		frm.submit();
	}

    function doCancelFile(){
    	var frm = eval('document.<c:out value="${gsPopForm}"/>');
    	frm.target = "_self";
		frm.action = "/adm/lcms/nct/LcmsNonScormCancel.do";
		frm.submit();
    }

</script>