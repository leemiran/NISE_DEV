<%@ page pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.net.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
SimpleDateFormat formatyyyyMMdd = new SimpleDateFormat("yyyyMMdd",Locale.KOREA);
SimpleDateFormat formatyyyy = new SimpleDateFormat("yyyy",Locale.KOREA);
Date currentTime = new Date();
String dcurrentTime = formatyyyyMMdd.format(currentTime);    
String dcurrentYear = formatyyyy.format(currentTime);    
String ServerName = "원격교육연수원 학습센터 [" + InetAddress.getLocalHost().getHostName() + "]";    

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
<c:set property="char" var="PstateCode" value="${PStateCode}"></c:set>
<c:set var="cacheVer" value="20180416"/>

<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="-1"/>
<meta http-equiv="Expire-time" content="-1"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta http-equiv="imagetoolbar" content="no"/>
<meta name="Keywords" lang="utf-8" content="<c:out value="${gsServerTitle}"/>" />
<meta name="Description" content="<c:out value="${gsServerTitle}"/>" />
<meta name="robots" content="noindex,nofollow" />

<!--title><c:out value="${gsServerTitle}"/></title-->
<title>
	<c:if test="${empty menu_sub_title}">
	<c:out value="국립특수교육원부설원격교육연수원"/>
	</c:if> 
	<c:if test="${!empty menu_sub_title}">
		<c:out value="${menu_sub_title}"  escapeXml="true" /> : 
	</c:if> 
	<c:if test="${!empty menu_tab_title}">
		<c:out value="${menu_tab_title}"  escapeXml="true" />
	</c:if> 
</title>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:out value="${gsDomainContext}"/>/css/user/main.css?ver=${cacheVer}"  />
<link rel="stylesheet" type="text/css" media="screen" href="<c:out value="${gsDomainContext}"/>/css/user/jqbanner.css?ver=${cacheVer}" />
<!-- main banner -->
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jqbanner.js?ver=${cacheVer}"></script>

<!-- 추가 -->
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/user/topmenu.js?ver=${cacheVer}"></script>
<!-- 추가 -->

<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1-9.1.js?ver=${cacheVer}"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-ui-1.10.3.custom.min.js?ver=${cacheVer}"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/json2.js?ver=${cacheVer}"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery.dotimeout.js?ver=${cacheVer}"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/map.js?ver=${cacheVer}"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/message.js?ver=${cacheVer}"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/ui/jquery.effects.drop.min.js?ver=${cacheVer}"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery.bpopup.min.js?ver=${cacheVer}"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/base_new.js?ver=${cacheVer}"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/user/common.js?ver=${cacheVer}"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/cresys_lib.js?ver=${cacheVer}"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/learning/studyPopup.js?ver=${cacheVer}"></script>

<!-- 추가 -->
<script type="text/javascript" charset="utf-8" src="<c:out value="${gsDomainContext}"/>/js/user/scroll.js?ver=${cacheVer}"></script>
<!-- 추가 -->
<% if(!request.getServletPath().equalsIgnoreCase("/WEB-INF/jsp/egovframework/svt/main/main.jsp")) { //메인페이지 일경우만 css를 바꾼다.%>
<link rel="stylesheet" type="text/css" href="<c:out value="${gsDomainContext}"/>/css/user/import.css?ver=${cacheVer}"  />
<% } %>

<script language="javascript" type="text/javascript">
var context = '<c:out value="${gsDomainContext}"/>';
//성적보기
function fn_statusPopup(subj, year, subjseq, userid){
	var url ="/com/pop/courseScoreListPopup.do"
		+ "?p_subj=" + subj
		+ "&p_year=" + year
		+ "&p_subjseq=" + subjseq
		+ "&p_userid=" + userid;

	/*
	$('.all_knise_modal_popup').bPopup({
		content:'iframe', //'ajax', 'iframe' or 'image'
       	contentContainer:'.all_knise_modal_popup_content',
        iframeAttr : 'scrolling="auto" width="100%" height="600" frameborder="0"',
        loadUrl: url //Uses jQuery.load()
	});
	*/
	 
	window.open(url,"searchSubjSeqInfoPopupWindowPop","width=820,height=650,scrollbars=yes");

	/*
	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	frm.p_subj.value = subj;
	frm.p_year.value = year;
	frm.p_subjseq.value = subjseq;
	frm.p_userid.value = userid;
	window.open('', 'eduListPop', 'width=820,height=740,scrollbars=yes');
	frm.action = "/com/pop/courseScoreListPopup.do";
	frm.target = "eduListPop";
	frm.submit();
	*/
}

var c_url   = this.location+"";
/*
//if (c_url.match("localhost")) {		
if (c_url.match("http://iedu.nise.go.kr")) {
	location.href="/temp_index/temp_index.jsp";
}
*/
</script>