<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%//@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="ui"     uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/common/admCommonHead.jsp" %>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/egovframework/cmm/fms/EgovMultiFileCom.js" ></script>

<link href="/css/pop.css" rel="stylesheet" type="text/css" />
<base target="_self">
<style>
body {overflow-x:hidden; overflow-y:auto}
</style>
<script type="text/javascript">
	window.name = "comFileUploadWindowIframe";

	window.onload = function () {
		//부모 창에 atchFileId 넣기.. 파일의 아이디이므로 항상 부모창에 존재해야 한다.
	 
		<c:if test="${type eq 'insert'}">
		 var maxFileNum = document.frm.posblAtchFileNumber.value;
		
		if(maxFileNum==null || maxFileNum==""){
		  maxFileNum = 3;
		}     
		var multi_selector = new MultiSelector( document.getElementById( 'egovComFileList' ), maxFileNum );
		    multi_selector.addElement( document.getElementById( 'egovComFileUploader' ) );
		</c:if>
		<c:if test="${type eq 'update'}">
	   
	    var existFileNum = document.frm.fileListCnt.value;	    
		var maxFileNum = document.frm.posblAtchFileNumber.value;

		if (existFileNum=="undefined" || existFileNum ==null) {
			existFileNum = 0;
		}
		if (maxFileNum=="undefined" || maxFileNum ==null) {
			maxFileNum = 0;
		}		
		var uploadableFileNum = maxFileNum - existFileNum;
		if (uploadableFileNum<0) {
			uploadableFileNum = 0;
		}	
		if (uploadableFileNum != 0) {
			//fn_egov_check_file('Y');
			var multi_selector = new MultiSelector( document.getElementById( 'egovComFileList' ), uploadableFileNum );
			multi_selector.addElement( document.getElementById( 'egovComFileUploader' ) );
			
			document.getElementById('file_upload_posbl').style.display = "block";
			document.getElementById('file_upload_imposbl').style.display = "none";	
			document.getElementById('file_upload_imposbl2').style.display = "block";
		} else {
			//fn_egov_check_file('N');
			document.getElementById('egovComFileUploader').disabled = "disabled";
			document.getElementById('file_upload_posbl').style.display = "block";
			document.getElementById('file_upload_imposbl').style.display = "block";
			document.getElementById('file_upload_imposbl2').style.display = "none";	
		}
		</c:if>
	   
	}

	function submitFile(isCheckFile) {
        if(isCheckFile != "" ){
           document.frm.isCheckFile.value="isCheckFile";
		   EtcExecutor.doPageList();
        }else{
           document.frm.isCheckFile.value="";
        }
		document.frm.submit();


	}

	

</script>	
  
  
<script type="text/javascript">
<!--
<c:if test="${param.isCheckFile eq 'isCheckFile'}">
  parent.fileReturn();
</c:if>

-->
</script>

    <style type="text/css">
       
        .dv_progress{
            border:1px solid;height:10px;width:302px;text-align:left;
        }
        .dv_progress_bar{
            background:orange;height:10px;width:0px;
        }
    </style>

</head> 

<body>
<div id="contents2">   
 	    
<!-- 
<form name="frm" id="frm" method="post" enctype="multipart/form-data" onsubmit="return false;" target="comFileUploadWindowIframe" action="<c:url value='/com/upl/comFileUploadActionCom.do'/>">
 -->  	    
<form name="frm" id="frm" method="post" enctype="multipart/form-data"  action="<c:url value='/com/upl/comFileUploadActionCom.do'/>">


<!--파일의 앞에 붙는 첨자아이디. / 파일업로드 기본경로  + 하위 폴더명-->
<input type="hidden" id="keyFileId" name="keyFileId" value="<c:out value="${keyFileId}"/>"/>   

<!--파일의 첨부개수를 지정한다.-->
<input type="hidden" name="posblAtchFileNumber" id="posblAtchFileNumber" value="<c:out value="${posblAtchFileNumber}"/>" />
<input type="hidden" name="isCheckFile" id="isCheckFile" value="" />
 
<!-- 삭제시 리턴할 url.-->
<input type="hidden" name="returnUrl" value="<c:url value='/com/upl/comFileUploadIframeCom.do'/>"/>
<input type="hidden" name="type" value="<c:out value="${type}"/>"/>
 

	<table cellspacing="0" cellpadding="0" border="0" align="left">  
		<tr>
			<td>
				<input name="file" id="egovComFileUploader" type="file" value="txt" size="30" class="infut_text2" title="첨부파일"/> 
	    	</td>
		</tr>
		<tr>
     		<td>
     			<div id="file_upload_imposbl"  style="display:none;"><spring:message code="common.imposbl.fileupload" /></div>
     			<div id="file_upload_imposbl2"  style="display:none;">&nbsp;</div>
     		</td>
    	</tr>
		<tr>
			<td>
				<div id="egovComFileList"></div>
			</td>
		</tr>  

	</table>	
 <br class="clear"/>	
    <table cellspacing="0" cellpadding="0" border="0" align="left">
    	<tr>
			<td>
			<div id="m"></div>
			<span id="status"></span> 
			</td>
		</tr>
    </table>  

 <br class="clear"/>

<div id='file_upload_posbl' style="display:none;" >
 <c:import url="/cmm/fms/selectFileInfsForComUpdate.do" >
     <c:param name="param_atchFileId" value="${atchFileId}" />
 </c:import>
</div>  
 
</form> 
<!-- 페이지 정보 -->
<%@ include file = "/WEB-INF/jsp/egovframework/com/lib/pageName.jsp" %>
<!-- 페이지 정보 -->
</div>

   
</body>
</html>

<script type="text/javascript"> 

/* [ progress bar class ]
PROGRESS_BAR -> build -> start -> progressing -> complete */
var PROGRESS_BAR = function(_id){
 var PROGRESS,BAR,PROGRESS_SIZE;
 var SELF=this;
 this.build=function(content){
     PROGRESS=$('<div>').addClass('dv_progress').attr('id',_id).hide();
     BAR=$('<div>').addClass('dv_progress_bar');
     PROGRESS.append(BAR);
     content.append(PROGRESS);
 }
 this.start=function(per,msec){
     PROGRESS.show();
     PROGRESS_SIZE=PROGRESS.innerWidth();
     this.progressing(per,msec||1000);
 }
 this.show=function(){ PROGRESS.show(); }
 this.stop=function(){ BAR.stop(); }
 this.reset=function(){ BAR.width(0); }
 this.progressing=function(per,msec){
     var msec=msec||1000;
     var N_PROGRESS=BAR.outerWidth();
     if(N_PROGRESS==PROGRESS_SIZE){ SELF.complete();return; }
     if(per=='COMPLETE')
         BAR.stop(),POINT=PROGRESS_SIZE-N_PROGRESS;
     else POINT=Math.round((PROGRESS_SIZE-N_PROGRESS)*(per/100));
     BAR.animate({width:'+='+POINT},msec,function(){
         SELF.progressing(per,msec);
     });
 }
 this.complete=function(){
     return;
 }
}

var EtcExecutor = {
        doPageList : function() {
		    PROGRESS1 = new PROGRESS_BAR('layer_progress');
		    PROGRESS1.build($('#m'));
		    PROGRESS1.show();
	        PROGRESS1.start(60,5000);
	        $("#status").text('전송중');

        }
}
</script>



