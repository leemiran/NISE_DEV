<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%//@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/common/admCommonHead.jsp" %>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/egovframework/cmm/fms/EgovMultiFile.js" ></script>

<link href="/css/pop.css" rel="stylesheet" type="text/css" />
<base target="_self">
<style>
body {overflow-x:hidden; overflow-y:auto}
</style>
<script type="text/javascript">
	window.name = "comFileUploadWindowIframe";

	window.onload = function () {

		//부모 창에 atchFileId 넣기.. 파일의 아이디이므로 항상 부모창에 존재해야 한다.
		parent.document.<c:out value="${gsMainForm}"/>.atchFileId.value = '<c:out value="${atchFileId}"/>';

		
		<c:if test="${empty atchFileId}">

		//등록모드
		var maxFileNum = document.frm.posblAtchFileNumber.value;
		if(maxFileNum==null || maxFileNum==""){
		  maxFileNum = 3;
		 }     
		var multi_selector = new MultiSelector( document.getElementById( 'egovComFileList' ), maxFileNum );
		multi_selector.addElement( document.getElementById( 'egovComFileUploader' ) );
		</c:if>

	   <c:if test="${not empty atchFileId}">
	   
	   //수정모드
		var existFileNum = document.frm.fileListCnt.value; // 이 값은 File List를 조회하는 부분에 담겨온다.
		var maxFileNum = document.frm.posblAtchFileNumber.value; 
		// 각 비즈니스 로직에서는 해당하는 폼 값에 첨부가능한 최대파일 숫자를 세팅해둬야 함
		var uploadableFileNum = maxFileNum - existFileNum; // 최대등록가능한 파일숫자에서 기존에 등록된 숫자를 뺀다.
		if(uploadableFileNum<0) {
		  uploadableFileNum = 0;
		}
		if(uploadableFileNum != 0){
		  fn_egov_check_file('Y');
		  var multi_selector = new MultiSelector( document.getElementById( 'egovComFileList' ), uploadableFileNum );
		  multi_selector.addElement( document.getElementById( 'egovComFileUploader' ) );
		}else{
		  fn_egov_check_file('N');
		}  
		</c:if>
	   
	   
	}

	function submitFile() {
		document.frm.submit();
	}

 
</script>	
  
  
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">
alert("${resultMsg}");
</c:if>
-->
</script>
</head> 

<body>
<div id="wrap">
    <div id="contents">
 	    
 	    
<form name="frm" method="post" enctype="multipart/form-data" onsubmit="return false;" target="comFileUploadWindowIframe" action="<c:url value='/com/upl/comFileUploadAction.do'/>">
<!--###################### 공통 변수 선언 Start ######################-->


<!--파일의 앞에 붙는 첨자아이디. / 파일업로드 기본경로  + 하위 폴더명-->
<input type="hidden" id="keyFileId" name="keyFileId" value="<c:out value="${keyFileId}"/>"/>

<!--파일의 첨부개수를 지정한다.-->
<input type="hidden" name="posblAtchFileNumber" id="posblAtchFileNumber" 
value="<c:if test="${not empty posblAtchFileNumber}"><c:out value="${posblAtchFileNumber}"/></c:if><c:if test="${empty posblAtchFileNumber}">1</c:if>" />
 
<!-- 삭제시 리턴할 url.-->
<input type="hidden" name="returnUrl" value="<c:url value='/com/upl/comFileUploadIframe.do'/>"/>
 
<!--###################### 공통 변수 선언 End ######################-->

<div>
 <c:import url="/cmm/fms/selectFileInfs.do" >
     <c:param name="param_atchFileId" value="${atchFileId}" />
 </c:import>
</div>   

<br/>

 <div id="file_upload_posbl"
<c:if test="${empty atchFileId}">
 style="display:block;"
</c:if>
<c:if test="${not empty atchFileId}">
 style="display:none;"
</c:if>
>

<table cellspacing="0" cellpadding="0" border="0" align="left">
   <tr>
    <td>
    <span class="text_olive">파일업로드</span> 
    <input name="file" id="egovComFileUploader" type="file" class="infut_text2" title="첨부파일"/> 
    <img src="/images/button/btn_save.gif" border="0" style="cursor:hand" onclick="submitFile()" />
    </td>
   </tr>
   <tr>
    <td>
     <div id="egovComFileList"></div>
    </td>
   </tr>
</table>	  
 </div>
 <div id="file_upload_imposbl"  style="display:none;" >
   <table width="680px" cellspacing="0" cellpadding="0" border="0" align="center" class="UseTable">
    <tr>
     <td><spring:message code="common.imposbl.fileupload" /></td>
    </tr>
   </table>				
 </div>
   
</form> 

</div>



</div>
<p></p>
<p></p>

<!-- 페이지 정보 -->
<%@ include file = "/WEB-INF/jsp/egovframework/com/lib/pageName.jsp" %>
<!-- 페이지 정보 -->
   
</body>
</html>
