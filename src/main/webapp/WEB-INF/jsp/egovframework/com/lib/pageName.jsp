<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "java.util.*" %>
<%@ page import="java.net.*" %>
<%
String footServerName = "원격교육연수원 학습센터 [" + InetAddress.getLocalHost().getHostName() + "]";
%>
<c:set var="footServerTitle"  value="<%=footServerName%>"/>

<c:if test="${sessionScope.userid eq 'admin'}">         
  <br/>
  <br/>
  <br/>
 		page path : <%= request.getServletPath()%>
 		<c:out value="${footServerTitle}"/>
  <br/>
</c:if>