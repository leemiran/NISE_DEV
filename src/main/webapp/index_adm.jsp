<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%String ipAddr = request.getRemoteAddr(); %>

<% if("127.0.0.1".equals(ipAddr) || "10.66.3.207".equals(ipAddr)){ %>
	<jsp:forward page="/adm/lgn/LoginUsr.do"/>
<%}else{ %>
<jsp:forward page="/adm/lgn/LoginUsr.do"/>
 	<script language="javascript">
 		//location.href="http://homepage.study.go.kr";
 	</script>   
<%} %>
