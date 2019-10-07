<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 

	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Expires" content="-1">
	<meta http-equiv="Expire-time" content="-1">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="imagetoolbar" content="no">
	<meta name="Keywords" lang="utf-8" content=":: 통합교원연수지원시스템 ::[AUNEYAU-5JUCPE8]">
	<meta name="Description" content=":: 통합교원연수지원시스템 ::[AUNEYAU-5JUCPE8]">
	<meta name="robots" content="noindex,nofollow" />
	<title>:: 통합교원연수지원시스템 ::[AUNEYAU-5JUCPE8]</title>
 
	<link rel="stylesheet" type="text/css" href="http://cyber.study.go.kr:80/css/import.css"  />
	<link rel="stylesheet" media="screen" type="text/css" href="http://cyber.study.go.kr:80/css/calendar/datepicker.css" />
 
    <script type="text/javascript" src="http://cyber.study.go.kr:80/script/jquery/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="http://cyber.study.go.kr:80/script/jquery/jquery-ui-1.8.2.custom.min.js"></script>
    <script type="text/javascript" src="http://cyber.study.go.kr:80/script/common/json2.js"></script>
    <script type="text/javascript" src="http://cyber.study.go.kr:80/script/jquery/jquery.dotimeout.js"></script>
    <script type="text/javascript" src="http://cyber.study.go.kr:80/script/common/map.js"></script>
	<script type="text/javascript" src="http://cyber.study.go.kr:80/script/common/message.js"></script>
	<script type="text/javascript" src="http://cyber.study.go.kr:80/script/jquery/ui/jquery.effects.drop.js"></script>
    <script type="text/javascript" src="http://cyber.study.go.kr:80/script/common/base_new.js"></script>
    <script type="text/javascript" src="http://cyber.study.go.kr:80/script/ziaan/shortcut.js"></script>
    <script type="text/javascript" src="http://cyber.study.go.kr:80/script/calendar/datepicker.js"></script>
    <script type="text/javascript" src="http://cyber.study.go.kr:80/script/common/commonUtil.js"></script>
	<script type="text/javascript" src="http://cyber.study.go.kr:80/script/common/common_function.js"></script>
 
    <script language="javascript">
    	var context = 'http://cyber.study.go.kr:80';

		function fn_main_headPageMove(mainMenuCode,detailMenuCode, url){
			document.eduupMenuForm.mainMenuCode.value=mainMenuCode;
			document.eduupMenuForm.detailMenuCode.value=detailMenuCode;
			document.eduupMenuForm.action = url;
	        document.eduupMenuForm.target = "_self";
	        document.eduupMenuForm.submit();
		}
 
		function fn_main_leftPageMove(mainMenuCode,detailMenuCode, url){
			document.eduupMenuForm.mainMenuCode.value = mainMenuCode;
			document.eduupMenuForm.detailMenuCode.value = detailMenuCode;
			document.eduupMenuForm.action = url;
      		document.eduupMenuForm.target = "_self";
		  	document.eduupMenuForm.submit();
		} 
 
		function fn_main_headGradeChange(obj){
			var optionObj = obj.options;
			var idx = optionObj.selectedIndex;
			//alert(optionObj[idx].text);
			document.eduupMenuForm.sauthGradeCodeValue.value = optionObj[idx].value;
			document.eduupMenuForm.sauthGradeCodeTxt.value = optionObj[idx].text;
			document.eduupMenuForm.mainMenuCode.value = "";
			document.eduupMenuForm.detailMenuCode.value = "";
 
			//
			document.eduupMenuForm.action = "/adm/lgn/actionMain.do";
      		document.eduupMenuForm.target = "_self";
		  	document.eduupMenuForm.submit();
		}
		
		function fn_LogOut(){
		    document.eduupMenuForm.action = "/adm/lgn/actionLogout.do";
		    document.eduupMenuForm.target = "_self";  
		    document.eduupMenuForm.submit();
		}
 
	</script>
</head>
 
<BODY leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" bgcolor="#FFFFFF">
<TEXTAREA ID="reportControl_" NAME="reportControl_" STYLE="display:none;" ROWS="0" COLS="0">
<OBJECT ID="MaDownloadRD" NAME="MaDownloadRD" CLASSID="clsid:3B780B78-73B9-49B8-9630-3E60EDE61C73" CODEBASE="http://125.60.59.81/ngsrd/RDServer/markany/MaDownloadRD.cab#version=1,0,2,0" WIDTH="" HEIGHT="" >
</OBJECT>
<OBJECT ID="Rdviewer" NAME="Rdviewer" CLASSID="clsid:ADB6D20D-80A1-4aa4-88AE-B2DC820DA076" CODEBASE="http://125.60.59.81/RDServer/rdviewer50.cab#version=5,0,0,258" WIDTH="100%" HEIGHT="100%" >
</OBJECT>
</TEXTAREA>
<SCRIPT language="javascript">
document.write( $("#reportControl_").val());
$("#reportControl_").val("");</SCRIPT>
<script language="javascript"> 
    Rdviewer.SetBackgroundColor(255,255,255);
    Rdviewer.AutoAdjust=0;
    Rdviewer.SetReportDialogInfo(1,'','',1,'','');
    Rdviewer.MAFileOpen2('http://cyber.study.go.kr:80/mrd/Portlet_Certification_TEST.mrd','/rfn [http://125.60.59.81/RDServer/rdagent.jsp] /rsn [java:comp/env/jdbc/EDUUP] /rpxpos [25]  /rmessageboxshow [2] /rv site[통합교원연수지원시스템] title[게시판] /rp [120532][20110829][1][위와 같이 이수하였음을 증명합니다.][백 종 면][][][][][][교육과학기술연수원장][http://portal.study.go.kr/attachfiles/asp/][1][org_link][이  수  증][N][thinklee] /rsave  /rmultipage /rwatchprn ','http://125.60.59.81/RDServer/rdagent.jsp');
</script>
 
	</BODY>
</HTML>
<SCRIPT LANGUAGE=JavaScript FOR=Rdviewer event="PrintFinished()"> 
 
	var seqCurm = "120532";
	seqCurm = seqCurm == "" ? "120532" : seqCurm;
	
	var map = new JMap();
	
	map.put( "userId", 			"thinklee" );
	map.put( "mainMenuCode", 	"" );
	map.put( "detailMenuCode", 	"" );
	map.put( "seqCurm", 		seqCurm );
	map.put( "sqYear", 			"" );
	map.put( "aspCode", 		"1340535" );
	
	var result = ajaxCall(
		{
			url		: 'http://cyber.study.go.kr:80/com/aja/com/insertPrintLog.do',
			async 	: false,
			type	: 'POST',
			ajaxId 	: "",
			method 	: "",
			data 	: map
		}
	);
	
 
</script>
