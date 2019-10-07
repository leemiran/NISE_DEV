<%@ page pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>
<%@ page import = "java.net.*" %>
<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core" %>
<%
SimpleDateFormat formatyyyyMMdd = new SimpleDateFormat("yyyyMMdd",Locale.KOREA);
SimpleDateFormat formatyyyy = new SimpleDateFormat("yyyy",Locale.KOREA);
Date currentTime = new Date();
String dcurrentTime = formatyyyyMMdd.format(currentTime);
String dcurrentYear = formatyyyy.format(currentTime);
String ServerName = "나의 강의실[" + InetAddress.getLocalHost().getHostName() + "]";  
%>
<c:set var="PCurrentDate" value="<%=dcurrentTime%>"/>
<c:set var="PCurrentYear" value="<%=dcurrentYear%>"/>    
<c:set var="gsServerTitle"  value="<%=ServerName%>"/>
<c:set var="gsMainForm" value="kniseForm"/>
<c:set var="gsMenuForm" value="kniseMenuForm"/>
<c:set var="gsDownLoadForm" value="kniseDownLoadForm"/>
<c:set var="gsPopForm" value="gsPopForm"/>
<c:set var="gsDomain" value="${fn:replace(pageContext.request.requestURL, pageContext.request.requestURI , '')}" />
<c:set var="gsDomainContext" value="${fn:replace(pageContext.request.requestURL, fn:replace(pageContext.request.requestURI, pageContext.request.contextPath, ''), '')}" />
<c:set var="gsSystemDiv" value="${pageContext.request.contextPath}" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="-1"/>
<meta http-equiv="Expire-time" content="-1"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta http-equiv="imagetoolbar" content="no"/>
<meta name="Keywords" lang="utf-8" content="<c:out value="${gsServerTitle}"/>"/>
<meta name="Description" content="<c:out value="${gsServerTitle}"/>"/>
<meta name="robots" content="noindex,nofollow" />
<!--title><c:out value="${gsServerTitle}"/></title-->
<title>
	<c:if test="${p_menu eq '02'}">
		<c:out value="학습시작(목록)  : " />
	</c:if>
	<c:if test="${p_menu eq '03'}">
		<c:out value="시험(목록)  : " />
	</c:if>
	<c:if test="${p_menu eq '04'}">
		<c:out value="설문(목록)  : " />
	</c:if>
	<c:if test="${p_menu eq '05'}">
		<c:out value="과제(목록) : " />
	</c:if>
	<c:out value="나의 강의실 : 국립특수교육원부설원격교육연수원" />
</title>
<link rel="stylesheet" type="text/css" href="<c:out value="${gsDomainContext}"/>/css/adm/popup.css"  />
<link rel="stylesheet" media="screen" type="text/css" href="<c:out value="${gsDomainContext}"/>/css/calendar/datepicker.css" />
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/cresys_lib.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/base_new.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>
<script language="javascript" type="text/javascript">
//<![CDATA[
var context = '<c:out value="${gsDomainContext}"/>';
//]]>
</script>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>