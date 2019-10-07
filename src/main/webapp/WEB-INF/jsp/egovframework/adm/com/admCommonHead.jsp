<%@ page pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>
<%@ page import = "java.net.*" %>
<%
    SimpleDateFormat formatyyyyMMdd = new SimpleDateFormat("yyyyMMdd",Locale.KOREA);
	SimpleDateFormat formatyyyy = new SimpleDateFormat("yyyy",Locale.KOREA);
    Date currentTime = new Date();
    String dcurrentTime = formatyyyyMMdd.format(currentTime);
    
    String dcurrentYear = formatyyyy.format(currentTime);
    
    String ServerName = ":: 원격교육연수원 학습센터 ::[" + InetAddress.getLocalHost().getHostName() + "]";
    
%>
<%

String userAgent =  request.getHeader("User-Agent");
boolean isMSIE7 = (userAgent != null && userAgent.indexOf("MSIE 7") > -1);
pageContext.setAttribute("isMSIE7", isMSIE7);

%>
<c:set var="PCurrentDate" value="<%=dcurrentTime%>"/>
<c:set var="PCurrentYear" value="<%=dcurrentYear%>"/>
    
<c:set var="gsServerTitle"  value="<%=ServerName%>"/>
<c:set var="gsMainForm" value="kniseForm"/>
<c:set var="gsMenuForm" value="kniseMenuForm"/>
<c:set var="gsDownLoadForm" value="kniseDownLoadForm"/>
<c:set var="gsPopForm" value="gsPopForm"/>

<c:set var="gsWebRootFrame" value="knise_main_fr"/>
<c:set var="gsAdmTopFrame" value="knise_adm_top"/>
<c:set var="gsAdmBodyFrame" value="knise_adm_body"/>

<c:set var="gsDomain" value="${fn:replace(pageContext.request.requestURL, pageContext.request.requestURI , '')}" />
<c:set var="gsDomainContext" value="${fn:replace(pageContext.request.requestURL, fn:replace(pageContext.request.requestURI, pageContext.request.contextPath, ''), '')}" />
<c:set var="gsSystemDiv" value="${pageContext.request.contextPath}" />
<c:set var="cacheVer" value="20170213"/>

<c:set property="char" var="PstateCode" value="${PStateCode}"></c:set>

	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Expires" content="-1">
	<meta http-equiv="Expire-time" content="-1">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="imagetoolbar" content="no">
	<!-- <meta http-equiv="X-UA-Compatible" content="IE=edge"/> -->
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
	<meta name="Keywords" lang="utf-8" content="<c:out value="${gsServerTitle}"/>">
	<meta name="Description" content="<c:out value="${gsServerTitle}"/>">
	<meta name="robots" content="noindex,nofollow" />
	<title><c:out value="${gsServerTitle}"/></title>


	<link rel="stylesheet" type="text/css" href="<c:out value="${gsDomainContext}"/>/css/adm/import.css?ver=${cacheVer}"  />
	<link rel="StyleSheet" href="<c:out value="${gsDomainContext}"/>/css/dtree/dtree.css?ver=${cacheVer}" type="text/css" />
	
	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/cresys_lib.js?ver=${cacheVer}"></script>
	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/base_new.js?ver=${cacheVer}"></script>
	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/map_util.js?ver=${cacheVer}"></script>
<!--	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/calendar/datePicker.js"></script>-->
	
    <script language="javascript">
    	var context = '<c:out value="${gsDomainContext}"/>';

	</script>
