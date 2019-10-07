<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EDGE" />
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/userCommonHead.jsp" %>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>

<!-- nonActive -->
<!--로컬 -->
<%-- 
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/crownix-viewer.min.js"></script>
<link rel="stylesheet" type="text/css" href="<c:out value="${gsDomainContext}"/>/css/crownix-viewer.min.css"  />
--%>


<!--운영-->

<script type="text/javascript" src="http://27.101.217.158:8080/ReportingServer/html5/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="http://27.101.217.158:8080/ReportingServer/html5/js/crownix-viewer.min.js"></script>
<link rel="stylesheet" type="text/css" href="http://27.101.217.158:8080/ReportingServer/html5/css/crownix-viewer.min.css"  />

</head> 

<%
	String p_zz = (String)session.getAttribute("gadmin"); 
	System.out.println("############### " + p_zz);
	String zicIn = "http://10.175.134.37/images/user/receipt_bottom.jpg";
	String p_logo = "http://10.175.134.37/images/user/logo.gif";
	String fileName = "selectUserCashPrint.mrd";
	
	
	System.out.println("############### " + fileName);
%>

<c:set var="rf" value="<%=fileName %>"/>
<c:set var="rv"></c:set>
<c:set var="rp">
	[<c:out value="${p_subj}"  escapeXml="false"/>] 
	[<c:out value="${p_year}"  escapeXml="false"/>] 
	[<c:out value="${p_subjseq}"  escapeXml="false"/>] 
	[<c:out value="${p_userid}"  escapeXml="false"/>] 
	[<%=zicIn %>]	
</c:set>



<BODY leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" bgcolor="#FFFFFF" style="background:none">
	<p id="report_programes"></p>
	<%-- <ui:report title="RD" file="${rf}" rv="${rv}" rp="${rp}" /> --%>
	
	
	<div id="crownix-viewer" style="position:absolute;width:100%;height:100%"></div>
	<script>

		window.onload = function(){
			//var viewer = new m2soft.crownix.Viewer('http://localhost:8283/ReportingServer/service', 'crownix-viewer');
			var viewer = new m2soft.crownix.Viewer('http://27.101.217.158:8080/ReportingServer/service.jsp', 'crownix-viewer');
			//viewer.openFile('http://localhost/mrd/${rf}', '/rp [<c:out value="${p_subj}"  escapeXml="false"/>] [<c:out value="${p_year}"  escapeXml="false"/>] [<c:out value="${p_subjseq}"  escapeXml="false"/>] [<c:out value="${p_userid}"  escapeXml="false"/>] [<%=zicIn %>] [<%=p_logo %>] /rfn [http://localhost:8282/DataServer/rdagent.jsp]');
			viewer.openFile('http://10.175.134.40:8080/DataServer/mrd/${rf}', '/rf [http://10.175.134.40:8080/DataServer/rdagent.jsp] /rsn [newknise] /rp [<c:out value="${p_subj}"  escapeXml="false"/>] [<c:out value="${p_year}"  escapeXml="false"/>] [<c:out value="${p_subjseq}"  escapeXml="false"/>] [<c:out value="${p_userid}"  escapeXml="false"/>] [<%=zicIn %>] [<%=p_logo %>]	');
		};
	</script>
	
</BODY>


</html>