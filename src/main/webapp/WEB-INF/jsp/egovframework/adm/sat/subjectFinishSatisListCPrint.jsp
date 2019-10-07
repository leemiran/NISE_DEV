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
System.out.println("============ 전체설문결과분석 출력 ===============");
Map<String, Object> view = (Map<String, Object>)request.getAttribute("view");
String subjnm 				= request.getParameter("print_subjnm")+"";
String edustart 			= request.getParameter("print_edustart")+"";
String edutimes 			= request.getParameter("print_edutimes")+"";
String studentTotalCnt 	= request.getParameter("print_studentTotalCnt")+"";
String isgraduatedY 		= request.getParameter("print_isgraduatedY")+"";
String isgraduatedN 		= request.getParameter("print_isgraduatedN")+"";

String replycount 			= request.getParameter("print_replycount")+"";
String replyrate 			= request.getParameter("print_replyrate")+"";

String ses_search_gyear 		= request.getParameter("ses_search_gyear")+"";
String ses_search_subj 			= request.getParameter("ses_search_subj")+"";
String ses_search_subjseq 	= request.getParameter("ses_search_subjseq")+"";

%>
<c:set var="rf" value="subjectFinishSatisListCPrint.mrd"/>
<c:set var="rv"></c:set>
<c:set var="rp">
	[<c:out value="<%=subjnm%>"  			escapeXml="false"/>] 
	[<c:out value="<%=edustart%>"  			escapeXml="false"/>] 
	[<c:out value="<%=edutimes%>"  			escapeXml="false"/>] 
	[<c:out value="<%=studentTotalCnt%>" 	escapeXml="false"/>] 
	[<c:out value="<%=isgraduatedY%>"  	escapeXml="false"/>] 
	[<c:out value="<%=isgraduatedN%>"  	escapeXml="false"/>] 
	[<c:out value="<%=replycount%>"  		escapeXml="false"/>] 
	[<c:out value="<%=replyrate%>"  			escapeXml="false"/>] 
	[<c:out value="${ses_search_gyear}" 	escapeXml="false"/>] 
	[<c:out value="${ses_search_subj}"  		escapeXml="false"/>] 
	[<c:out value="${ses_search_subjseq}"	escapeXml="false"/>] 
</c:set>
<%
System.out.println("subjnm["+ subjnm+"]");
System.out.println("edustart["+edustart+"]");
System.out.println("edutimes["+edutimes+"]");
System.out.println("studentTotalCnt["+studentTotalCnt+"]");
System.out.println("isgraduatedY["+isgraduatedY+"]");
System.out.println("isgraduatedN["+isgraduatedN+"]");
System.out.println("view["+view+"]");

System.out.println("ses_search_gyear["+ses_search_gyear+"]");
System.out.println("ses_search_subj["+ses_search_subj+"]");
System.out.println("ses_search_subjseq["+ses_search_subjseq+"]");

%>
<BODY leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" bgcolor="#FFFFFF">
	<ui:report title="RD" file="${rf}" rv="${rv}" rp="${rp}" />
</BODY>
</html>