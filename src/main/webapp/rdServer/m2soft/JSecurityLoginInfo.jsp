<%@ page import="java.util.*,java.io.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.rddbagent.role.*,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.log.m.*" %><% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>
<%
	initArg(request,out);
%>
<html>
<head>
<style>
<%@ include file="icis.css" %>
</style>
</head>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">

User, Host Name (IP), Log in time,Report Document and Others

<%
  ArrayList list = RoleFile.getLogList();
  for(int i=0; i< list.size(); i++)
     out.println(list.get(i).toString());
%>
</body>
</html>

