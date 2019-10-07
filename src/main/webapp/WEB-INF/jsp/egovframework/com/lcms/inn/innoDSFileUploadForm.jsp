<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg and uploadType eq 'FILE'}">
	alert("${resultMsg}");
	opener.doPageReload();
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper" style="width:560px;">
	<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
	<div class="popIn">
		<p id="title"><c:out value="${innodsPageTitle}"/></p>
		<!-- contents -->
		<div style="padding:10px;" valign="top">
			<iframe id="fileFrm" src="/innorix/InnoDS/index.html" width="520" height="302" scrolling="no" frameborder="0" marginwidth="0" marginheight="0"></iframe>
			<input type="hidden" id="subj" 			name="subj" 			value="${subj}">
			<input type="hidden" id="org" 			name="org" 				value="${innodsSubDir}">
			<input type="hidden" id="contentType" 	name="contentType" 		value="${contentType}">
			<input type="hidden" id="uploadType" 	name="uploadType" 		value="${uploadType}">
			<input type="hidden" id="path" 			name="path" 			value="${path}">
<!-- 			EgovProperties.getProperty("Globals.contentFileStore")+commandMap.get("userid")+"/"+commandMap.get("subj"); -->
<%--     			<input type="hidden" id="uploadType" 	name="uploadType" 		value="${uploadType}"/> --%>
<%--     			<input type="hidden" id="SubDir" 		name="SubDir" 			value="${innodsSubDir}"/> --%>
<%--     			<input type="hidden" id="RootDir" 		name="RootDir" 			value="${innodsRootDir}"/> --%>
<%--     			<input type="hidden" id="ViewType" 		name="ViewType" 		value="${innodsViewType}"/> --%>
<%--     			<input type="hidden" id="compression" 	name="compression" 		value="${innodsCompression}"/> --%>
<%--     			<input type="hidden" id="LimitExt" 		name="LimitExt" 		value="${innodsLimitExt}"/> --%>
<%--     			<input type="hidden" id="ExtPermission" name="ExtPermission" 	value="${innodsExtPermission}"/> --%>
<%--     			<input type="hidden" id="innodsPageTitle" name="innodsPageTitle" 	value="${innodsPageTitle}"/> --%>
<%--     			<input type="hidden" id="contentType" 	name="contentType" 		value="${contentType}"/> --%>
        </div>
		<!-- // contents -->
<!--  			<p class="right"> -->
<%-- 				<c:if test="${innodsViewType == 0}"> --%>
<!-- 				<a href="#none" class="btn02" id="orderType" onclick="fileFrm.InnoDS.OpenFileDialog();"><span>파일추가</span></a> -->
<%-- 				</c:if> --%>
<%-- 				<c:if test="${innodsViewType != 0}"> --%>
<!-- 				<a href="#none" class="btn02" id="orderType" onclick="fileFrm.InnoDS.OpenFolderDialog();"><span>폴더추가</span></a> -->
<%-- 				</c:if> --%>
<!-- 				<a href="#none" class="btn02" id="orderType" onclick="fileFrm.upload();"><span>전송하기</span></a>             -->
<!--             </p> -->
		<!-- button -->
		<ul class="btnCen">			
			<li><a href="#" onClick="javascript:window.close();" class="btn01"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
	</form>
</div>
<!-- // popup wrapper -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

	function getParam(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		var arr = new Array();
		arr[0] = frm.subj.value;
		arr[1] = frm.path.value;
		return arr;
	}



	function upload(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.uploadType.value == "CONTENT" ){
			frm.action = "/adm/lcms/ims/imsManifestList.do";
		}
		else if( frm.uploadType.value == "FILE" ){
			frm.action = "/adm/lcms/cts/uploadContentFile.do";
		}else if( frm.uploadType.value == "NONSCORM" ){
			frm.action = "/adm/lcms/nct/excelNonScormList.do";
		}else if( frm.uploadType.value == "NORMAL" ){
			//frm.action = "/adm/lcms/nct/excelNonScormList.do";
		}else if( frm.uploadType.value == "MOBILE" ){
			//frm.action = "/adm/lcms/nct/excelNonScormList.do";
		}
		frm.target = "_self";
		frm.submit();
		
	}
	
	
	
	/**
	 	function getParam(){

		var arr = new Array();
		arr[0] = $("#SubDir").val();
		arr[1] = $("#RootDir").val();
		arr[2] = $("#compression").val();
		arr[3] = $("#LimitExt").val();
		arr[4] = $("#ExtPermission").val();
		arr[5] = $("#ViewType").val();
		arr[6] = $("#contentType").val();
		return arr;
	}

	function uploadFail(){
		alert("업로드에 실패하였습니다.");
		window.close();
	}

	function uploadCompleted(){
		var uploadType = $("#uploadType").val();
		var resultURL = "";
		if( uploadType == "CONTENT" ){ //컨텐츠 업로드
			resultURL = "<c:out value="${gsDomainContext}"/>/adm/ims/imsManifestList.do";
		}else if( uploadType == "FILE" ){ // 파일등록
			alert("파일을 등록 하였습니다.");
			opener.CtlExecutor.doPageReload();
			window.close();
		}else if( uploadType == "FOLDER" ){ // 파일등록
			alert("폴더를 등록 하였습니다.");
			opener.CtlExecutor.doPageReload();
			window.close();
		}else if( uploadType == "NONSCORM" ){
			resultURL = "<c:out value="${gsDomainContext}"/>/adm/nct/excelNonScormList.do";
		}

		$("#<c:out value="${gsPopForm}"/>").onSubmit(
			options = {
             url         : resultURL,
             method      : "pageList",
             validation  : false
         });
	}
	*/
</script>