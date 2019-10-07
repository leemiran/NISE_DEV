<%@ page language="java" import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File " %>
<%@ page contentType="text/html; charset=EUC-KR" %>
<%@ include file="/rdServer/m2soft/JLCurUserTree.java" %>
<%@ include file="/rdServer/control/lib/JLJsp.jsp" %>
<%@ include file="/rdServer/control/lib/JLObj.java" %>
<%@ include file="/rdServer/control/lib/JLRuntimeClass.java" %>
<%@ include file="/rdServer/control/lib/JLHttp.java" %>
<%@ include file="/rdServer/control/lib/JLTreeCtrl.java" %>
<%@ include file="/rdServer/control/lib/JLContextMenu.java" %>
<body bgcolor="#ffffff" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<%
initArg(request,out);
JLCurUserTree ctrl = new JLCurUserTree(out);
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