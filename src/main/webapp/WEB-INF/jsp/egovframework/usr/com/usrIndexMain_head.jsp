<%@ page language="java" 	 contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" 	 uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" 		 uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" 	 uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" 	 uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" 		 uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" 	 uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ajax" 	 uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"  xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonHead.jsp" %>
</head> 
<body>
<div id="u_skip"> <a href="#gnb"><span>메뉴 영역으로 바로가기</span></a>  <a href="#contents"><span>본문 영역으로 바로가기</span></a> </div>  
<!-- wrapper -->
<!-- <div id="wrapper"> -->
<c:if test="${not empty menu_main}">
	<!-- header -->
	<%-- <%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonTop.jsp" %> --%>
	<%@ include file = "/WEB-INF/jsp/egovframework/svt/main/commonTop.jsp" %>
	<!-- header -->	
    <!-- visual -->
    <div class="main_img2"><img src="/images/main/topimg2.jpg" alt="개성을 살리는 특수교육국립특수교육원이 함께 합니다 "></div>
    <div id="xmain">
		<div id="visual_${menu_main}">
			<div>
				<c:if test="${menu_main eq '1'}">
					<img src="/images/user/visual_tit01.gif" alt="연수안내" />
				</c:if>
				<c:if test="${menu_main eq '2'}">
					<img src="/images/user/visual_tit02.gif" alt="연수 신청" />
				</c:if>
				<c:if test="${menu_main eq '3'}">
					<img src="/images/user/visual_tit03.gif" alt="나의강의실" />
				</c:if>
				<c:if test="${menu_main eq '4'}">
					<img src="/images/user/visual_tit04.gif" alt="참여마당" />
				</c:if>
				<c:if test="${menu_main eq '5'}">
					<img src="/images/user/visual_tit05.gif" alt="연수행정, 연수행정에 관한 정보를 확인 할 수 있습니다." />
				</c:if>
				<c:if test="${menu_main eq '6'}">
					<img src="/images/user/visual_tit06.gif" alt="마이페이지, 나의 모든 정보를 한눈에 보실 수 있습니다." />
				</c:if>
				<c:if test="${menu_main eq '8'}">
					<img src="/images/user/visual_tit06.gif" alt="마이페이지, 나의 모든 정보를 한눈에 보실 수 있습니다." />
				</c:if>
			</div>
		</div>
		<!-- visual -->
	<!-- container -->
	<div id="container">
		<!-- left -->
		<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonLeft.jsp" %>
		<!-- // left -->
		<!-- contents -->
		<div class="contents" id="contents">
			<!-- title -->
			<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonTitle.jsp" %>
			<!-- // title -->
</c:if>
<c:if test="${empty menu_main}">
	<!-- container -->
	<div id="container">
    	<!-- contents -->
		<div class="contents" id="contents">
</c:if>
