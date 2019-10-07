<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}");
	opener.doLinkPage("${pageIndex}");
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
		<input type="hidden" name="org" value="${subj}">
		<input type="hidden" name="contentType" value="${contentType}">
		<input type="hidden" name="pageIndex" value="${pageIndex}">
		
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:doManifestInsert()"><span>등록</span></a></li>
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
			<table summary="" width="100%" class="popTb">
				<colgroup>
	                <col width="15%" />
	                <col width="35%" />
	                <col width="30%" />
	                <col width="10%" />
					<c:if test="${contentType != 'X'}">
	                <col width="10%" />
					</c:if>
				</colgroup>
				<thead>
					<tr>
						<th scope="row">[번호]구분</th>
						<th scope="row">제목</th>
						<th scope="row">자료파일명</th>
						<th scope="row">스콤유형</th>
						<c:if test="${contentType != 'X'}">
						<th scope="row">이수기준점수</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
		            <c:forEach items="${maniList}" var="result">
		            	<tr <c:if test="${result.type == 'Y'}">style="background-color:#EEE7D3"</c:if>>
		            		<td class="subject"><c:out value="${result.gubun}"/></td>
		            		<td class="subject"><c:if test="${result.type != 'Y'}">&nbsp;&nbsp;&nbsp;</c:if><c:out value="${result.title}"/></td>
		            		<td class="subject">
		            			<c:if test="${contentType == '07' and result.type != 'Y'}">
		            			default.htm
		            			</c:if>
		            			<c:if test="${contentType != 'X'}">
		            			<c:out value="${result.fileName}"/>
		            			</c:if>
		            		</td>
		            		<td><c:out value="${result.scormType}"/></td>
							<c:if test="${contentType != 'X'}">
		            		<td><c:out value="${result.score}"/></td>
							</c:if>
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
			<li><a href="#" class="pop_btn01" onclick="javascript:doManifestInsert()"><span>등록</span></a></li>
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

	function doManifestInsert(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		document.getElementById("loadingBars").style.display = "";
		var insertURL = "/adm/lcms/cts/LcmsOrganizationInsert.do";	// scorm
		if( frm.contentType.value == "X" ){
			insertURL = "/adm/lcms/xin/xinicsContentInsert.do";	//xinics
		}
		frm.target = "_self";
		frm.action = insertURL;
		frm.submit();
	}

    function doCancelFile(){
    	var frm = eval('document.<c:out value="${gsPopForm}"/>');
    	frm.target = "_self";
		frm.action = "/adm/lcms/cts/LcmsOrganizationCancel.do";
		frm.submit();
    }

	
	    /* ********************************************************
	    * manifest파일등록
	    ******************************************************** 
		doManifestInsert : function(){
			$("#loadingBars").show();
			var insertURL = "<c:out value="${gsDomainContext}"/>/adm/cts/LcmsOrganizationInsert.do"; //scorm : default
			if( $("#contentType").val() == "07" ){ //xinics
				insertURL = "<c:out value="${gsDomainContext}"/>/adm/xin/xinicsContentInsert.do";
			}
			$("#<c:out value="${gsPopForm}"/>").onSubmit(
				options = {
						url         : insertURL,
	                    target      : "hiddenFrame",
	                    callpost    : function() {
	 		                          },
	                    validation  : false
				}
			);
		},
	
		doCancelFile : function(){
			$("#<c:out value="${gsPopForm}"/>").onSubmit(
	            options = {
	                url         : "<c:out value="${gsDomainContext}"/>/adm/cts/LcmsOrganizationCancel.do",
	                target      : "hiddenFrame",
	                callpost    : function() {
			                          },
	                validation  : false
	            }
	        );
		}

		*/
</script>