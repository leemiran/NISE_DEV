<%@ page pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>

<c:set var="gsServerTitle" value="원격교육연수원 학습센터"/>
<c:set var="gsMainForm" value="snuiForm"/>
<c:set var="gsMenuForm" value="snuiMenuForm"/>
<c:set var="gsExernalForm" value="snuiExternalForm"/>
<c:set var="gsDownLoadForm" value="snuiDownLoadForm"/>
<c:set var="gsPopForm" value="gsPopForm"/>
<c:set var="gsStudyroomForm" value="gsStudyroomForm"/>

<c:set var="gsWebRootFrame" value="snui_main_fr"/>
<c:set var="gsAdmTopFrame" value="snui_adm_top"/>
<c:set var="gsAdmBodyFrame" value="snui_adm_body"/>

<c:set var="gsDomainContext" value="${fn:replace(pageContext.request.requestURL, fn:replace(pageContext.request.requestURI, pageContext.request.contextPath, ''), '')}" />
<c:set var="gsSystemDiv" value="${pageContext.request.contextPath}" />
<c:set var="subProgram" value="${fn:replace(pageContext.request.requestURL, gsDomainContext , '')}" />
<%@page import="egovframework.com.cmm.service.Globals"%>
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Expires" content="-1">
	<meta http-equiv="Cache-Control" content="no-store">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="imagetoolbar" content="no">
	<meta name="Keywords" lang="utf-8" content="<c:out value="${gsServerTitle}"/>">
	<meta name="Description" content="<c:out value="${gsServerTitle}"/>">
	<meta name="robots" content="noindex,nofollow" />
	<title id="firstTitle"><c:out value="${gsServerTitle}"/></title>
	<link rel="STYLESHEET" type="text/css" href="<c:out value="${gsDomainContext}"/>/css/study/learn.css">
	<link rel="STYLESHEET" type="text/css" href="<c:out value="${gsDomainContext}"/>/css/study/class.css">
	<script type="text/javascript">
		<!--	
		var context = '<c:out value="${gsDomainContext}"/>';
		var gsPopForm = '<c:out value="${gsPopForm}"/>';
		var gsMainForm = '<c:out value="${gsMainForm}"/>';
		var tempPath = "";
		if( '<c:out value="${output.studyType}"/>' == 'OLD' ){
			tempPath = "";
		}
		function goLogOut() {
			doFormSubmit( '<c:out value="${gsMainForm}"/>', false, { action: "<c:out value="${gsDomainContext}"/>/adm/logout.do"});
		}
		var TopExecutor = {
			doIndex : function() {
				doFormSubmit( nForm, false, { action: '/main.do', target: '_top'});
			}	
		}
		-->
	</script>

	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-ui-1.8.2.custom.min.js"></script>
    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/json2.js"></script>
    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery.dotimeout.js"></script>
    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery.form.js"></script>
    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/map.js"></script>
	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/message.js"></script>
	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/ui/jquery.effects.drop.js"></script>
    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/base_new.js"></script>
    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/ziaan/shortcut.js"></script>
    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/calendar/datepicker.js"></script>
    <script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/commonUtil.js"></script>
	<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/common_function.js"></script>
</head>