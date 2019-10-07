<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File,m2soft.rdsystem.server.core.install.Message" %>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>

<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JLMenuBar.java"%>
<%@ include file="../control/lib/JLToolBar.java"%>
<%@ include file="../control/lib/JLSplitter.java"%>
<%@ include file="../control/lib/JLFrame.java"%>
<%@ include file="../control/lib/JLMultiFrame.java"%>
<%@ include file="../control/lib/JLContextMenu.java"%>
<%@ include file="../control/lib/JLPopupMenu.java"%>
<%@ include file="../control/lib/JLTabCtrl.java"%>

<%
	initArg(request,out);
%>

<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">


<%
	debugPrint("JRptCharSet.jsp");
	debugPrint(m_param);
%>
