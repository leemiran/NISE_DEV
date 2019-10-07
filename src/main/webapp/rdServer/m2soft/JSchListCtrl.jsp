<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File,m2soft.rdsystem.server.core.install.Message" %>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../m2soft/JSchListCtrl.java" %>
<%@ include file="../control/lib/JLJsp.jsp" %>
<%@ include file="../control/lib/JLObj.java" %>
<%@ include file="../control/lib/JLRuntimeClass.java" %>
<%@ include file="../control/lib/JSHttp.jsp" %>
<%@ include file="../control/lib/JLHttp.java" %>
<%@ include file="../control/lib/JLListCtrl.java" %>
<%@ include file="../control/lib/JSContextMenu.jsp" %>
<%@ include file="../control/lib/JLContextMenu.java" %>
<body bgcolor="#ffffff" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<%
initArg(request,out);
JSchListCtrl ctrl = new JSchListCtrl(out,session);
ctrl.initByParam(m_param);
ctrl.printList();
ctrl.printFinishScript();
%>
</body>