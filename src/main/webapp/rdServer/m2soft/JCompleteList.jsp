<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File "%>
<% String contentType1 = m2soft.rdsystem.server.core.install.Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="JCompleteListCtrl.java" %>

<%
	initArg(request,out);
%>

<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java" %>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java" %>
<%@ include file="../control/lib/JSListCtrl.jsp" %>
<%@ include file="../control/lib/JLListCtrl.java"%>




<body bgcolor="#FFFFFF" SCROLL ="no" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<%
	{	
		JCompleteListCtrl listCtrl = new JCompleteListCtrl(out);
		
		listCtrl.init("completelist","","","","",0);
		
		listCtrl.addHeader("",23,"","",1,0,"","",0,0);
		listCtrl.addHeader(Message.get("completefilelist_10"),50,"","",0,0,"","",1,0);
		listCtrl.addHeader(Message.get("completefilelist_02"),557,"","",0,0,"","",0,1);
		listCtrl.addHeader(Message.get("completefilelist_03"),130,"","",0,0,"","",0,2);
		
		listCtrl.setPageSize(20);
		listCtrl.setStripe(2);
		listCtrl.setStripeBgColor(0,"#F0F0D9");
		listCtrl.setStripeBgColor(1,"#ffffff");
		
		listCtrl.setBtnFace("#B0C4DE");

		listCtrl.printObj();
	}
%>

