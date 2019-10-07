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

<%
String p_isgraduated = request.getParameter("p_isgraduated");
String isgraduatedYn = "";
String isgraduatedYn1 = "";
System.out.println("#@!@   " + p_isgraduated);
if(p_isgraduated.equals("Y")){
	isgraduatedYn = "Y";
	isgraduatedYn1 = "";
}else if(p_isgraduated.equals("N")){
	isgraduatedYn = "";
	isgraduatedYn1 = "N";
}
%>

<c:set var="rf" value="subjectHabDetail.mrd"/>
<c:set var="rv"></c:set>
<c:set var="rp">
	[<c:out value="${p_year}"  escapeXml="false"/>] 
	[<c:out value="${p_subj}"  escapeXml="false"/>] 
	[<c:out value="${p_subjseq}"  escapeXml="false"/>] 
	[<%=isgraduatedYn%>] 
	[<%=isgraduatedYn1%>] 
</c:set>

[<c:out value="${p_year}"  escapeXml="false"/>] 
	[<c:out value="${p_subj}"  escapeXml="false"/>] 
	[<c:out value="${p_subjseq}"  escapeXml="false"/>] 
	[<%=isgraduatedYn%>] 
	[<%=isgraduatedYn1%>] 
<BODY leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" bgcolor="#FFFFFF">
	<ui:report title="RD" file="${rf}" rv="${rv}" rp="${rp}" />
</BODY>
</html>