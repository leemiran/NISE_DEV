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
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/userCommonHead.jsp" %>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>
</head> 



<c:set var="rf" value="subjReceipMomeyList.mrd"/>
<c:set var="rv"></c:set>
<c:set var="rp">
	[<c:out value="${ses_search_gyear}"  escapeXml="false"/>] 
	[<c:out value="${ses_search_subj}"  escapeXml="false"/>] 
	[<c:out value="${ses_search_att}"  escapeXml="false"/>] 
	[<c:out value="${ses_search_subjinfo}"  escapeXml="false"/>] 
	[<c:out value="${ses_search_att_name}"  escapeXml="false"/>] 
</c:set>
<%
String year = request.getParameter("ses_search_gyear");
String att = request.getParameter("ses_search_att");
String sub = request.getParameter("ses_search_subj");
String subjnm = request.getParameter("ses_search_subjinfo");
String attnm = request.getParameter("ses_search_att_name");

System.out.println("ses_search_gyear["+year+"]");
System.out.println("ses_search_subj["+sub+"]");
System.out.println("ses_search_att["+att+"]");
System.out.println("ses_search_subjnm["+subjnm+"]");
System.out.println("ses_search_att_name["+attnm+"]");
%>

<BODY leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" bgcolor="#FFFFFF">
	<ui:report title="RD" file="${rf}" rv="${rv}" rp="${rp}" />
</BODY>
</html>