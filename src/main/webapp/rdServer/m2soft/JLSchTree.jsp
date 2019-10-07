<%@ page import="java.util.*,java.sql.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*,m2soft.rdsystem.server.core.rddbagent.util.*" %> 
<%@ include file="properties.h"%>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../m2soft/JLSchTree.java" %>
<%@ include file="../control/lib/JLJsp.jsp" %>
<%@ include file="../control/lib/JLRuntimeClass.java" %>
<%@ include file="../control/lib/JLHttp.java" %>
<%@ include file="../control/lib/JLTreeCtrl.java" %>
<%@ include file="../control/lib/JLContextMenu.java" %>
<body bgcolor="#ffffff" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<%
initArg(request,out);
JLSchTree ctrl = new JLSchTree(out);
ctrl.setParam(m_param);
int nCmd = getIntValue(m_param,"_cmd");
if (nCmd == 0) // getSegment
{ 
	ctrl.printData(m_param);
} 
if (nCmd == 1) // insertRow
{ 
	ctrl.insertNode();
} 
if (nCmd == 2) // updateRow
{ 
	ctrl.updateNode();
} 
if (nCmd == 3) // deleteRow
{ 
	ctrl.deleteNode();
} 
%>
</body>